package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.filterbyperson;

import io.github.victorhsr.retry.AbstractCommand;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.reactivex.rxjava3.core.Single;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Caso de uso onde eh possivel recuperar {@link Recommendations}
 * a partir de um usuario
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface FilterRecommendationsByPersonUC {

    Single<Optional<Recommendations>> getRecommendationsFrom(final FindRecommendationsByPersonCommand command);

    class FindRecommendationsByPersonCommand extends AbstractCommand<String> {

        @Builder
        FindRecommendationsByPersonCommand(LocalDateTime issuedAt, String payload) {
            super(issuedAt, payload);
        }
    }
}
