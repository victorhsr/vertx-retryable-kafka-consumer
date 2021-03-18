package io.github.victorhsr.retry.person.core.person.usecase.registerPerson;

import io.github.victorhsr.retry.AbstractCommand;
import io.github.victorhsr.retry.person.event.Person;
import io.reactivex.rxjava3.core.Single;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Caso de uso onde eh possivel registrar uma nova
 * pessoa ({@link Person}) no sistema
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface RegisterPersonUC {

    Single<Person> handleRegister(final RegisterPersonCommand command);

    class RegisterPersonCommand extends AbstractCommand<Person> {

        @Builder
        RegisterPersonCommand(LocalDateTime issuedAt, Person payload) {
            super(issuedAt, payload);
        }
    }

}
