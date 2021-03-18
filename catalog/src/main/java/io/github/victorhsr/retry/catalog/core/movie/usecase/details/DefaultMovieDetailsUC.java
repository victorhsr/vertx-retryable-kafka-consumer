package io.github.victorhsr.retry.catalog.core.movie.usecase.details;

import io.github.victorhsr.retry.catalog.core.movie.Movie;
import io.github.victorhsr.retry.catalog.core.movie.usecase.details.port.MovieByIdPort;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Optional;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class DefaultMovieDetailsUC implements MovieDetailsUC {

    private final MovieByIdPort movieByIdPort;

    public DefaultMovieDetailsUC(MovieByIdPort movieByIdPort) {
        this.movieByIdPort = movieByIdPort;
    }

    @Override
    public Single<Optional<Movie>> getDetails(MovieDetailsCommand command) {
        return this.movieByIdPort.findById(command.getPayload()).subscribeOn(Schedulers.io());
    }
}
