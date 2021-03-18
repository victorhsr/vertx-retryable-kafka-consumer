package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate;

import io.github.victorhsr.retry.AbstractCommand;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.github.victorhsr.retry.recommendations.processor.dto.PersonData;
import io.reactivex.rxjava3.core.Single;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Caso de uso onde eh possivel gerar recomendacoes
 * de filmes para uma pessoa
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface GenerateRecommendationsUC {

    Single<Recommendations> generateRecomendations(final GenerateRecommendationsCommand command);

    class GenerateRecommendationsCommand extends AbstractCommand<PersonData> {

        @Builder
        GenerateRecommendationsCommand(LocalDateTime issuedAt, PersonData payload) {
            super(issuedAt, payload);
        }
    }

}
