package io.github.victorhsr.retry.kafka.consumer.retry.delay;

/**
 * Interface que estabelece como se resolver o tempo
 * de espera no processamento de mensagens
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface MessageProcessingDelayResolver {

    /**
     * resolve o tempo de espera para se processar uma mensagem
     * baseado em retryable consumers
     *
     * @param recordTimestamp quando o registro ocorreu
     * @param currentAttempt  tentativa a qual o processamento ira ocorrer
     */
    long resolveProcessingDelay(final long recordTimestamp, final int currentAttempt);

}
