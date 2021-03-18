package io.github.victorhsr.retry.recommendations.application.kafka.recommendations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.victorhsr.retry.kafka.consumer.retry.RetryableConsumerDefinition;
import io.github.victorhsr.retry.kafka.consumer.retry.RetryableKafkaConsumer;
import io.github.victorhsr.retry.kafka.consumer.retry.delay.MessageProcessingDelayResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.name.KafkaRetryNameResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.proxy.KafkaListenerRetryProxyProcessor;
import io.github.victorhsr.retry.person.event.Person;
import io.github.victorhsr.retry.person.event.PersonRegisteredEvent;
import io.github.victorhsr.retry.person.event.definitions.PersonEventType;
import io.github.victorhsr.retry.person.event.definitions.PersonTopicType;
import io.github.victorhsr.retry.recommendations.application.kafka.recommendations.dto.PersonMapper;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.GenerateRecommendationsUC;
import io.github.victorhsr.retry.recommendations.processor.dto.PersonData;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Properties;

/**
 * input/driving adapter, para interceptacao de eventos de {@link PersonEventType#PERSON_REGISTERED}
 * no topico {@link PersonTopicType#PERSON_REGISTERED_TOPIC}, que ao acontecer, disparam
 * o caso de uso {@link GenerateRecommendationsUC}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class PersonCreatedListener extends RetryableKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonCreatedListener.class);

    private final String bootstrapServers;
    private final String personCreatedConsumerGroup;
    private final ObjectMapper objectMapper;

    private final GenerateRecommendationsUC generateRecommendationsUC;

    public PersonCreatedListener(KafkaRetryNameResolver kafkaRetryNameResolver,
                                 KafkaListenerRetryProxyProcessor kafkaListenerRetryProxyProcessor,
                                 GenerateRecommendationsUC generateRecommendationsUC,
                                 @Value("${person-registered.topic.max-attempts}") int maxAttempts,
                                 @Value("${kafka.bootstrapservers}") String bootstrapServers,
                                 @Value("${person-registered.consumer.group}") String personCreatedConsumerGroup,
                                 MessageProcessingDelayResolver messageProcessingDelayResolver) {

        super(kafkaRetryNameResolver, new RetryableConsumerDefinition(personCreatedConsumerGroup, PersonTopicType.PERSON_REGISTERED_TOPIC.getType(), maxAttempts), kafkaListenerRetryProxyProcessor, messageProcessingDelayResolver);
        this.bootstrapServers = bootstrapServers;
        this.personCreatedConsumerGroup = personCreatedConsumerGroup;
        this.generateRecommendationsUC = generateRecommendationsUC;
        this.objectMapper = new ObjectMapper();

        this.init().subscribe((ks) -> {
        }, Throwable::printStackTrace, () -> {
            LOGGER.info("{} iniciado com sucesso", PersonRegisteredEvent.class.getName());
        });
    }

    @Override
    protected Properties getConsumerProperties() {

        final Properties kafkaProperties = new Properties();
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, this.personCreatedConsumerGroup);
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return kafkaProperties;
    }

    @Override
    @SneakyThrows
    protected void process(final String eventId, final JsonNode messageJson) {

        final PersonRegisteredEvent personCreatedEvent = this.objectMapper.treeToValue(messageJson, PersonRegisteredEvent.class);
        final Person personToAnalyze = personCreatedEvent.getPayload();

        final PersonData personData = PersonMapper.INSTANCE.personToPersonData(personToAnalyze);

        final GenerateRecommendationsUC.GenerateRecommendationsCommand command = GenerateRecommendationsUC.GenerateRecommendationsCommand.builder()
                .issuedAt(LocalDateTime.now())
                .payload(personData)
                .build();

        final Recommendations recommendations = this.generateRecommendationsUC.generateRecomendations(command).blockingGet();

        LOGGER.info("Evento '{}', processado, recomendacoes: {}", eventId, recommendations);
    }
}
