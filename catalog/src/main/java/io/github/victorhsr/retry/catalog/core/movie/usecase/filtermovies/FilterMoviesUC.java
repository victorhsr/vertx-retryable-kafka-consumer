package io.github.victorhsr.retry.catalog.core.movie.usecase.filtermovies;

import io.github.victorhsr.retry.AbstractCommand;
import io.github.victorhsr.retry.catalog.core.movie.Movie;
import io.reactivex.rxjava3.core.Observable;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Caso de uso onde um ator podera aplicar uma filtragem
 * nos {@link Movie} registrados. Essa filtragem sera feita a partir
 * das tags de cada filme.
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface FilterMoviesUC {

    Observable<Movie> filterMovies(final FilterMoviesCommand command);

    class FilterMoviesCommand extends AbstractCommand<List<String>> {

        @Builder
        FilterMoviesCommand(LocalDateTime issuedAt, List<String> payload) {
            super(issuedAt, payload);
        }
    }

}
