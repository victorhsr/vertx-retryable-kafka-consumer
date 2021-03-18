package io.github.victorhsr.retry.person.event;

import io.github.victorhsr.retry.AbstractDomainEvent;
import io.github.victorhsr.retry.person.event.definitions.PersonEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Evento lancado ao se registrar uma nova {@link Person}
 * no sistema
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@AllArgsConstructor
public class PersonRegisteredEvent extends AbstractDomainEvent<Person> {

    @Builder
    public PersonRegisteredEvent(String id, LocalDateTime issuedAt, LocalDateTime answeredAt, Person payload) {
        super(id, issuedAt, answeredAt, payload, PersonEventType.PERSON_REGISTERED.getType());
    }
}
