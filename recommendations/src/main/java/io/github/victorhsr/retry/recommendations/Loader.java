package io.github.victorhsr.retry.recommendations;

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

        RetryContextLoader.loadContext(BASE_PACKAGE)
                .subscribe(() -> {
                    final ApplicationHttpServer applicationHttpServer = RetryContextLoader.getBean(ApplicationHttpServer.class);
                    applicationHttpServer.start()
                            .subscribe(verticleId -> LOGGER.info("Recommendations service iniciado, verticle-id: '{}'", verticleId)
                                    , Throwable::printStackTrace);

                }, Throwable::printStackTrace);
    }

}
