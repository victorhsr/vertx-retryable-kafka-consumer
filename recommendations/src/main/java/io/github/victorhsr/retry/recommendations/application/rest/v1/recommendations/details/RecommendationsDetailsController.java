package io.github.victorhsr.retry.recommendations.application.rest.v1.recommendations.details;

import io.github.victorhsr.retry.recommendations.application.rest.v1.recommendations.details.dto.RecommendationsMapper;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.details.RecommendationsDetailsUC;
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
 * ao caso de uso {@link RecommendationsDetailsUC}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Controller
public class RecommendationsDetailsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationsDetailsController.class);
    private static final String PATH_VARIABLE = "recommendationsId";

    private final RecommendationsDetailsUC recommendationsDetailsUC;
    private final RecommendationsMapper recommendationsMapper;

    public RecommendationsDetailsController(RouterProvider routerProvider, RecommendationsDetailsUC recommendationsDetailsUC) {
        this.recommendationsDetailsUC = recommendationsDetailsUC;
        this.registerController(routerProvider);
        this.recommendationsMapper = RecommendationsMapper.INSTANCE;
    }

    private void registerController(final RouterProvider routerProvider) {
        final ControllerIdentifier controllerIdentifier = new ControllerIdentifier(HttpMethod.GET, String.format("/v1/details/:%s", PATH_VARIABLE));
        routerProvider.registerController(controllerIdentifier, this::handleDetails);
    }

    private void handleDetails(final RoutingContext routingContext) {

        final String recommendationsId = routingContext.pathParam(PATH_VARIABLE);
        LOGGER.info("Tratando requisicao, buscando recomendacao '{}'", recommendationsId);

        final RecommendationsDetailsUC.RecommendationsDetailsCommand command = RecommendationsDetailsUC.RecommendationsDetailsCommand.builder()
                .issuedAt(LocalDateTime.now())
                .payload(recommendationsId)
                .build();

        final HttpServerResponse httpServerResponse = routingContext.response();
        this.recommendationsDetailsUC.getDetails(command)
                .map(recommendationsOpt -> recommendationsOpt.map(this.recommendationsMapper::recommendationsToRecommendationsDetails))
                .subscribe(dtoOpt -> {
                    if (!dtoOpt.isPresent()) {
                        LOGGER.warn("Recomendacoes '{}' nao encontradas", recommendationsId);
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
