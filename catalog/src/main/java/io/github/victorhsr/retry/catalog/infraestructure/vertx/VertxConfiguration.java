package io.github.victorhsr.retry.catalog.infraestructure.vertx;

import io.github.victorhsr.retry.server.VertxProvider;
import io.vertx.core.Vertx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
public class VertxConfiguration {

    @Bean
    public Vertx getVertx(){
        return VertxProvider.getVertxInstance();
    }

}
