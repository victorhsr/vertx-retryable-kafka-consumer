package io.github.victorhsr.retry.recommendations.processor.verticle.infraestructure;

import io.github.victorhsr.retry.recommendations.processor.RecommendationsProcessor;
import io.github.victorhsr.retry.recommendations.processor.verticle.core.TooOldButItsAClassicProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
public class RecommendationsProcessorConfiguration {

    @Bean
    public RecommendationsProcessor recommendationsProcessor(@Value("${recommendations.processor.movie.max-age}") int movieMaxAge,
                                                             @Value("${recommendations.processor.movie.classic-tag}") String classicMovieTag) {
        return new TooOldButItsAClassicProcessor(movieMaxAge, classicMovieTag);
    }

}
