package io.github.victorhsr.retry.jdbc.operation.transaction;

import io.github.victorhsr.retry.jdbc.DatabaseConnectionException;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Holder de uma transacao do sistema
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/

@Getter
@AllArgsConstructor
public class TransactionHolder {

    private final SqlConnection connection;
    private final Transaction transaction;

    /**
     * Efetua o commit da transacao e libera os
     * recursos
     */
    public Completable commit() {

        return Completable.create(emitter -> {

            this.transaction.commit(commitAsyncResult -> {

                this.connection.close();

                if (commitAsyncResult.failed()) {
                    emitter.onError(new DatabaseConnectionException(commitAsyncResult.cause()));
                    return;
                }

                emitter.onComplete();
            });
        });
    }

}
