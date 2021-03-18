package io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository;

import io.github.victorhsr.retry.jdbc.SqlCommand;
import io.github.victorhsr.retry.jdbc.operation.ReactiveJdbcOperations;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.details.port.RecommendationsByIdPort;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.dto.RecommendationsEntityExtractor;
import io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.dto.RecommendationsMapper;
import io.reactivex.rxjava3.core.Single;
import io.vertx.sqlclient.Tuple;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <p>
 * output adapter. Implementacao que recupera {@link Recommendations} a
 * partir do seu ID
 * </p>
 *
 * <p>
 * TL;DR neste servico, estamos utilizando event-sourcing junto com
 * cqrs. Nossa source of truth sao as filas do kafka com nossos eventos,
 * porem a consulta de exibicao esta sendo feita em sgbd relacional
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Repository
public class RecommendationsByIdAdatper implements RecommendationsByIdPort {

    private final ReactiveJdbcOperations reactiveJdbcOperations;
    private final RecommendationsMapper recommendationsMapper;

    public RecommendationsByIdAdatper(ReactiveJdbcOperations reactiveJdbcOperations) {
        this.reactiveJdbcOperations = reactiveJdbcOperations;
        this.recommendationsMapper = RecommendationsMapper.INSTANCE;
    }

    @Override
    public Single<Optional<Recommendations>> findById(final String id) {
        return Single.create(emitter -> {
            final String query = "SELECT data FROM t_recommendations WHERE id = $1";
            final SqlCommand sqlCommand = new SqlCommand(query, Tuple.of(id));

            this.reactiveJdbcOperations.executeQuery(sqlCommand, RecommendationsEntityExtractor::extract)
                    .map(opt -> opt.map(this.recommendationsMapper::entityToRecommendations))
                    .subscribe(emitter::onSuccess, emitter::onError);
        });
    }
}
