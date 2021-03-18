package io.github.victorhsr.retry.recommendations.processor;

import io.github.victorhsr.retry.recommendations.processor.dto.MovieData;
import io.github.victorhsr.retry.recommendations.processor.dto.PersonData;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;

@ProxyGen
@VertxGen
public interface RecommendationsProcessor {

    void proccess(final PersonData personData, final List<MovieData> movies, final Handler<AsyncResult<RecommendationsProcessingResult>> resultHandler);

    static RecommendationsProcessor createProxy(final Vertx vertx, final String address) {
        return new RecommendationsProcessorVertxEBProxy(vertx, address);
    }

}
