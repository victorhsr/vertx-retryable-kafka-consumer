package io.github.victorhsr.retry.person;

import io.github.victorhsr.retry.server.ApplicationHttpServer;
import io.github.victorhsr.retry.server.context.RetryContextLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);
    private static final String BASE_PACKAGE = "io.github.victorhsr.retry";

    public static void main(String[] args) {
        LOGGER.info("Iniciando scan dos beans gerenciados...");
        RetryContextLoader.loadContext(BASE_PACKAGE)
                .subscribe(() -> {

                    LOGGER.info("Scan finalizado, iniciando servidor http...");
                    final ApplicationHttpServer applicationHttpServer = RetryContextLoader.getBean(ApplicationHttpServer.class);
                    applicationHttpServer.start()
                            .subscribe(verticleId -> LOGGER.info("Person-service iniciado, verticle-id: '{}'", verticleId)
                                    , Throwable::printStackTrace);
                }, Throwable::printStackTrace);
    }

}
