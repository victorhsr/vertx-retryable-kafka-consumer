package io.github.victorhsr.retry.server.router.request.proxy.decode;

import io.github.victorhsr.retry.server.router.request.exception.ExceptionHandler;
import io.github.victorhsr.retry.server.router.request.exception.RequestFailureHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.DecodeException;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class DecodeExceptionHandler {

    public DecodeExceptionHandler(RequestFailureHandler requestFailureHandler) {
        requestFailureHandler.registerHandler(this);
    }

    @ExceptionHandler(DecodeException.class)
    public void handle(final RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                .end("Corpo da requisicao mal formatado");
    }

}
