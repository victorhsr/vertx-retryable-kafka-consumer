package io.github.victorhsr.retry.server.router.request.proxy.structure;

import io.github.victorhsr.retry.server.router.request.exception.ExceptionHandler;
import io.github.victorhsr.retry.server.router.request.exception.RequestFailureHandler;
import io.github.victorhsr.retry.server.router.request.proxy.emptybody.EmptyBodyException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class BadlyFormattedBodyExceptionHandler {

    public BadlyFormattedBodyExceptionHandler(RequestFailureHandler requestFailureHandler) {
        requestFailureHandler.registerHandler(this);
    }

    @ExceptionHandler(BadlyFormattedBodyException.class)
    public void handle(final RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                .end("Badly formatted request body");
    }
}
