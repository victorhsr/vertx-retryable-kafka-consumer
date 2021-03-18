package io.github.victorhsr.retry.catalog.core.movie.usecase.details.port;

import io.github.victorhsr.retry.catalog.core.movie.Movie;
import io.reactivex.rxjava3.core.Single;

import java.util.Optional;

/**
 * output port, define como poderemos recuperar {@link Movie}
 * persistidos no sistema
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface MovieByIdPort {

    Single<Optional<Movie>> findById(final String id);

}
