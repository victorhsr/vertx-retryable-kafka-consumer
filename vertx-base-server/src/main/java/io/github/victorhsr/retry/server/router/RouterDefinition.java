package io.github.victorhsr.retry.server.router;

import io.vertx.ext.web.Router;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Estrutura de dados que contem definicoes de roteamento
 * de um aggregate
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/

@Getter
@AllArgsConstructor
public class RouterDefinition {

    private final Router router;
    private final String rootPath;

}
