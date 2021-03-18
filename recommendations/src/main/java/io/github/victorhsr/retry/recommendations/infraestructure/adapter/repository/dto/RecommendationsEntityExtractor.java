package io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.dto;

import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowIterator;
import io.vertx.sqlclient.RowSet;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class RecommendationsEntityExtractor {

    public static RecommendationsEntity extract(final RowSet<Row> rows) {
        final RowIterator<Row> rowIterator = rows.iterator();

        if (!rowIterator.hasNext()) {
            return null;
        }

        final Row row = rowIterator.next();
        final RecommendationsEntity recommendationsEntity = row.getJsonObject(0).mapTo(RecommendationsEntity.class);
        return recommendationsEntity;
    }

}
