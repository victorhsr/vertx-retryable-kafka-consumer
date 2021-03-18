package io.github.victorhsr.retry.jdbc.operation.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representacao de um resultado de uma transacao
 * em que nao foi realizado o commit
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/

@Getter
@AllArgsConstructor
public class NotCommitedResult<T> {

    private final TransactionHolder transactionHolder;
    private final T result;

}
