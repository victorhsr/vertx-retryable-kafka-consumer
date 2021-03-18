package io.github.victorhsr.retry.server.router.request.exception;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Classe base para o tratamento de falhas
 * nas requisicoes para determinados recursos
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class RequestFailureHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFailureHandler.class);

    private final Map<Class<? extends Throwable>, Consumer<RoutingContext>> throwableHandlerMap;
    private final DefaultExceptionHandler defaultExceptionHandler;

    public RequestFailureHandler(DefaultExceptionHandler defaultExceptionHandler) {
        this.defaultExceptionHandler = defaultExceptionHandler;
        this.throwableHandlerMap = new HashMap<>();
    }

    @Override
    public void handle(final RoutingContext routingContext) {
        final Throwable applicationThrowable = routingContext.failure();

        LOGGER.info("Tratando throwable '{}'", applicationThrowable.toString());

        final Consumer<RoutingContext> throwableHandler = this.throwableHandlerMap.get(applicationThrowable.getClass());
        if (Objects.isNull(throwableHandler)) {
            LOGGER.warn("Throwable nao mapeado: '{}' com a mensagem: '{}', realizando tratamento padrao...", applicationThrowable.getClass().getSimpleName(), applicationThrowable.getMessage());
            this.defaultExceptionHandler.handle(routingContext);
        }

        throwableHandler.accept(routingContext);
    }

    /**
     * Realiza o registro de classes que possuam metodos anotados com {@link ExceptionHandler}
     * para tratamento de excessoes
     *
     * @param handlerWrapper objeto que contem os metodos de tratamento
     */
    public void registerHandler(final Object handlerWrapper) {

        Objects.requireNonNull(handlerWrapper);

        Stream.of(handlerWrapper.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(ExceptionHandler.class))
                .filter(method -> method.getAnnotation(ExceptionHandler.class).value().length > 0)
                .map((method) -> {

                    final Consumer<RoutingContext> consumerHandler = routingContext -> {
                        try {
                            method.invoke(handlerWrapper, routingContext);
                        } catch (final Exception ex) {
                            LOGGER.error("Falha ao invocar o tratamento para a excessao {}, invocando tratamento padrao...", ex);
                            this.defaultExceptionHandler.handle(routingContext);
                        }
                    };

                    return Stream.of(method.getAnnotation(ExceptionHandler.class).value())
                            .map(exceptionClass -> new ExceptionToHandlerWrapper(exceptionClass, consumerHandler));
                })
                .flatMap(Stream::distinct)
                .forEach(exceptionToHandlerWrapper -> {

                    if (this.throwableHandlerMap.containsKey(exceptionToHandlerWrapper.getException())) {
                        LOGGER.error("Tentativa de registro de manipulador para tipo de throwable ja mapeado. Throwable: '{}'", exceptionToHandlerWrapper.getException());
                        throw new IllegalArgumentException(String.format("Throwable class already registered: '%s'", exceptionToHandlerWrapper.getException()));
                    }

                    LOGGER.info("Registrando handler para a excessao '{}'", exceptionToHandlerWrapper.getException());
                    this.throwableHandlerMap.put(exceptionToHandlerWrapper.getException(), exceptionToHandlerWrapper.getHandler());
                });
    }

    @Getter
    @AllArgsConstructor
    private class ExceptionToHandlerWrapper {

        private Class<? extends Throwable> exception;
        private Consumer<RoutingContext> handler;
    }

}
