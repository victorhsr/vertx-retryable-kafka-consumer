package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate;

import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.port.MoviesByTagPort;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.port.PublishRecommendationsGeneratedEventPort;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.github.victorhsr.retry.recommendations.event.RecommendationsGeneratedEvent;
import io.github.victorhsr.retry.recommendations.processor.RecommendationsProcessingResult;
import io.github.victorhsr.retry.recommendations.processor.RecommendationsProcessor;
import io.github.victorhsr.retry.recommendations.processor.dto.MovieData;
import io.github.victorhsr.retry.recommendations.processor.dto.PersonData;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class DefaultGenerateRecommendationsUC implements GenerateRecommendationsUC {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGenerateRecommendationsUC.class);

    private final RecommendationsProcessor recomendationsProcessor;
    private final MoviesByTagPort moviesByTagPort;
    private final PublishRecommendationsGeneratedEventPort publishRecommendationsGeneratedEventPort;

    public DefaultGenerateRecommendationsUC(RecommendationsProcessor recomendationsProcessor,
                                            MoviesByTagPort moviesByTagPort,
                                            PublishRecommendationsGeneratedEventPort publishRecommendationsGeneratedEventPort) {

        this.recomendationsProcessor = recomendationsProcessor;
        this.moviesByTagPort = moviesByTagPort;
        this.publishRecommendationsGeneratedEventPort = publishRecommendationsGeneratedEventPort;
    }

    @Override
    public Single<Recommendations> generateRecomendations(final GenerateRecommendationsCommand command) {
        return Single.create(emitter -> {

            final PersonData personData = command.getPayload();

            this.moviesByTagPort.getMoviesByTag(personData.getPreferences())
                    .collect(Collectors.toList())
                    .subscribe(movies -> {
                        this.recomendationsProcessor.proccess(personData, movies, asyncResult -> {
                            if (asyncResult.failed()) {
                                emitter.onError(asyncResult.cause());
                                return;
                            }

                            final RecommendationsProcessingResult processingResult = asyncResult.result();
                            LOGGER.info("Recomendacoes processadas, resultado: {}", processingResult);

                            final Recommendations recommendations = Recommendations.builder()
                                    .id(UUID.randomUUID().toString())
                                    .movies(processingResult.getMovies().stream().map(MovieData::getId).collect(Collectors.toList()))
                                    .person(personData.getId())
                                    .build();

                            final RecommendationsGeneratedEvent event = this.createEvent(command, recommendations);

                            LOGGER.info("Publicando evento de geracao de recomendacoes: '{}'...", event.getId());
                            this.publishRecommendationsGeneratedEventPort.publishRecommendationsGeneratedEvent(event)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(() -> {
                                        LOGGER.info("Evento '{}' publicado com sucesso!", event.getId());
                                        emitter.onSuccess(recommendations);
                                    }, error -> {
                                        LOGGER.error("Falha na publicacao do evento '{}'...", event.getId());
                                        emitter.onError(error);
                                    });
                        });
                    }, emitter::onError);
        });
    }

    private RecommendationsGeneratedEvent createEvent(final GenerateRecommendationsCommand command, final Recommendations recommendations) {
        final RecommendationsGeneratedEvent event = RecommendationsGeneratedEvent.builder()
                .id(recommendations.getId())
                .issuedAt(command.getIssuedAt())
                .answeredAt(LocalDateTime.now())
                .payload(recommendations)
                .build();

        return event;
    }

}
