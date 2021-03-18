package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.details;

import io.github.victorhsr.retry.AbstractCommand;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.reactivex.rxjava3.core.Single;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Caso de uso onde eh possivel recuperar detalhes
 * de {@link Recommendations}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface RecommendationsDetailsUC {

    Single<Optional<Recommendations>> getDetails(final RecommendationsDetailsCommand command);

    class RecommendationsDetailsCommand extends AbstractCommand<String> {

        @Builder
        RecommendationsDetailsCommand(LocalDateTime issuedAt, String payload) {
            super(issuedAt, payload);
        }
    }

}
