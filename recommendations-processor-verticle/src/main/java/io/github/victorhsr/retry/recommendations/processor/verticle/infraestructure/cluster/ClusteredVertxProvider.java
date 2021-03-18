package io.github.victorhsr.retry.recommendations.processor.verticle.infraestructure.cluster;

import io.github.victorhsr.retry.server.VertxProvider;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * <p>
 * Classe responsavel por prover uma instancia gerenciada
 * pelo spring-context do vertx, em modo clusterizado.
 * </p>
 *
 * <p>
 * TL;DR caso alguma funcionalidade seja considerada cara, em sua execucao,
 * poderemos dedicar uma nova instancia com seu verticle especifico para esta operacao,
 * de forma a distribuir a carga de trabalho entre varias maquinas. E eh para isto
 * que estamos dispondo essa instancia clusterizada. Ela sera responsavel por integrar
 * os nos do cluster bem como lidar com a comunicacao entre eles
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
public class ClusteredVertxProvider {

    @Bean
    @Primary
    public Vertx getClusteredVertx(@Value("#{'${vertx.cluster.members}'.split(',')}") final List<String> members) {
        return VertxProvider.getClusteredVertxInstance(members);
    }
}
