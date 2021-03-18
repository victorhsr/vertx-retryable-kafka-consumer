package io.github.victorhsr.retry.catalog.infraestructure.adapter;

import io.github.victorhsr.retry.catalog.core.movie.Movie;
import io.github.victorhsr.retry.catalog.core.movie.usecase.filtermovies.port.FilterMoviesByTagsPort;
import io.github.victorhsr.retry.catalog.infraestructure.adapter.repository.entity.MovieEntity;
import io.github.victorhsr.retry.catalog.infraestructure.adapter.repository.entity.MovieMapper;
import io.github.victorhsr.retry.jdbc.SqlCommand;
import io.github.victorhsr.retry.jdbc.operation.ReactiveJdbcOperations;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * output adapter. Implementacao, com mock simples de filmes,
 * do port {@link FilterMoviesByTagsPort}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Repository
public class FilterMoviesByTagsAdapter implements FilterMoviesByTagsPort {

    private final ReactiveJdbcOperations reactiveJdbcOperations;
    private final MovieMapper movieMapper;

    public FilterMoviesByTagsAdapter(ReactiveJdbcOperations reactiveJdbcOperations) {
        this.reactiveJdbcOperations = reactiveJdbcOperations;
        movieMapper = MovieMapper.INSTANCE;
    }

    @Override
    public Observable<Movie> filterByTags(List<String> tags) {
        return Observable.create(emitter -> {

            final String query = "WITH tagged_movies AS ( SELECT movie.id, array_agg(tag) tags FROM t_movie AS movie, jsonb_array_elements_text(data -> 'tags') AS tag GROUP BY movie.id ) SELECT movie.data FROM t_movie AS movie JOIN tagged_movies AS tg ON movie.id = tg.id WHERE tg.tags && $1";

            final SqlCommand sqlCommand = new SqlCommand(query, Tuple.of(tags.toArray()));

            final Function<RowSet<Row>, List<Movie>> extractor = rows -> {

                final Function<Row, MovieEntity> rowToMovieEntity = row -> row.getJsonObject(0).mapTo(MovieEntity.class);

                final Iterable<Row> iterable = () -> rows.iterator();
                final List<Movie> movies = StreamSupport.stream(iterable.spliterator(), false)
                        .map(rowToMovieEntity)
                        .map(this.movieMapper::entityToMovie)
                        .collect(Collectors.toList());

                return movies;
            };

            this.reactiveJdbcOperations.executeQuery(sqlCommand, extractor)
                    .map(Optional::get)
                    .flatMapObservable(Observable::fromIterable)
                    .doOnError(Throwable::printStackTrace)
                    .subscribe(emitter::onNext, emitter::onError, emitter::onComplete);
        });
    }

}
