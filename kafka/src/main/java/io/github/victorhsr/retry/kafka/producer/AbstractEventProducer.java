package io.github.victorhsr.retry.kafka.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.victorhsr.retry.AbstractDomainEvent;
import io.reactivex.rxjava3.core.Single;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Classe base de onde podem partir implementacoes de
 * criadores/publicadores de eventos
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public abstract class AbstractEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventProducer.class);

    private final KafkaProducer<String, JsonNode> kafkaProducer;
    private final ObjectMapper objectMapper;

    protected AbstractEventProducer(KafkaProducer<String, JsonNode> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        objectMapper = new ObjectMapper();
    }

    /**
     * resolve o id da mensagem a ser publicada, sobrescreva
     * esse metodo para aplicar suas proprias regras
     *
     * @param domainEvent evento a ser publicado
     * @param topicName   topico a qual o evento sera publicado
     */
    protected Single<String> resolveMessageId(final AbstractDomainEvent<?> domainEvent, final String topicName) {
        return Single.just(domainEvent.getId());
    }

    public Single<AbstractDomainEvent> publish(final AbstractDomainEvent<?> domainEvent, final String topicName) {
        return Single.create(emitter -> {

            LOGGER.info("Iniciando preparativos para publicacao do envento: {}", domainEvent);

            this.resolveMessageId(domainEvent, topicName)
                    .subscribe((recordId) -> {
                        final JsonNode kafkaPayload = this.objectMapper.valueToTree(domainEvent);
                        final ProducerRecord<String, JsonNode> record = new ProducerRecord<>(topicName, domainEvent.getId(), kafkaPayload);

                        LOGGER.info("Publicando evento no topico {}", topicName);
                        this.kafkaProducer.send(record, (recordMetadata, ex) -> {
                            if (Objects.nonNull(ex)) {
                                LOGGER.error("Falha na publicacao do evento: '{}'", ex.getMessage());
                                emitter.onError(ex);
                                return;
                            }
                            LOGGER.info("Evento publicado com sucesso {}", recordMetadata.toString());

                            emitter.onSuccess(domainEvent);
                        });
                    }, emitter::onError);
        });
    }
}
