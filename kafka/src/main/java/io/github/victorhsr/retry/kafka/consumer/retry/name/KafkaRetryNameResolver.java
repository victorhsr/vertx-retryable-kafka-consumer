package io.github.victorhsr.retry.kafka.consumer.retry.name;

/**
 * Contrato que define como se resolver nomes de forma
 * padronizada, no que diz respeito a retentativas de
 * consumo de mensagens
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface KafkaRetryNameResolver {

    /**
     * Resolve o nome que o topico, devera possuir
     * de acordo com a tentativa de consumo fornecida
     *
     * @param topicName      nome do topico que sera resolvido
     * @param consumerGroup  grupo ao qual o topico se refere
     * @param currentAttempt numero da tentativa de consumo
     */
    String resolveRetryTopicName(final String topicName, final String consumerGroup, final int currentAttempt);

    /**
     * Resolve o nome do topico DLQ para um dado consumer
     *
     * @param topicName     nome do topico que sera resolvido
     * @param consumerGroup grupo ao qual o consumidor que falhou no processamento da mensagem pertence
     */
    String resolveDlqTopicName(final String topicName, final String consumerGroup);

    /**
     * Resolve o consumer group que os consumers dos topicos de
     * retentativas deverao adotar
     *
     * @param consumerGroup  nome que represente o consumer do topico principal de onde se originou a mensagem
     * @param currentAttempt numero da tentativa atual de processamento
     */
    String resolveConsumerGroup(final String consumerGroup, final int currentAttempt);

}
