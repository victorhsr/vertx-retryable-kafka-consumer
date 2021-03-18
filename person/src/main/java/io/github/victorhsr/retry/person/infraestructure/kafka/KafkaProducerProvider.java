package io.github.victorhsr.retry.person.infraestructure.kafka;

import com.fasterxml.jackson.databind.JsonNode;
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
public class KafkaProducerProvider {

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
