package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.filterbyperson;

import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.filterbyperson.port.RecommendationsByPersonIdPort;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Optional;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class DefaultFilterRecommendationsByPersonUC implements FilterRecommendationsByPersonUC {

    private final RecommendationsByPersonIdPort recommendationsByPersonIdPort;

    public DefaultFilterRecommendationsByPersonUC(RecommendationsByPersonIdPort recommendationsByPersonIdPort) {
        this.recommendationsByPersonIdPort = recommendationsByPersonIdPort;
    }

    @Override
    public Single<Optional<Recommendations>> getRecommendationsFrom(final FindRecommendationsByPersonCommand command) {
        return this.recommendationsByPersonIdPort.getRecommendations(command.getPayload()).subscribeOn(Schedulers.io());
    }
}
