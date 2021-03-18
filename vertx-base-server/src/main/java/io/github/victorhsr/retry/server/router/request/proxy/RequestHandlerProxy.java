package io.github.victorhsr.retry.server.router.request.proxy;

import io.github.victorhsr.retry.commons.validation.SelfValidatingObject;
import io.github.victorhsr.retry.server.router.request.proxy.emptybody.EmptyBodyException;
import io.github.victorhsr.retry.server.router.request.proxy.structure.BadlyFormattedBodyException;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * <p>
 * Proxy responsavel por interceptar a requisicao
 * vinda do router, disparar validacoes caso necessario
 * e por fim, encaminhar o tratamento da requisicao
 * </p>
 *
 * <p>
 * Note que este proxy eh pensado para funcionar apenas se
 * tivermos um corpo de requisicao em formato JSON.
 * Para engatilhar a validacao desse payload fornecido, garanta
 * que sua classe alvo extenda {@link SelfValidatingObject}
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class RequestHandlerProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandlerProxy.class);

    /**
     * @param handler           handler final que ira tratar a requisicao
     * @param requestBodyClass  classe que o corpo da requisicao sera mapeado
     * @param <RequestBodyType> generico que representa o tipo de payload esperado pelo handler
     * @return um proxy que ira parsear o corpo da requisicao para o tipo esperado e engatilhar as validacoes caso necessario (caso trate-se de uma instancia de {@link SelfValidatingObject}
     */
    public <RequestBodyType> Handler<RoutingContext> doProxy(final BiConsumer<RoutingContext, RequestBodyType> handler, final Class<RequestBodyType> requestBodyClass) {

        return routingContext -> {

            /**
             * Primeiramnte, esse proxy eh pensado para funcionar apenas
             * se tivermos um corpo de requisicao em formato JSON, entao
             * iremos garantir inicialmente que essse corpo exista
             */
            final Optional<JsonObject> bodyAsJsonOpt = Optional.ofNullable(routingContext.getBodyAsJson());
            if (!bodyAsJsonOpt.isPresent()) {
                routingContext.fail(new EmptyBodyException());
                return;
            }

            RequestBodyType requestBody;

            /**
             * Segundo passo eh garantir que o corpo passado seja
             * um JSON, e que eles esteja atendendo a estrutura de
             * dados forncida em requestBodyClass
             */
            try {
                requestBody = routingContext.getBodyAsJson().mapTo(requestBodyClass);
            } catch (final IllegalArgumentException ex) {
                routingContext.fail(new BadlyFormattedBodyException(ex));
                return;
            }


            /**
             * Por fim executamos a validacao do objeto, caso o mesmo
             * extenda de {@link SelfValidatingObject}
             */
            try {

                if (requestBody instanceof SelfValidatingObject) {
                    ((SelfValidatingObject) requestBody).validateSelf();
                }

                handler.accept(routingContext, requestBody);

            } catch (final ValidationException ex) {
                final Throwable throwableToHandle = Objects.isNull(ex.getCause()) ? ex : ex.getCause();
                LOGGER.warn("Um erro de validacao ocorreu enquanto a requisicao estava sendo processada, exception: {}", ex.toString());
                routingContext.fail(throwableToHandle);
            } catch (final Exception ex) {
                LOGGER.warn("Uma excessao inesperada ocorreu, exception: {}", ex.toString());
                routingContext.fail(ex);
            }
        };
    }

}
