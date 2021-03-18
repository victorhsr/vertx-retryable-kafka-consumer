package io.github.victorhsr.retry.server.router;

import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.BiConsumer;

/**
 * Wrapper com as definicoes de manipulacao
 * do corpo de uma requisicao
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@AllArgsConstructor
public class ControllerBodyHandlerDefinition<PayloadType> {

    private final BiConsumer<RoutingContext, PayloadType> handler;
    private final Class<PayloadType> bodyType;

}
