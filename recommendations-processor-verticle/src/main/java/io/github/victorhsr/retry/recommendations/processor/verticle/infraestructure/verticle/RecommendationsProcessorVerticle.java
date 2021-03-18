package io.github.victorhsr.retry.recommendations.processor.verticle.infraestructure.verticle;

import io.github.victorhsr.retry.recommendations.processor.RecommendationsProcessor;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Verticle responsavel por cuidar do registro do componente
 * {@link RecommendationsProcessor} no cluster
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class RecommendationsProcessorVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationsProcessorVerticle.class);

    private final String processorAddress;
    private final RecommendationsProcessor recomendationsProcessorToRegister;

    public RecommendationsProcessorVerticle(@Value("${recommendations.processor.address}") String processorAddress,
                                            RecommendationsProcessor recomendationsProcessorToRegister) {

        this.processorAddress = processorAddress;
        this.recomendationsProcessorToRegister = recomendationsProcessorToRegister;
    }

    @Override
    public void start() {

        LOGGER.info("Registrando {} no address '{}'", this.recomendationsProcessorToRegister.getClass().getSimpleName(), this.processorAddress);

        new ServiceBinder(this.vertx)
                .setAddress(this.processorAddress)
                .register(RecommendationsProcessor.class, this.recomendationsProcessorToRegister);
    }

}
