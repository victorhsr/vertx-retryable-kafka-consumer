package io.github.victorhsr.retry.kafka.consumer.retry;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.victorhsr.retry.kafka.consumer.retry.delay.MessageProcessingDelayResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.name.KafkaRetryNameResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.proxy.KafkaListenerRetryProxyProcessor;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * <p>
 * Abstracao base de onde implementacoes de retryable consumers
 * podem partir. A fim de conveniencia, estaremos lidando com as mensagens
 * serializadas como {@link JsonNode}.
 * </p>
 *
 * <p>
 * Inicia-se o poll de mensagens dos topicos alvos porem ao mesmo tempo,
 * eh criada uma estrutura para tratamento de falhas, onde ao se falhar
 * o processamento de uma mensagem, a mesma eh republicada em um novo
 * topico para que seu processamento seja retomado a posterior. Cada consumer
 * group tera seus proprios topicos de retentativas (RETRY) e ao se alcancar
 * o maximo de tentativas estabelecido, as mensagens serao publicadas em um
 * topico de mensagens mortas (DLQ). Ou seja, varios topicos de RETRY e DLQ,
 * bem como seus respectivos consumers sao criados under the hoods
 * </p>
 *
 * <p>
 * Verifique a documentacao de {@link KafkaRetryNameResolver} para entender
 * como funciona a padronizacao de nomes para os topicos e consumer group.
 * Alem de {@link KafkaListenerRetryProxyProcessor} para entender melhor
 * a politica de redirecionamento de mensagens. E por fim, {@link MessageProcessingDelayResolver}
 * para implementar sua propria politica de delay entre reprocessamentos de mensagens
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public abstract class RetryableKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryableKafkaConsumer.class);

    private final KafkaRetryNameResolver kafkaRetryNameResolver;
    private final RetryableConsumerDefinition retryableConsumerDefinition;
    private final KafkaListenerRetryProxyProcessor kafkaListenerRetryProxyProcessor;
    private final MessageProcessingDelayResolver messageProcessingDelayResolver;

    public RetryableKafkaConsumer(KafkaRetryNameResolver kafkaRetryNameResolver,
                                  RetryableConsumerDefinition retryableConsumerDefinition,
                                  KafkaListenerRetryProxyProcessor kafkaListenerRetryProxyProcessor,
                                  MessageProcessingDelayResolver messageProcessingDelayResolver) {

        this.kafkaListenerRetryProxyProcessor = kafkaListenerRetryProxyProcessor;
        this.retryableConsumerDefinition = retryableConsumerDefinition;
        this.messageProcessingDelayResolver = messageProcessingDelayResolver;

        if (this.retryableConsumerDefinition.getMaxAttempts() < 0)
            throw new IllegalArgumentException("O maximo de tentativas deve ser maior ou igual a 0");

        this.kafkaRetryNameResolver = kafkaRetryNameResolver;
    }

    /**
     * Recupera as configuracoes kafka que o consumer
     * devera seguir
     */
    protected abstract Properties getConsumerProperties();

    /**
     * Recupera as definicoes de propriedades para o consumer, alterando a propriedade
     * {@link ConsumerConfig#GROUP_ID_CONFIG} para corresponder com o valor do tentativa
     * a ser escutada
     *
     * @param currentAttempt tentativa de consumo da mensagem
     */
    private Properties getConsumerPropertiesExpecificApplicationIdAttempt(final int currentAttempt) {
        final Properties newProperties = (Properties) this.getConsumerProperties().clone();
        final String newConsumerGroup = this.kafkaRetryNameResolver.resolveConsumerGroup(this.retryableConsumerDefinition.getConsumerGroup(), currentAttempt);

        newProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, newConsumerGroup);
        return newProperties;
    }

    /**
     * Metodo chamado para o processamento de uma mensagem
     * publicada no broker
     *
     * @param key   chave da mensagem
     * @param value payload da mensagem
     */
    protected abstract void process(final String key, final JsonNode value);

    /**
     * Cria uma instancia de {@link KafkaConsumer} configurado
     * para atender as mensagens especificas da tentativa
     * de processamento fornecida
     *
     * @param currentAttempt tentativa de processamento a ser aplicada
     */
    private KafkaConsumerDetails createKafkaConsumer(final int currentAttempt) {

        final String topicToListen = this.kafkaRetryNameResolver.resolveRetryTopicName(this.retryableConsumerDefinition.getTopicName(), this.retryableConsumerDefinition.getConsumerGroup(), currentAttempt);
        LOGGER.info("Iniciando listener para o topico {}", topicToListen);

        final Properties currentConsumerProperties = this.getConsumerPropertiesExpecificApplicationIdAttempt(currentAttempt);
        final KafkaConsumer<String, JsonNode> kafkaConsumer = new KafkaConsumer<>(currentConsumerProperties);
        kafkaConsumer.subscribe(Arrays.asList(topicToListen));

        return new KafkaConsumerDetails(kafkaConsumer, currentAttempt);
    }

    /**
     * Organiza a estrutura de execucao do poll de mensagens e processamento
     * de um {@link KafkaConsumer}. Como se eh esperado, cada {@link Runnable}
     * devera ser executado em sua propria {@link Thread}
     *
     * @param kafkaConsumerDetails detalhes sobre o consumer a ser utilizado
     */
    private Runnable createConsummingTask(final KafkaConsumerDetails kafkaConsumerDetails) {
        return () -> {

            final BiConsumer<String, JsonNode> retryableProcessor = this.kafkaListenerRetryProxyProcessor.prepareAndProcess(this.retryableConsumerDefinition, this::process, kafkaConsumerDetails.getAttempt());

            while (true) {
                kafkaConsumerDetails.getKafkaConsumer().poll(Duration.ofMillis(Long.MAX_VALUE))
                        .forEach(record -> {
                            final Long timeToWait = this.messageProcessingDelayResolver.resolveProcessingDelay(record.timestamp(), kafkaConsumerDetails.getAttempt());
                            LOGGER.info("Aplicando delay de '{}' milisegundo(s) ao evento '{}', na tentativa '{}'", timeToWait, record.key(), kafkaConsumerDetails.getAttempt());

                            try {
                                Thread.sleep(timeToWait);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            retryableProcessor.accept(record.key(), record.value());
                        });
            }
        };
    }

    private Observable<KafkaConsumerDetails> doInitConsumers() {
        LOGGER.info("Criando listener(s) para o topico {}, numero de tentativas: {}", this.retryableConsumerDefinition.getTopicName(), this.retryableConsumerDefinition.getMaxAttempts());
        return Observable.range(0, this.retryableConsumerDefinition.getMaxAttempts() + 1)
                .map(this::createKafkaConsumer);
    }

    /**
     * Inicia os consumers necessarios para tratar o fluxo
     * desejado para determinado evento
     */
    protected Observable<KafkaConsumerDetails> init() {

        return this.doInitConsumers()
                .doOnError(Throwable::printStackTrace)
                .doOnNext(details -> this.addShutdownHook(details.getKafkaConsumer()))
                .flatMapSingle(consumerDetails -> Single.just(consumerDetails)
                        .doOnSuccess(details -> this.createConsummingTask(details).run())
                        .subscribeOn(Schedulers.newThread())
                );
    }

    /**
     * Registra o hook de shutdown, para fechar a o consumer criado
     * em caso de SIGTERM ou SIGINT
     */
    private void addShutdownHook(final KafkaConsumer<String, JsonNode> consumer) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> consumer.close()));
    }

    /**
     * Estrutura de dados com detalhes sobre
     * um retryable consumer criado
     */
    @Getter
    @AllArgsConstructor
    protected final class KafkaConsumerDetails {
        private final KafkaConsumer<String, JsonNode> kafkaConsumer;
        private final int attempt;
    }

}
