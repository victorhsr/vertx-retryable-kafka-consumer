package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.filterbyperson.port;

import io.github.victorhsr.retry.person.event.Person;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.reactivex.rxjava3.core.Single;

import java.util.Optional;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface RecommendationsByPersonIdPort {

    /**
     * Recupera {@link Recommendations} criadas para
     * um determinada {@link Person}
     *
     * @param personId id que representa um {@link Person} no sistema
     */
    Single<Optional<Recommendations>> getRecommendations(final String personId);

}
