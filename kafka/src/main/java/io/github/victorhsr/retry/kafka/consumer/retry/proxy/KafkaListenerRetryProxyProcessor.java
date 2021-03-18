package io.github.victorhsr.retry.kafka.consumer.retry.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.victorhsr.retry.kafka.consumer.retry.RetryableConsumerDefinition;
import io.github.victorhsr.retry.kafka.consumer.retry.name.KafkaRetryNameResolver;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

/**
 * <p>
 * Classe responsavel por preparar a funcao
 * de processamento de um evento, para trabalhar com
 * o mecanismo de retry.
 * </p>
 *
 * <p>
 * Cada evento a ser processado eh encapsulado num
 * metodo try/catch onde eh detectada a falha, caso ocorra, e entao o
 * evento eh republicado nas filas de retry ate que
 * as mesmas se esgotem e caia na DLQ (Dead Letter Queue)
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class KafkaListenerRetryProxyProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaListenerRetryProxyProcessor.class);

    private final KafkaRetryNameResolver kafkaRetryNameResolver;
    private final KafkaProducer<String, JsonNode> kafkaProducer;

    public KafkaListenerRetryProxyProcessor(KafkaRetryNameResolver kafkaRetryNameResolver, KafkaProducer<String, JsonNode> kafkaProducer) {
        this.kafkaRetryNameResolver = kafkaRetryNameResolver;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Cria um proxy para o processamento de um evento
     * recebido pelo kafka streams api. O proxy ira
     * empacotar o metodo de execucao do evento passado
     * em um bloco try/catch onde eh detectada a falha, caso ocorra, e entao
     * evento eh republicado nas filas de retry ate que
     * as mesmas se esgotem e caia na DLQ (Dead Letter Queue)
     *
     * @param retryableConsumerDefinition dados sobre o processamento a ser feito
     * @param processor                   processador do evento que sera empacotado
     * @param currentAttempt              tentativa atual de processamento do evento
     */
    public BiConsumer<String, JsonNode> prepareAndProcess(final RetryableConsumerDefinition retryableConsumerDefinition, final BiConsumer<String, JsonNode> processor, final int currentAttempt) {

        return (key, value) -> {

            try {
                LOGGER.info("Iniciando processamento do evento {}", key);
                processor.accept(key, value);
            } catch (final Exception ex) {

                LOGGER.error("Falha no processamento do evento {}, excessao: {}", key, ex.toString());

                String topicToRepublish;
                if (currentAttempt == retryableConsumerDefinition.getMaxAttempts()) {
                    LOGGER.warn("Evento alcancou o maximo de tentativas de processamento ({}), enviando para DLQ...", retryableConsumerDefinition.getMaxAttempts());
                    topicToRepublish = this.kafkaRetryNameResolver.resolveDlqTopicName(retryableConsumerDefinition.getTopicName(), retryableConsumerDefinition.getConsumerGroup());
                } else {
                    final int nextAttempt = currentAttempt + 1;
                    topicToRepublish = this.kafkaRetryNameResolver.resolveRetryTopicName(retryableConsumerDefinition.getTopicName(), retryableConsumerDefinition.getConsumerGroup(), nextAttempt);
                }
                this.republish(topicToRepublish, key, value);
            }
        };
    }

    @SneakyThrows
    private void republish(final String topic, final String key, final JsonNode value) {

        LOGGER.info("Republicando evento no topico: {}", topic);
        final ProducerRecord<String, JsonNode> record = new ProducerRecord<>(topic, key, value);
        this.kafkaProducer.send(record).get();
        LOGGER.info("Evento {} republicado com sucesso no topico {}", key, topic);
    }

}
