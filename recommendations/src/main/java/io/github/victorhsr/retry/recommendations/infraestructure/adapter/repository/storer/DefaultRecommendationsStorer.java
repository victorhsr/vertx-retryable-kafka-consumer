package io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.storer;

import io.github.victorhsr.retry.jdbc.SqlCommand;
import io.github.victorhsr.retry.jdbc.operation.ReactiveJdbcOperations;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.dto.RecommendationsEntity;
import io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.dto.RecommendationsMapper;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;
import org.springframework.stereotype.Repository;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Repository
public class DefaultRecommendationsStorer implements RecommendationsStorer {

    private final ReactiveJdbcOperations reactiveJdbcOperations;
    private final RecommendationsMapper recommendationsMapper;

    public DefaultRecommendationsStorer(ReactiveJdbcOperations reactiveJdbcOperations) {
        this.reactiveJdbcOperations = reactiveJdbcOperations;
        recommendationsMapper = RecommendationsMapper.INSTANCE;
    }

    @Override
    public Completable persist(final Recommendations recommendationsToPersist) {

        final RecommendationsEntity recommendationsEntity = this.recommendationsMapper.recommendationsToEntity(recommendationsToPersist);

        final String query = "INSERT INTO t_recommendations (id, data) VALUES ($1, $2) ON CONFLICT (id) DO NOTHING";
        final SqlCommand sqlCommand = new SqlCommand(query, Tuple.of(recommendationsEntity.getId(), JsonObject.mapFrom(recommendationsEntity)));

        return this.reactiveJdbcOperations.execute(sqlCommand);
    }
}
