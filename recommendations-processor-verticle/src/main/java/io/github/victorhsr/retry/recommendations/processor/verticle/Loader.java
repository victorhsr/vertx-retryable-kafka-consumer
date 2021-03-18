package io.github.victorhsr.retry.recommendations.processor.verticle;

import io.github.victorhsr.retry.recommendations.processor.verticle.infraestructure.verticle.RecommendationsProcessorVerticle;
import io.github.victorhsr.retry.server.context.RetryContextLoader;
import io.github.victorhsr.retry.server.profile.ProfileSetUp;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);
    private static final String BASE_PACKAGE = "io.github.victorhsr.retry";

    public static void main(String[] args) {

        ProfileSetUp profileSetUp = new ProfileSetUp();
        profileSetUp.setUp();

        RetryContextLoader.loadContext(BASE_PACKAGE)
                .subscribe(() -> {
                    final Vertx vertx = RetryContextLoader.getBean(Vertx.class);
                    final RecommendationsProcessorVerticle recommendationsProcessorVerticle = RetryContextLoader.getBean(RecommendationsProcessorVerticle.class);

                    vertx.deployVerticle(recommendationsProcessorVerticle)
                            .onSuccess(deployId -> LOGGER.info("Verticle iniciado, id {}", deployId))
                            .onFailure(Throwable::printStackTrace);

                }, Throwable::printStackTrace);
    }
}
