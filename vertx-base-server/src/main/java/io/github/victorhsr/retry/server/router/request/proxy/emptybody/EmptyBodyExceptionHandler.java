package io.github.victorhsr.retry.server.router.request.proxy.emptybody;

import io.github.victorhsr.retry.server.router.request.exception.ExceptionHandler;
import io.github.victorhsr.retry.server.router.request.exception.RequestFailureHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class EmptyBodyExceptionHandler {

    public EmptyBodyExceptionHandler(RequestFailureHandler requestFailureHandler) {
        requestFailureHandler.registerHandler(this);
    }

    @ExceptionHandler(EmptyBodyException.class)
    public void handle(final RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                .end("Empty body");
    }
}
