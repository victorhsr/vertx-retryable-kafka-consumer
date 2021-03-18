package io.github.victorhsr.retry.recommendations.application.rest.v1.recommendations.details;

import io.github.victorhsr.retry.recommendations.application.rest.v1.recommendations.details.dto.RecommendationsMapper;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.filterbyperson.FilterRecommendationsByPersonUC;
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
 * ao caso de uso {@link FilterRecommendationsByPersonUC}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Controller
public class FilterRecommendationsByPersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterRecommendationsByPersonController.class);
    private static final String PATH_VARIABLE = "recommendationsId";

    private final FilterRecommendationsByPersonUC filterRecommendationsByPersonUC;
    private final RecommendationsMapper recommendationsMapper;

    public FilterRecommendationsByPersonController(RouterProvider routerProvider, FilterRecommendationsByPersonUC filterRecommendationsByPersonUC) {
        this.filterRecommendationsByPersonUC = filterRecommendationsByPersonUC;
        this.registerController(routerProvider);
        this.recommendationsMapper = RecommendationsMapper.INSTANCE;
    }

    private void registerController(final RouterProvider routerProvider) {
        final ControllerIdentifier controllerIdentifier = new ControllerIdentifier(HttpMethod.GET, String.format("/v1/person/:%s", PATH_VARIABLE));
        routerProvider.registerController(controllerIdentifier, this::handleDetails);
    }

    private void handleDetails(final RoutingContext routingContext) {

        final String personId = routingContext.pathParam(PATH_VARIABLE);
        LOGGER.info("Tratando requisicao, buscando recomendacoes para a pessoa '{}'", personId);

        final FilterRecommendationsByPersonUC.FindRecommendationsByPersonCommand command = FilterRecommendationsByPersonUC.FindRecommendationsByPersonCommand.builder()
                .issuedAt(LocalDateTime.now())
                .payload(personId)
                .build();

        final HttpServerResponse httpServerResponse = routingContext.response();
        this.filterRecommendationsByPersonUC.getRecommendationsFrom(command)
                .map(recommendationsOpt -> recommendationsOpt.map(this.recommendationsMapper::recommendationsToRecommendationsDetails))
                .subscribe(dtoOpt -> {
                    if (!dtoOpt.isPresent()) {
                        LOGGER.warn("Nao foram encontradas recomendacoes para o usuario '{}'", personId);
                        httpServerResponse.setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
                        return;
                    }

                    LOGGER.info("Recomendacoes encontradas '{}'", dtoOpt.get());
                    httpServerResponse.end(JsonObject.mapFrom(dtoOpt.get()).encode());
                }, error -> {
                    routingContext.fail(error);
                });
    }


}
