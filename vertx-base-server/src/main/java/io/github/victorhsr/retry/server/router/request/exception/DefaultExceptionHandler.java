package io.github.victorhsr.retry.server.router.request.exception;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

/**
 * Handler padrao para excecoes lancadas
 * no router
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class DefaultExceptionHandler implements Handler<RoutingContext> {

    @Override
    public void handle(final RoutingContext routingContext) {
        routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
    }
}
