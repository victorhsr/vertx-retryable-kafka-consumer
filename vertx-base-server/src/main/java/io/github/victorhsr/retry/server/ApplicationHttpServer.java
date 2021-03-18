package io.github.victorhsr.retry.server;

import io.github.victorhsr.retry.server.router.RouterDefinition;
import io.github.victorhsr.retry.server.router.provider.RouterProvider;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Servidor que oferece acesso http às rotas mapeadas
 * no sistema via @{@link RouterProvider}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class ApplicationHttpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationHttpServer.class);

    private final int port;
    private final Vertx vertx;
    private final List<RouterProvider> routerProviders;

    public ApplicationHttpServer(@Value("${server.port}") final int port, final Vertx vertx, final List<RouterProvider> routerProviders) {
        this.port = port;
        this.vertx = vertx;
        this.routerProviders = routerProviders;
    }

    /**
     * Capitura todas as {@link RouterDefinition} do sistema,
     * os adicionando e as registra no router principal
     */
    private Router buildRouter() {

        final Router mainRouter = Router.router(this.vertx);
        mainRouter.route().handler(BodyHandler.create());

        final String healthCheckPath = "/health";
        LOGGER.info("Adicionando rota de health check ({})...", healthCheckPath);
        final HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);
        mainRouter.get(healthCheckPath).handler(healthCheckHandler);

        LOGGER.info("Mapeando rotas...");
        this.routerProviders.forEach(routerProvider -> {
            final RouterDefinition routerDefinition = routerProvider.getRouterDefinition();
            LOGGER.info("Mapeando rotas do dominio '{}'", routerDefinition.getRootPath());
            mainRouter.mountSubRouter(routerDefinition.getRootPath(), routerDefinition.getRouter());
        });

        return mainRouter;
    }

    public Single<String> start() {

        return Single.create(emitter -> {
            this.vertx.deployVerticle(new HttpServerVerticle(this.port), (idAsync) -> {
                if (idAsync.failed()) {
                    emitter.onError(idAsync.cause());
                }
                emitter.onSuccess(idAsync.result());
            });
        });
    }

    private final class HttpServerVerticle extends AbstractVerticle {

        private final int port;

        private HttpServerVerticle(int port) {
            this.port = port;
        }

        @Override
        public void start(Promise<Void> startPromise) {

            final Router mainRouter = buildRouter();

            final Handler<AsyncResult<HttpServer>> onStartHandler = server -> {
                LOGGER.info("Servidor iniciado, ouvindo porta {}", this.port);
                startPromise.complete();
            };

            vertx.createHttpServer()
                    .requestHandler(mainRouter)
                    .exceptionHandler(startPromise::fail)
                    .listen(this.port, onStartHandler);
        }

    }

}
