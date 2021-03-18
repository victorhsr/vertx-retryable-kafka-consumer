package io.github.victorhsr.retry.commons.request;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Builder para instancias de {@link HttpResponseHandler},
 * onde se eh definido os status code e os respectivos handlers
 * para a resposta http
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class HttpResponseHandlerBuilder {

    private Map<Integer, Consumer<HttpResponse<Buffer>>> handlerStrategies = new HashMap<>();

    public HttpResponseHandlerBuilder notFoundHandler(final Consumer<HttpResponse<Buffer>> handler) {
        this.handlerStrategies.put(HttpResponseStatus.NOT_FOUND.code(), handler);
        return this;
    }

    public HttpResponseHandlerBuilder serverErrorHandler(final Consumer<HttpResponse<Buffer>> handler) {
        this.handlerStrategies.put(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), handler);
        return this;
    }

    public HttpResponseHandlerBuilder okHandler(final Consumer<HttpResponse<Buffer>> handler) {
        this.handlerStrategies.put(HttpResponseStatus.OK.code(), handler);
        return this;
    }


    public HttpResponseHandlerBuilder addHandler(final int statusCode, final Consumer<HttpResponse<Buffer>> handler) {
        this.handlerStrategies.put(statusCode, handler);
        return this;
    }


    public HttpResponseHandler build() {
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(handlerStrategies);
        this.handlerStrategies = new HashMap<>();

        return httpResponseHandler;
    }

    /**
     * Handler para respostas http que invoca o manipulador
     * especifico para a resposta de acordo com o status code
     * presente na resposta da requisicao
     */
    public class HttpResponseHandler {

        private final Map<Integer, Consumer<HttpResponse<Buffer>>> handlerStrategies;

        public HttpResponseHandler(Map<Integer, Consumer<HttpResponse<Buffer>>> handlerStrategies) {
            this.handlerStrategies = handlerStrategies;
        }

        /**
         * Verifica o status code de uma resposta http
         * e invoca seu respectivo handler
         *
         * @param httpResponse resposta http a ser verificada
         * @throws ResponseStatusCodeNotExpectedException caso o status code nao esteja mapeado
         */
        public void handle(final HttpResponse<Buffer> httpResponse) throws ResponseStatusCodeNotExpectedException {
            Optional.of(handlerStrategies.get(httpResponse.statusCode()))
                    .orElseThrow(() -> new ResponseStatusCodeNotExpectedException(String.format("Status code '%d' nao mapeado", httpResponse.statusCode())))
                    .accept(httpResponse);
        }
    }

}
