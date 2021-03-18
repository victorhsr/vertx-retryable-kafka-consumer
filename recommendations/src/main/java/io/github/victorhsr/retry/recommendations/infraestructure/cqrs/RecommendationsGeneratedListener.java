package io.github.victorhsr.retry.recommendations.infraestructure.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.victorhsr.retry.kafka.consumer.retry.RetryableConsumerDefinition;
import io.github.victorhsr.retry.kafka.consumer.retry.RetryableKafkaConsumer;
import io.github.victorhsr.retry.kafka.consumer.retry.delay.MessageProcessingDelayResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.name.KafkaRetryNameResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.proxy.KafkaListenerRetryProxyProcessor;
import io.github.victorhsr.retry.recommendations.event.Recommendations;
import io.github.victorhsr.retry.recommendations.event.RecommendationsGeneratedEvent;
import io.github.victorhsr.retry.recommendations.event.definitions.RecommendationsTopicType;
import io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.storer.RecommendationsStorer;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class RecommendationsGeneratedListener extends RetryableKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationsGeneratedListener.class);

    private final String bootstrapServers;
    private final String recommendationsGeneratedConsumerGroup;

    private final RecommendationsStorer recommendationsStorer;
    private final ObjectMapper objectMapper;

    public RecommendationsGeneratedListener(KafkaRetryNameResolver kafkaRetryNameResolver,
                                            KafkaListenerRetryProxyProcessor kafkaListenerRetryProxyProcessor,
                                            MessageProcessingDelayResolver messageProcessingDelayResolver,
                                            @Value("${kafka.bootstrapservers}") String bootstrapServers,
                                            @Value("${recommendations-generated.topic.max-attempts}") int maxAttempts,
                                            @Value("${recommendations-generated.consumer.group}") String recommendationsGeneratedConsumerGroup, RecommendationsStorer recommendationsStorer) {

        super(kafkaRetryNameResolver, new RetryableConsumerDefinition(recommendationsGeneratedConsumerGroup, RecommendationsTopicType.RECOMMENDATIONS_GENERATED_TOPIC.getType(), maxAttempts), kafkaListenerRetryProxyProcessor, messageProcessingDelayResolver);

        this.bootstrapServers = bootstrapServers;
        this.recommendationsGeneratedConsumerGroup = recommendationsGeneratedConsumerGroup;
        this.recommendationsStorer = recommendationsStorer;
        objectMapper = new ObjectMapper();

        this.init().subscribe((ks) -> {
        }, Throwable::printStackTrace, () -> {
            LOGGER.info("{} iniciado com sucesso", RecommendationsGeneratedListener.class.getName());
        });
    }

    @Override
    protected Properties getConsumerProperties() {

        final Properties kafkaProperties = new Properties();
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, this.recommendationsGeneratedConsumerGroup);
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return kafkaProperties;
    }

    @Override
    @SneakyThrows
    protected void process(final String eventId, final JsonNode messageJson) {

        final RecommendationsGeneratedEvent recommendationsGeneratedEvent = this.objectMapper.treeToValue(messageJson, RecommendationsGeneratedEvent.class);
        final Recommendations recommendationsToPersist = recommendationsGeneratedEvent.getPayload();

        this.recommendationsStorer.persist(recommendationsToPersist).blockingAwait();
        LOGGER.info("Evento '{}', processado", eventId);
    }
}
