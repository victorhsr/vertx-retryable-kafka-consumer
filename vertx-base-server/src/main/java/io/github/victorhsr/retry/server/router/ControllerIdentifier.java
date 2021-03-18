package io.github.victorhsr.retry.server.router;

import io.vertx.core.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Classe wrapper com os dados de identificacao
 * de um controlador, ou seja, que define qual recurso
 * e acao http ele ira reagir
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@ToString
@AllArgsConstructor
public class ControllerIdentifier {

    private final HttpMethod httpMethod;
    private final String path;

}
