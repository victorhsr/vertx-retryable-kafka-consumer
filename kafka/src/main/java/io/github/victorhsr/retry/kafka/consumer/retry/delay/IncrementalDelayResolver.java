package io.github.victorhsr.retry.kafka.consumer.retry.delay;

import java.time.Instant;
import java.util.function.Function;

/**
 * <p>
 * Implementacao de {@link MessageProcessingDelayResolver} que
 * aplica uma espera incremental entre o processamento das mensagens,
 * isto eh, faz com que a espera varie proporcionalmente a tentativa de processamento
 * a ser aplicada.
 * </p>
 *
 * <p>
 * Caso a mensagem <i>M1</i> tenha um delay de 5 minutos porem
 * esta na terceira tentativa de processamento, o delay utilizado sera
 * de 15 minutos.
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class IncrementalDelayResolver implements MessageProcessingDelayResolver {

    private final long milliToWaitByAttempt;
    private final boolean countWhenProcessingStart;

    /**
     * @param milliToWaitByAttempt     quantidade de tempo de espera a ser aplicado por tentativa
     * @param countWhenProcessingStart marcador que indica se o calculo de tempo de espera a ser
     *                                 aplicado deve iniciar a partir do momento de inicio de processamento
     *                                 da mensagem ou a partir do timestamp da mesma
     */
    public IncrementalDelayResolver(long milliToWaitByAttempt, boolean countWhenProcessingStart) {
        this.milliToWaitByAttempt = milliToWaitByAttempt;
        this.countWhenProcessingStart = countWhenProcessingStart;
    }

    @Override
    public long resolveProcessingDelay(final long recordTimestamp, final int currentAttempt) {
        long timeToWait = 0;

        if (currentAttempt > 0) {
            final long currentAttemptMilliToWait = this.milliToWaitByAttempt * currentAttempt;

            if (this.countWhenProcessingStart) {
                timeToWait = currentAttemptMilliToWait;
            } else {
                final long timestampToProcess = recordTimestamp + currentAttemptMilliToWait;
                timeToWait = timestampToProcess - Instant.now().toEpochMilli();
            }

            timeToWait = timeToWait >= 0 ? timeToWait : 0;
        }

        return timeToWait;
    }
}
