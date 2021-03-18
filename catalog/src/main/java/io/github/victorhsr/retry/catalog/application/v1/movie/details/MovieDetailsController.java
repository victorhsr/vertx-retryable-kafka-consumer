package io.github.victorhsr.retry.catalog.application.v1.movie.details;

import io.github.victorhsr.retry.catalog.application.v1.movie.details.dto.MovieMapper;
import io.github.victorhsr.retry.catalog.core.movie.usecase.details.MovieDetailsUC;
import io.github.victorhsr.retry.server.router.ControllerIdentifier;
import io.github.victorhsr.retry.server.router.provider.RouterProvider;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * input/driving adapter, para recepcao de requisicoes HTTP, correspondentes
 * ao caso de uso {@link MovieDetailsUC}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Controller
public class MovieDetailsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieDetailsController.class);
    private static final String PATH_VARIABLE = "movieId";

    private final MovieMapper movieMapper;
    private final MovieDetailsUC movieDetailsUC;

    public MovieDetailsController(RouterProvider routerProvider, MovieDetailsUC movieDetailsUC) {
        this.movieDetailsUC = movieDetailsUC;
        movieMapper = MovieMapper.INSTANCE;
        this.registerController(routerProvider);
    }

    private void registerController(final RouterProvider routerProvider) {
        final ControllerIdentifier controllerIdentifier = new ControllerIdentifier(HttpMethod.GET, String.format("/v1/details/:%s", PATH_VARIABLE));
        routerProvider.registerController(controllerIdentifier, this::handleDetails);
    }

    private void handleDetails(final RoutingContext routingContext) {

        final String movieId = routingContext.pathParam(PATH_VARIABLE);
        LOGGER.info("Tratando requisicao, buscando filme '{}'", movieId);

        final MovieDetailsUC.MovieDetailsCommand command = MovieDetailsUC.MovieDetailsCommand.builder()
                .issuedAt(LocalDateTime.now())
                .payload(movieId)
                .build();

        final HttpServerResponse httpServerResponse = routingContext.response();
        this.movieDetailsUC.getDetails(command)
                .map(recommendationsOpt -> recommendationsOpt.map(this.movieMapper::movieToMovieDetailsDTO))
                .subscribe(dtoOpt -> {
                    if (!dtoOpt.isPresent()) {
                        LOGGER.info("Filme '{}' nao encontrado", movieId);
                            httpServerResponse.setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
                        return;
                    }

                    LOGGER.info("Filme encontrado: '{}'", dtoOpt.get());
                    httpServerResponse.end(JsonObject.mapFrom(dtoOpt.get()).encode());
                }, error -> {
                    routingContext.fail(error);
                });
    }

}
