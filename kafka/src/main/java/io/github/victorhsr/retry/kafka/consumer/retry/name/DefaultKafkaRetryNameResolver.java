package io.github.victorhsr.retry.kafka.consumer.retry.name;

/**
 * Implementacao padrao de {@link KafkaRetryNameResolver}
 *
 * <p>
 * As seguintes regras sao seguidas:
 *     <ul>
 *         <li>Serao utilizados os nomes que identificam os consumer, para que cada falha de processamento possa
 *         ser tratada de forma isolada</li>
 *          <li>Os nomes fornecidos serao modificados, com acrescimo do sufixo {@link DefaultKafkaRetryNameResolver#RETRY_SUFIX},
 *              acrestido do valor da tentativa;</li>
 *         <li>Caso a tentativa atual seja = 0, nao haverá modificacao no nome fornecido (em casos de retry)</li>
 *         <li>Para resolver o nome para DLQ, sera utilizado o sufixo {@link DefaultKafkaRetryNameResolver#DLQ_SUFIX};</li>
 *     </ul>
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class DefaultKafkaRetryNameResolver implements KafkaRetryNameResolver {

    String RETRY_SUFIX = "RETRY";
    String DLQ_SUFIX = "DLQ";

    @Override
    public String resolveRetryTopicName(final String topicName, final String consumerGroup, final int currentAttempt) {
        if (currentAttempt == 0)
            return topicName;

        return String.format("%s-%s-%s-%d", consumerGroup, topicName, RETRY_SUFIX, currentAttempt);
    }

    @Override
    public String resolveDlqTopicName(final String topicName, final String consumerGroup) {
        return String.format("%s-%s-%s", consumerGroup, topicName, DLQ_SUFIX);
    }

    @Override
    public String resolveConsumerGroup(final String consumerGroup, final int currentAttempt) {
        if (currentAttempt == 0)
            return consumerGroup;

        return String.format("%s-%s-%d", consumerGroup, RETRY_SUFIX, currentAttempt);
    }

}
