package io.github.victorhsr.retry.recommendations.processor.verticle.core;

import io.github.victorhsr.retry.recommendations.processor.RecommendationsProcessingResult;
import io.github.victorhsr.retry.recommendations.processor.RecommendationsProcessor;
import io.github.victorhsr.retry.recommendations.processor.dto.MovieData;
import io.github.victorhsr.retry.recommendations.processor.dto.PersonData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.impl.future.SucceededFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Year;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementacao de {@link RecommendationsProcessor} que aplica uma regra de idade aos filmes,
 * caso um filme seja mais velho que a idade estabelecida
 * ele nao sera escolhido, a nao ser que seja um <b>classico</b>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class TooOldButItsAClassicProcessor implements RecommendationsProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TooOldButItsAClassicProcessor.class);

    private final int movieMaxAge;
    private final String classicMovieTag;

    public TooOldButItsAClassicProcessor(int movieMaxAge, String classicMovieTag) {
        this.movieMaxAge = movieMaxAge;
        this.classicMovieTag = classicMovieTag;
    }

    @Override
    public void proccess(final PersonData personData, final List<MovieData> movies, final Handler<AsyncResult<RecommendationsProcessingResult>> resultHandler) {
        LOGGER.info("Processando recomendacoes para o usuario '{}'", personData.getId());

        final Predicate<Integer> oldMoviePredicate = movieReleaseYear -> movieReleaseYear >= (Year.now().getValue() - this.movieMaxAge);
        final Predicate<List<String>> classicMoviePredicate = tags -> tags.contains(classicMovieTag);

        final List<MovieData> moviesToRecommend = movies.stream()
                .filter(movie -> oldMoviePredicate.test(movie.getReleaseYear()) || classicMoviePredicate.test(movie.getTags()))
                .collect(Collectors.toList());

        final RecommendationsProcessingResult recommendations = new RecommendationsProcessingResult(moviesToRecommend);

        final SucceededFuture<RecommendationsProcessingResult> recommendationsSucceededFuture = new SucceededFuture<>(recommendations);
        LOGGER.info("Processamento concluido");
        resultHandler.handle(recommendationsSucceededFuture);
    }

}
