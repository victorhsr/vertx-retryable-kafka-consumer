package io.github.victorhsr.retry.server.router.provider;

import io.github.victorhsr.retry.server.router.ControllerBodyHandlerDefinition;
import io.github.victorhsr.retry.server.router.ControllerIdentifier;
import io.github.victorhsr.retry.server.router.RouterDefinition;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Definicoes sobre regras que um provedor de instancias de {@link RouterDefinition}
 * devera seguir
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface RouterProvider {

    /**
     * Recupera as definicoes de roteamento
     * que foram cadastradas via {@link RouterProvider#registerController(ControllerIdentifier, Handler)}
     * e {@link RouterProvider#registerController(ControllerIdentifier, ControllerBodyHandlerDefinition)}
     */
    RouterDefinition getRouterDefinition();

    /**
     * Registra um controlador que ira tratar tipos de requisicoes
     * em um recurso especifico
     *
     * @param controllerIdentifier            identificador do controlador
     * @param controllerBodyHandlerDefinition definicoes para tratamento de corpo da requisicao
     */
    <PayloadType> void registerController(final ControllerIdentifier controllerIdentifier, final ControllerBodyHandlerDefinition controllerBodyHandlerDefinition);

    /**
     * Registra um controlador que ira tratar tipos de requisicoes
     * em um recurso especifico
     *
     * @param controllerIdentifier identificador do controlador
     * @param requestHandler       consumer que ira processar a requisicao
     */
    void registerController(final ControllerIdentifier controllerIdentifier, final Handler<RoutingContext> requestHandler);

}
