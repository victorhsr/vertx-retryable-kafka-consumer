package io.github.victorhsr.retry.server.router.provider;

import io.github.victorhsr.retry.server.router.ControllerBodyHandlerDefinition;
import io.github.victorhsr.retry.server.router.ControllerIdentifier;
import io.github.victorhsr.retry.server.router.request.proxy.RequestHandlerProxy;
import io.github.victorhsr.retry.server.router.request.exception.RequestFailureHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe base para especializacoes de {@link RouterProvider}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public abstract class AbstractRouterProvider implements RouterProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRouterProvider.class);

    private final RequestFailureHandler requestFailureHandler;
    private final RequestHandlerProxy requestHandlerProxy;
    private final Router router;

    protected AbstractRouterProvider(RequestFailureHandler requestFailureHandler, RequestHandlerProxy requestHandlerProxy, Vertx vertx) {
        this.requestFailureHandler = requestFailureHandler;
        this.requestHandlerProxy = requestHandlerProxy;
        this.router = Router.router(vertx);
    }

    @Override
    public <PayloadType> void registerController(final ControllerIdentifier controllerIdentifier, final ControllerBodyHandlerDefinition controllerBodyHandlerDefinition) {

        LOGGER.info("Registrando controlador: {}", controllerIdentifier);
        this.router.route(controllerIdentifier.getHttpMethod(), controllerIdentifier.getPath())
                .handler(this.requestHandlerProxy.doProxy(controllerBodyHandlerDefinition.getHandler(), controllerBodyHandlerDefinition.getBodyType()))
                .failureHandler(this.requestFailureHandler);

    }

    @Override
    public void registerController(final ControllerIdentifier controllerIdentifier, final Handler<RoutingContext> consumer) {

        LOGGER.info("Registrando controlador: {}", controllerIdentifier);
        this.router.route(controllerIdentifier.getHttpMethod(), controllerIdentifier.getPath())
                .handler(consumer)
                .failureHandler(this.requestFailureHandler);
    }

    protected Router getRouter() {
        return this.router;
    }

}
