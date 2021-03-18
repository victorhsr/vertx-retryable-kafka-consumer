package io.github.victorhsr.retry.kafka.consumer.retry;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Wrapper com as definicoes de processamento e retry
 * de um consumer
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@AllArgsConstructor
public class RetryableConsumerDefinition {

    private final String consumerGroup;
    private final String topicName;
    private final Integer maxAttempts;

}
