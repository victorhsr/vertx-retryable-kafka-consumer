package io.github.victorhsr.retry.recommendations.infraestructure.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.victorhsr.retry.kafka.producer.AbstractEventProducer;
import io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.port.PublishRecommendationsGeneratedEventPort;
import io.github.victorhsr.retry.recommendations.event.RecommendationsGeneratedEvent;
import io.github.victorhsr.retry.recommendations.event.definitions.RecommendationsTopicType;
import io.reactivex.rxjava3.core.Completable;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Component;

/**
 * output adapter. Implementacao, que faz uso do Apache Kafka
 * para publicacao de mensagens do aggregate
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Component
public class PublishRecommendationsGeneratedEventAdapter extends AbstractEventProducer implements PublishRecommendationsGeneratedEventPort {

    public PublishRecommendationsGeneratedEventAdapter(KafkaProducer<String, JsonNode> kafkaProducer) {
        super(kafkaProducer);
    }

    @Override
    public Completable publishRecommendationsGeneratedEvent(final RecommendationsGeneratedEvent recommendationsGeneratedEvent) {
        return Completable.create(emitter -> {
            this.publish(recommendationsGeneratedEvent, RecommendationsTopicType.RECOMMENDATIONS_GENERATED_TOPIC.getType())
                    .subscribe((evt) -> emitter.onComplete(), emitter::onError);
        });
    }
}
