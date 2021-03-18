package io.github.victorhsr.retry.catalog.infraestructure.adapter;

import io.github.victorhsr.retry.catalog.core.movie.Movie;
import io.github.victorhsr.retry.catalog.core.movie.usecase.details.port.MovieByIdPort;
import io.github.victorhsr.retry.catalog.infraestructure.adapter.repository.entity.MovieEntity;
import io.github.victorhsr.retry.catalog.infraestructure.adapter.repository.entity.MovieMapper;
import io.github.victorhsr.retry.jdbc.SqlCommand;
import io.github.victorhsr.retry.jdbc.operation.ReactiveJdbcOperations;
import io.reactivex.rxjava3.core.Single;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowIterator;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Repository
public class MovieByIdAdapter implements MovieByIdPort {

    private final ReactiveJdbcOperations reactiveJdbcOperations;
    private final MovieMapper movieMapper;

    public MovieByIdAdapter(ReactiveJdbcOperations reactiveJdbcOperations) {
        this.reactiveJdbcOperations = reactiveJdbcOperations;
        movieMapper = MovieMapper.INSTANCE;
    }

    @Override
    public Single<Optional<Movie>> findById(final String id) {
        return Single.create(emitter -> {

            final String query = "SELECT data FROM t_movie WHERE id = $1";
            final SqlCommand sqlCommand = new SqlCommand(query, Tuple.of(id));

            final Function<RowSet<Row>, MovieEntity> extractor = rows -> {

                final RowIterator<Row> rowIterator = rows.iterator();

                if (!rowIterator.hasNext()) {
                    return null;
                }

                final Row row = rowIterator.next();
                final MovieEntity movieEntity = row.getJsonObject(0).mapTo(MovieEntity.class);
                return movieEntity;
            };

            this.reactiveJdbcOperations.executeQuery(sqlCommand, extractor)
                    .map(opt -> opt.map(this.movieMapper::entityToMovie))
                    .subscribe(emitter::onSuccess, emitter::onError);
        });
    }

}
