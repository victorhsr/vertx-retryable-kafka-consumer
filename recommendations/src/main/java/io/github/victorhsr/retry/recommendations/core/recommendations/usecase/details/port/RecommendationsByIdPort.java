package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.details.port;

import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.reactivex.rxjava3.core.Single;

import java.util.Optional;

/**
 * output port, define como poderemos recuperar {@link Recommendations}
 * persistidos no sistema
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface RecommendationsByIdPort {

    Single<Optional<Recommendations>> findById(final String id);

}
