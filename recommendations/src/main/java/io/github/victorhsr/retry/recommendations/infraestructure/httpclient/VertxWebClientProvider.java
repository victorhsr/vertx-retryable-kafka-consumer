package io.github.victorhsr.retry.recommendations.infraestructure.httpclient;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
public class VertxWebClientProvider {

    @Bean
    public WebClient getClient(final Vertx vertx) {
        final WebClient webClient = WebClient.create(vertx);
        return webClient;
    }
}
