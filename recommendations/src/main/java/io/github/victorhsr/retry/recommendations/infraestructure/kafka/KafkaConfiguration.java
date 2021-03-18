package io.github.victorhsr.retry.recommendations.infraestructure.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.victorhsr.retry.kafka.consumer.retry.delay.IncrementalDelayResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.delay.MessageProcessingDelayResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.name.DefaultKafkaRetryNameResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.name.KafkaRetryNameResolver;
import io.github.victorhsr.retry.kafka.consumer.retry.proxy.KafkaListenerRetryProxyProcessor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
public class KafkaConfiguration {

    @Bean
    public KafkaRetryNameResolver getKafkaRetryNameResolver() {
        return new DefaultKafkaRetryNameResolver();
    }

    @Bean
    public KafkaListenerRetryProxyProcessor getKafkaListenerRetryProxyProcessor(final KafkaRetryNameResolver kafkaRetryNameResolver, final KafkaProducer<String, JsonNode> kafkaProducer) {
        return new KafkaListenerRetryProxyProcessor(kafkaRetryNameResolver, kafkaProducer);
    }

    @Bean
    public MessageProcessingDelayResolver getProcessingDelayResolver(@Value("${kafka.consumer.retry-delay}") final long retryDelay) {
        return new IncrementalDelayResolver(retryDelay, true);
//        return new ConstantDelayResolver(retryDelay);
    }

    @Bean
    public KafkaProducer<String, JsonNode> getKafkaProducer(@Value("${kafka.producer.client-id}") String clientId,
                                                            @Value("${kafka.bootstrapservers}") String bootstrapServers) {

        final Properties properties = new Properties();
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        final KafkaProducer<String, JsonNode> kafkaProducer = new KafkaProducer<>(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> kafkaProducer.close()));

        return kafkaProducer;
    }

}
