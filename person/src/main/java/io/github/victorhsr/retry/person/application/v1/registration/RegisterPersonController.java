package io.github.victorhsr.retry.person.application.v1.registration;

import io.github.victorhsr.retry.person.application.dto.PersonMapper;
import io.github.victorhsr.retry.person.core.person.usecase.registerPerson.RegisterPersonUC;
import io.github.victorhsr.retry.person.event.Person;
import io.github.victorhsr.retry.person.application.v1.registration.dto.RegisterPersonDTO;
import io.github.victorhsr.retry.server.router.ControllerBodyHandlerDefinition;
import io.github.victorhsr.retry.server.router.ControllerIdentifier;
import io.github.victorhsr.retry.server.router.provider.RouterProvider;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * input/driving adapter, para recepcao de requisicoes HTTP, correspondentes
 * ao caso de uso {@link RegisterPersonUC}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Controller
public class RegisterPersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterPersonController.class);

    private final RegisterPersonUC registerPersonUC;

    public RegisterPersonController(RouterProvider routerProvider, RegisterPersonUC registerPersonUC) {
        this.registerPersonUC = registerPersonUC;
        this.registerController(routerProvider);
    }

    private void registerController(final RouterProvider routerProvider) {
        final ControllerIdentifier controllerIdentifier = new ControllerIdentifier(HttpMethod.POST, "/v1");
        final ControllerBodyHandlerDefinition<RegisterPersonDTO> controllerBodyHandlerDefinition = new ControllerBodyHandlerDefinition<>(this::handleCreate, RegisterPersonDTO.class);

        routerProvider.registerController(controllerIdentifier, controllerBodyHandlerDefinition);
    }

    public void handleCreate(final RoutingContext routingContext, final RegisterPersonDTO registerPersonDTO) {

        LOGGER.info("Tratando requisicao...");

        final Person person = PersonMapper.INSTANCE.registerDTOToPerson(registerPersonDTO);

        final RegisterPersonUC.RegisterPersonCommand command = RegisterPersonUC.RegisterPersonCommand.builder()
                .issuedAt(LocalDateTime.now())
                .payload(person)
                .build();

        this.registerPersonUC.handleRegister(command)
                .subscribe(registeredPerson -> {
                    LOGGER.info("Pessoa registrada com sucesso, encerrando requisicao");
                    routingContext.response().setStatusCode(HttpResponseStatus.CREATED.code()).end(Json.encode(registeredPerson));
                }, routingContext::fail);
    }
}
