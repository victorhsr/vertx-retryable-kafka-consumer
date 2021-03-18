package io.github.victorhsr.retry.person.core.person.usecase.registerPerson;

import io.github.victorhsr.retry.person.core.person.usecase.registerPerson.port.PublishPersonRegisteredEventPort;
import io.github.victorhsr.retry.person.event.Person;
import io.github.victorhsr.retry.person.event.PersonRegisteredEvent;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class DefaultRegisterPersonUC implements RegisterPersonUC {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRegisterPersonUC.class);

    private final PublishPersonRegisteredEventPort publishPersonRegisteredEventPort;

    public DefaultRegisterPersonUC(PublishPersonRegisteredEventPort publishPersonRegisteredEventPort) {
        this.publishPersonRegisteredEventPort = publishPersonRegisteredEventPort;
    }

    @Override
    public Single<Person> handleRegister(final RegisterPersonCommand command) {
        return Single.create(emitter -> {

            LOGGER.info("Tratando comando: {}", command);
            final Person personToSave = command.getPayload();
            this.generateId(personToSave);

            final PersonRegisteredEvent personCreatedEvent = this.createEvent(command, personToSave);
            LOGGER.info("Pessoa criada, publicando evento: '{}'...", personCreatedEvent.getId());
            this.publishPersonRegisteredEventPort.publishPersonCreatedEvent(personCreatedEvent)
                    .subscribeOn(Schedulers.io())
                    .subscribe(() -> {
                        LOGGER.info("Evento '{}' publicado com sucesso!", personCreatedEvent.getId());
                        emitter.onSuccess(personToSave);
                    }, error -> {
                        LOGGER.error("Falha na publicacao do evento '{}'...", personCreatedEvent.getId());
                        emitter.onError(error);
                    });
        });
    }

    private PersonRegisteredEvent createEvent(final RegisterPersonCommand command, final Person person) {
        final PersonRegisteredEvent event = PersonRegisteredEvent.builder()
                .id(person.getId())
                .issuedAt(command.getIssuedAt())
                .answeredAt(LocalDateTime.now())
                .payload(person)
                .build();

        return event;
    }

    private void generateId(final Person person) {
        person.setId(UUID.randomUUID().toString());
    }
}
