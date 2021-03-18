package io.github.victorhsr.retry.recommendations.infraestructure.adapter.catalog;

import io.github.victorhsr.retry.commons.request.HttpResponseHandlerBuilder;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.port.MoviesByTagPort;
import io.github.victorhsr.retry.recommendations.processor.dto.MovieData;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * output adapter. Implementacao que recupera os
 * filmes a partir de uma requisicao http ao catalog aggregate
 * </p>
 *
 * <p>
 * TL;DR neste programa de exemplo, estamos considerando que nao
 * fazemos uso de um event-carried state transfer (nao persistimos os dados
 * advindo de eventos externos em nosso proprigo aggregate), logo
 * para se conseguir dados sobre os filmes, precisamos solicitar diretamente
 * ao catalog aggregate
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class CatalogAdapter implements MoviesByTagPort {

    private final WebClient webClient;

    private final int catalogTimeOut;
    private final String catalogLocation;
    private final String catalogMovieFilterRelativeLocation;
    private final String catalogMovieTagParamName;

    public CatalogAdapter(WebClient webClient,
                          @Value("${catalog.time-out}") int catalogTimeOut,
                          @Value("${catalog.location}") String catalogLocation,
                          @Value("${catalog.movie-filter.location}") String catalogMovieFilterRelativeLocation,
                          @Value("${catalog.movie-tag.param}") String catalogMovieTagParamName  ) {

        this.webClient = webClient;
        this.catalogTimeOut = catalogTimeOut;
        this.catalogLocation = catalogLocation;
        this.catalogMovieFilterRelativeLocation = catalogMovieFilterRelativeLocation;
        this.catalogMovieTagParamName = catalogMovieTagParamName;
    }

    @Override
    public Observable<MovieData> getMoviesByTag(final List<String> tags) {
        return Observable.create(emitter -> {
            final String movieInfoLocation = this.catalogLocation.concat(this.catalogMovieFilterRelativeLocation);
            final String queryParams = tags.stream()
                    .reduce((prev, next) -> String.format("%s,%s", prev, next)).orElseGet(() -> "");

            this.webClient.getAbs(movieInfoLocation)
                    .addQueryParam(this.catalogMovieTagParamName, queryParams)
                    .timeout(catalogTimeOut)
                    .send()
                    .onSuccess(httpResponse -> {

                        new HttpResponseHandlerBuilder()
                                .serverErrorHandler((hr) -> emitter.onError(new RuntimeException("Falha no catalog aggregate")))
                                .notFoundHandler((hr) -> emitter.onError(new RuntimeException("Catalog aggregate nao encontrado")))
                                .okHandler((hr) -> {
                                    Arrays.stream(Json.decodeValue(hr.body(), MovieData[].class)).forEach(emitter::onNext);
                                    emitter.onComplete();
                                })
                                .build()
                                .handle(httpResponse);
                    })
                    .onFailure(error -> {
                        emitter.onError(error);
                    });
        });
    }

}
