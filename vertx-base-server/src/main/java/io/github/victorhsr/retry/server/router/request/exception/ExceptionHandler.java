package io.github.victorhsr.retry.server.router.request.exception;

import io.vertx.ext.web.RoutingContext;

import java.lang.annotation.*;

/**
 * Anotacao que indica que um determinado metodo
 * ira tratar excessoes trafegadas por um router.
 * <br/>
 * Metodos anotados devem aceitar uma instancia de {@link RoutingContext}
 * como parametro
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionHandler {

    /**
     * Excessoes que serao tratadas por este
     * exception handler
     */
    Class<? extends Throwable>[] value();

}
