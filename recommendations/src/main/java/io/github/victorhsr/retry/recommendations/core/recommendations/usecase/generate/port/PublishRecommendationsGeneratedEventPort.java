package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.port;

import io.github.victorhsr.retry.recommendations.event.RecommendationsGeneratedEvent;
import io.reactivex.rxjava3.core.Completable;

/**
 * output port, define como poderemos publicar o evento
 * de geracao de recomendacao de filmes {@link RecommendationsGeneratedEvent}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface PublishRecommendationsGeneratedEventPort {

    /**
     * Realiza a publicacao do evento de geracao de recomendacoes de filmes,
     * o ambiente de publicacao fica a criterio do adapter
     * implementado
     *
     * @param recommendationsGeneratedEvent evento lancado ao se gerar recomendacoes de filmes no sistema
     */
    Completable publishRecommendationsGeneratedEvent(final RecommendationsGeneratedEvent recommendationsGeneratedEvent);

}
