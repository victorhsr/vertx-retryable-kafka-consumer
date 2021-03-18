package io.github.victorhsr.retry.catalog.application.v1.movie.filter;

import io.github.victorhsr.retry.catalog.core.movie.usecase.filtermovies.FilterMoviesUC;
import io.github.victorhsr.retry.catalog.application.v1.movie.details.dto.MovieMapper;
import io.github.victorhsr.retry.server.router.ControllerIdentifier;
import io.github.victorhsr.retry.server.router.provider.RouterProvider;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * input/driving adapter, para recepcao de requisicoes HTTP, correspondentes
 * ao caso de uso {@link FilterMoviesUC}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Controller
public class FilterMoviesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterMoviesController.class);

    private final FilterMoviesUC filterMoviesUC;
    private final MovieMapper movieMapper;

    public FilterMoviesController(RouterProvider routerProvider, FilterMoviesUC filterMoviesUC) {
        this.filterMoviesUC = filterMoviesUC;
        this.movieMapper = MovieMapper.INSTANCE;
        this.registerController(routerProvider);
    }

    private void registerController(final RouterProvider routerProvider) {
        final ControllerIdentifier controllerIdentifier = new ControllerIdentifier(HttpMethod.GET, "/v1/filter");
        routerProvider.registerController(controllerIdentifier, this::handleFilter);
    }

    public void handleFilter(final RoutingContext routingContext) {

        LOGGER.info("Tratando requisicao...");

        final String tagsParam = Optional.ofNullable(routingContext.request().getParam("tags"))
                .orElseGet(() -> {
                    LOGGER.warn("Nenhuma palavra chave presente na requisicao");
                    return "";
                });

        final String[] tagsArray = tagsParam.isEmpty() ? new String[0] : tagsParam.split(",");

        final List<String> tags = Arrays.asList(tagsArray);
        final FilterMoviesUC.FilterMoviesCommand command = FilterMoviesUC.FilterMoviesCommand.builder()
                .issuedAt(LocalDateTime.now())
                .payload(tags)
                .build();

        this.filterMoviesUC.filterMovies(command)
                .map(this.movieMapper::movieToMovieDetailsDTO)
                .collect(Collectors.toList())
                .subscribe(movies -> {
                    LOGGER.info("Filmes filtrados com sucesso: {}, encerrando requisicao", movies);
                    routingContext.response().end(new JsonArray(movies).encode());
                }, routingContext::fail);
    }

}
