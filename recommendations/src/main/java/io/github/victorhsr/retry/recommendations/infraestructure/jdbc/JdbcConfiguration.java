package io.github.victorhsr.retry.recommendations.infraestructure.jdbc;

import io.github.victorhsr.retry.jdbc.operation.DefaultReactiveJdbcOperations;
import io.github.victorhsr.retry.jdbc.operation.ReactiveJdbcOperations;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
public class JdbcConfiguration {

    @Bean
    public Pool getPostgresPool(final Vertx vertx,
                                @Value("${jdbc.datasource.port}") final int port,
                                @Value("${jdbc.datasource.host}") final String host,
                                @Value("${jdbc.datasource.dbname}") final String dbName,
                                @Value("${jdbc.datasource.username}") final String username,
                                @Value("${jdbc.datasource.password}") final String password) {

        final PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(port)
                .setHost(host)
                .setDatabase(dbName)
                .setUser(username)
                .setPassword(password);

        final PgPool client = PgPool.pool(vertx, connectOptions, new PoolOptions());

        return client;
    }

    @Bean
    public ReactiveJdbcOperations getReactiveJdbcOperations(final Pool pool) {
        return new DefaultReactiveJdbcOperations(pool);
    }

}
