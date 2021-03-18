package io.github.victorhsr.retry.person.infraestructure.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.victorhsr.retry.kafka.producer.AbstractEventProducer;
import io.github.victorhsr.retry.person.core.person.usecase.registerPerson.port.PublishPersonRegisteredEventPort;
import io.github.victorhsr.retry.person.event.PersonRegisteredEvent;
import io.github.victorhsr.retry.person.event.definitions.PersonTopicType;
import io.reactivex.rxjava3.core.Completable;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Component;

/**
 * <p>
 * output adapter. Implementacao de {@link AbstractEventProducer}, que faz uso do Apache Kafka
 * para publicacao de mensagens do aggregate
 * </p>
 *
 * <p>
 * TL;DR estamos simplesmente publicando a mensagem no topico via kafka de forma assincrona, porem ainda aguardando o
 * resultado da publicacao para entao notificar nosso caller.
 * <p>
 *
 * <p>
 * Aqui poderiamos ainda, dependendo de nosso dominio, adotar varias estrategias diferentes, como fazer uso do outbox pattern,
 * persistindo a solicitacao de publicacao da mensagem num banco de dados e eventualmente, realizando o envio da mensagem para
 * o topico de fato.
 * <p>
 *
 * <p>
 * Este tipo de abordagem pode ser util caso queiramos garantir que uma quantidade significativa de replicas estejam sincronizadas (min.insync.replicas),
 * porem ao mesmo tempo, nao queremos segurar a conexao do ator ativa por muito tempo. Entao podemos somente registrar
 * diretamente em um banco de dados essa solicitacao e posteriormente, um worker iria de fato realizar a publicacao e aguardar o tempo
 * que fosse necessario para o registro ser concluido
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class PersonEventKafkaPublisherAdapter extends AbstractEventProducer implements PublishPersonRegisteredEventPort {

    public PersonEventKafkaPublisherAdapter(KafkaProducer<String, JsonNode> kafkaProducer) {
        super(kafkaProducer);
    }

    @Override
    public Completable publishPersonCreatedEvent(final PersonRegisteredEvent personCreatedEvent) {
        return Completable.create(emitter -> {
            this.publish(personCreatedEvent, PersonTopicType.PERSON_REGISTERED_TOPIC.getType())
                    .subscribe((evt) -> emitter.onComplete(), emitter::onError);
        });
    }

}
