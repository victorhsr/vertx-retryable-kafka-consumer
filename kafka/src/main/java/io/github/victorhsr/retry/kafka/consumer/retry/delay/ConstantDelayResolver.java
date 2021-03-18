package io.github.victorhsr.retry.kafka.consumer.retry.delay;

import java.time.Instant;

/**
 * <p>
 * Implementacao de {@link MessageProcessingDelayResolver} que
 * aplica uma espera "constante" entre o processamento das mensagens,
 * isto eh, faz com que em todos os niveis de reprocessamento seja aplicada
 * a mesma quantidade de espera.
 * </p>
 *
 * <p>
 * Caso a mensagem <i>M1</i> tenha um delay de 5 minutos, porem,
 * ela foi recuperada para processamento 2 minutos apos sua publicacao,
 * o delay da mesma sera de 3 minutos. Isso implica dizer que mensagens
 * que foram recuperadas para processamento apos 5 minutos, serao executadas
 * imediatamente, sem espera.
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class ConstantDelayResolver implements MessageProcessingDelayResolver {

    private final long milliToWait;

    public ConstantDelayResolver(long milliToWait) {
        this.milliToWait = milliToWait;
    }

    @Override
    public long resolveProcessingDelay(final long recordTimestamp, final int currentAttempt) {

        long timeToWait = 0;

        if (currentAttempt > 0) {
            final long timestampToProcess = recordTimestamp + this.milliToWait;
            final long currentTimestamp = Instant.now().toEpochMilli();
            final long milliToWait = timestampToProcess - currentTimestamp;

            timeToWait = milliToWait >= 0 ? milliToWait : 0;
        }

        return timeToWait;
    }
}
