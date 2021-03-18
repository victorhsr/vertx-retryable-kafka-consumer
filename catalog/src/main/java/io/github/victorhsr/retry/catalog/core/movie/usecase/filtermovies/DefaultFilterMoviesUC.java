package io.github.victorhsr.retry.catalog.core.movie.usecase.filtermovies;

import io.github.victorhsr.retry.catalog.core.movie.Movie;
import io.github.victorhsr.retry.catalog.core.movie.usecase.filtermovies.port.FilterMoviesByTagsPort;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.List;

/**
 * Implementacao padrao de {@link FilterMoviesUC}, aqui aplicamos um bug
 * proposital, caso nenhuma tag seja passada para filtragem
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class DefaultFilterMoviesUC implements FilterMoviesUC {

    private FilterMoviesByTagsPort filterMoviesByTagsPort;

    public DefaultFilterMoviesUC(FilterMoviesByTagsPort filterMoviesByTagsPort) {
        this.filterMoviesByTagsPort = filterMoviesByTagsPort;
    }

    @Override
    public Observable<Movie> filterMovies(final FilterMoviesCommand command) {
        final List<String> tagsToFilter = command.getPayload();

        /**
         * Aqui introduzimos nosso bug proposital,
         * se nao forem passadas tagsToFilter no comando,
         * iremos causar uma falha inesperada no caso de uso
         */
        if (tagsToFilter.isEmpty()) {
            throw new RuntimeException("Nenhuma tag informada");
        }

        return this.filterMoviesByTagsPort.filterByTags(tagsToFilter).subscribeOn(Schedulers.io());
    }
}
