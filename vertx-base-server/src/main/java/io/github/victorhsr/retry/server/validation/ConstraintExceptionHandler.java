package io.github.victorhsr.retry.server.validation;

import io.github.victorhsr.retry.server.router.request.exception.ExceptionHandler;
import io.github.victorhsr.retry.server.router.request.exception.RequestFailureHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler que trata da formatacao
 * de resposta para {@link ConstraintViolationException}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class ConstraintExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintExceptionHandler.class);

    public ConstraintExceptionHandler(RequestFailureHandler requestFailureHandler) {
        requestFailureHandler.registerHandler(this);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handle(final RoutingContext routingContext) {
        final ConstraintViolationException exception = (ConstraintViolationException) routingContext.failure();

        LOGGER.info("Tratando resposta para quebra de constraints: '{}'", exception.toString());
        final List<String> errors = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        LOGGER.info("Erros mapeados: '{}'", errors.toString());

        routingContext.response()
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                .end(new JsonArray(errors).encode());
    }

}
