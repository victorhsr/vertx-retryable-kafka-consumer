package io.github.victorhsr.retry.recommendations.event;

import io.github.victorhsr.retry.AbstractDomainEvent;
import io.github.victorhsr.retry.recommendations.event.definitions.RecommendationsEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Evento lancado ao se gerar {@link Recommendations}
 * no sistema
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@AllArgsConstructor
public class RecommendationsGeneratedEvent extends AbstractDomainEvent<Recommendations> {

    @Builder
    public RecommendationsGeneratedEvent(String id, LocalDateTime issuedAt, LocalDateTime answeredAt, Recommendations payload) {
        super(id, issuedAt, answeredAt, payload, RecommendationsEventType.RECOMMENDATIONS_GENERATED.getType());
    }

}
