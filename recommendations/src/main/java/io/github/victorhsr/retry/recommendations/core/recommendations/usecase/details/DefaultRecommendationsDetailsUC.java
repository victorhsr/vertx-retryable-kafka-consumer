package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.details;

import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.details.port.RecommendationsByIdPort;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Optional;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class DefaultRecommendationsDetailsUC implements RecommendationsDetailsUC {

    private final RecommendationsByIdPort recommendationsByIdPort;

    public DefaultRecommendationsDetailsUC(RecommendationsByIdPort recommendationsByIdPort) {
        this.recommendationsByIdPort = recommendationsByIdPort;
    }

    @Override
    public Single<Optional<Recommendations>> getDetails(final RecommendationsDetailsCommand command) {
        return this.recommendationsByIdPort.findById(command.getPayload()).subscribeOn(Schedulers.io());
    }

}
