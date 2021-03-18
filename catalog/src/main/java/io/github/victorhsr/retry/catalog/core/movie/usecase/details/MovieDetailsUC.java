package io.github.victorhsr.retry.catalog.core.movie.usecase.details;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/

import io.github.victorhsr.retry.AbstractCommand;
import io.github.victorhsr.retry.catalog.core.movie.Movie;
import io.reactivex.rxjava3.core.Single;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Caso de uso onde eh possivel recuperar detalhes
 * de {@link Movie}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface MovieDetailsUC {

    Single<Optional<Movie>> getDetails(final MovieDetailsCommand command);

    class MovieDetailsCommand extends AbstractCommand<String> {

        @Builder
        MovieDetailsCommand(LocalDateTime issuedAt, String payload) {
            super(issuedAt, payload);
        }
    }

}
