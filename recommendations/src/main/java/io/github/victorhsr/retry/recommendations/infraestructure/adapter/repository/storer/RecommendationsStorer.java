package io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.storer;

import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.reactivex.rxjava3.core.Completable;

/**
 * <p>
 * Contrato para lidar com armazenamento para leitura
 * de {@link Recommendations}
 * </p>
 *
 * <p>
 * TL;DR neste servico, estamos utilizando event-sourcing junto com
 * cqrs. Nossa source of truth sao as filas do kafka com nossos eventos,
 * porem a consulta de exibicao esta sendo feita em um banco de dados especifico
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface RecommendationsStorer {

    Completable persist(final Recommendations recommendationsToPersist);

}
