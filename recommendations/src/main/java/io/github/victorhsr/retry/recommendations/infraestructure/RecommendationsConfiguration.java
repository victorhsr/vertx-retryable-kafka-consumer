package io.github.victorhsr.retry.recommendations.infraestructure;

import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.details.DefaultRecommendationsDetailsUC;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.details.RecommendationsDetailsUC;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.details.port.RecommendationsByIdPort;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.filterbyperson.DefaultFilterRecommendationsByPersonUC;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.filterbyperson.FilterRecommendationsByPersonUC;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.filterbyperson.port.RecommendationsByPersonIdPort;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.DefaultGenerateRecommendationsUC;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.GenerateRecommendationsUC;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.port.MoviesByTagPort;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.port.PublishRecommendationsGeneratedEventPort;
import io.github.victorhsr.retry.recommendations.infraestructure.cluster.ClusteredVertxProvider;
import io.github.victorhsr.retry.recommendations.processor.RecommendationsProcessor;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
public class RecommendationsConfiguration {

    @Bean
    public GenerateRecommendationsUC getGenerateRecommendationsUC(final RecommendationsProcessor recommendationsProcessor,
                                                                  final MoviesByTagPort moviesByTagPort,
                                                                  final PublishRecommendationsGeneratedEventPort publishRecommendationsGeneratedEventPort) {
        return new DefaultGenerateRecommendationsUC(recommendationsProcessor, moviesByTagPort, publishRecommendationsGeneratedEventPort);
    }

    @Bean
    public RecommendationsDetailsUC getRecommendationsDetailsUC(final RecommendationsByIdPort recommendationsByIdPort) {
        return new DefaultRecommendationsDetailsUC(recommendationsByIdPort);
    }

    @Bean
    public FilterRecommendationsByPersonUC getFilterRecommendationsByUserUC(final RecommendationsByPersonIdPort recommendationsByPersonIdPort) {
        return new DefaultFilterRecommendationsByPersonUC(recommendationsByPersonIdPort);
    }

    /**
     * <p>
     * Neste exemplo estamos considerando que {@link RecommendationsProcessor} seja
     * uma operacao cara o suficiente para escalarmos por meio de verticles especificos
     * em nosso cluster.
     * </p>
     *
     * <p>
     * Para acesar esse verticle estamos utilizando o vertx event-bus, que esta
     * abstraido na forma do vertx-service-proxy. A comunicacao entre os nos esta encarregada
     * pelo proprio vertx {@link ClusteredVertxProvider#getClusteredVertx(List)}
     * </p>
     *
     * @param vertx                         clustered vertx instance, que ira lidar com a comunicacao entre os nos do cluster
     * @param recomendationProcessorAddress endereco do servico que estamos enderecando com este proxy. Note que este
     *                                      encedeco nao funciona como uma rota absoluta, e sim como uma chave a qual
     *                                      varios servicos do mesmo tipo espalhados pelo cluster podem se registrar e
     *                                      entao o trabalho estara sendo distribuido entre estes nos do cluster
     * @return
     */
    @Bean
    public RecommendationsProcessor recomendationsProcessor(final Vertx vertx, final @Value("${recommendations.processor.address}") String recomendationProcessorAddress) {
        return RecommendationsProcessor.createProxy(vertx, recomendationProcessorAddress);
    }

}
