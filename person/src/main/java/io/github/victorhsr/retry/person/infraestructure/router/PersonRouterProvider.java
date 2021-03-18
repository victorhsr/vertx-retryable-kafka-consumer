package io.github.victorhsr.retry.person.infraestructure.router;

import io.github.victorhsr.retry.server.router.RouterDefinition;
import io.github.victorhsr.retry.server.router.provider.AbstractRouterProvider;
import io.github.victorhsr.retry.server.router.request.exception.RequestFailureHandler;
import io.github.victorhsr.retry.server.router.request.proxy.RequestHandlerProxy;
import io.vertx.core.Vertx;
import org.springframework.stereotype.Component;

/**
 * classe com as definicoes de rota do aggregate
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class PersonRouterProvider extends AbstractRouterProvider {

    private static final String ROOT_PATH = "/person";

    private final RouterDefinition routerDefinition;

    protected PersonRouterProvider(RequestFailureHandler requestFailureHandler, RequestHandlerProxy requestHandlerProxy, Vertx vertx) {
        super(requestFailureHandler, requestHandlerProxy, vertx);
        routerDefinition = new RouterDefinition(this.getRouter(), ROOT_PATH);
    }

    @Override
    public RouterDefinition getRouterDefinition() {
        return this.routerDefinition;
    }
}
