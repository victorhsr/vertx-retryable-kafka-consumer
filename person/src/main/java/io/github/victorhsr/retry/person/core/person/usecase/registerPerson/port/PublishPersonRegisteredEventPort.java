package io.github.victorhsr.retry.person.core.person.usecase.registerPerson.port;

import io.github.victorhsr.retry.person.event.PersonRegisteredEvent;
import io.reactivex.rxjava3.core.Completable;

/**
 * output port, define como poderemos publicar o evento
 * de registro de pessoas {@link PersonRegisteredEvent}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface PublishPersonRegisteredEventPort {

    /**
     * Realiza a publicacao do evento de registro de pessoa,
     * o ambiente de publicacao fica a criterio do adapter
     * implementado
     *
     * @param personCreatedEvent evento lancado ao se cadastrar uma pessoa no sistema
     */
    Completable publishPersonCreatedEvent(final PersonRegisteredEvent personCreatedEvent);

}
