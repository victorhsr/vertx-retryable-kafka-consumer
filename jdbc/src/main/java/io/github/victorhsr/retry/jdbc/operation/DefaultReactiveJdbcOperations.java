package io.github.victorhsr.retry.jdbc.operation;

import io.github.victorhsr.retry.jdbc.DatabaseConnectionException;
import io.github.victorhsr.retry.jdbc.SqlCommand;
import io.github.victorhsr.retry.jdbc.operation.transaction.NotCommitedResult;
import io.github.victorhsr.retry.jdbc.operation.transaction.TransactionHolder;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.sqlclient.*;

import java.util.Optional;
import java.util.function.Function;

/**
 * Implementacao padrao de {@link ReactiveJdbcOperations}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class DefaultReactiveJdbcOperations implements ReactiveJdbcOperations {

    private final Pool connectionPool;

    public DefaultReactiveJdbcOperations(Pool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public <ResultType> Single<Optional<ResultType>> executeQuery(final SqlCommand sqlCommand, final Function<RowSet<Row>, ResultType> resultExtractor) {
        return Single.create(emitter -> {

            this.buildTransaction()
                    .subscribe(transactionHolder -> {
                        this.executeQuery(transactionHolder, sqlCommand, resultExtractor)
                                .subscribe(notCommitedResult -> {
                                    notCommitedResult.getTransactionHolder().commit()
                                            .subscribe(() -> {
                                                emitter.onSuccess(Optional.ofNullable(notCommitedResult.getResult()));
                                            }, emitter::onError);
                                }, emitter::onError);
                    }, emitter::onError);
        });
    }

    @Override
    public <ResultType> Single<NotCommitedResult<ResultType>> executeQueryOpenTransaction(final SqlCommand sqlCommand, final Function<RowSet<Row>, ResultType> resultExtractor) {
        return Single.create(emitter -> {

            this.buildTransaction()
                    .subscribe(transactionHolder -> {
                        this.executeQuery(transactionHolder, sqlCommand, resultExtractor)
                                .subscribe(emitter::onSuccess, emitter::onError);
                    }, emitter::onError);
        });
    }

    @Override
    public <ResultType> Single<NotCommitedResult<ResultType>> executeQuery(final TransactionHolder transactionHolder, final SqlCommand sqlCommand, final Function<RowSet<Row>, ResultType> resultExtractor) {
        return Single.create(emitter -> {

            transactionHolder.getConnection().preparedQuery(sqlCommand.getQuery()).execute(sqlCommand.getParams(), pendingQuery -> {

                if (pendingQuery.succeeded()) {
                    final RowSet<Row> resultRowSet = pendingQuery.result();
                    final ResultType result = resultExtractor.apply(resultRowSet);

                    final NotCommitedResult<ResultType> notCommitedResult = new NotCommitedResult(transactionHolder, result);

                    emitter.onSuccess(notCommitedResult);
                } else {
                    emitter.onError(new DatabaseConnectionException(pendingQuery.cause()));
                }
            });
        });
    }

    @Override
    public Completable execute(final SqlCommand sqlCommand) {
        return Completable.create(emitter -> {

            this.executeOpenTransaction(sqlCommand)
                    .subscribe(transactionHolder -> {
                        transactionHolder.commit()
                                .subscribe(emitter::onComplete, emitter::onError);
                    }, emitter::onError);
        });
    }

    @Override
    public Single<TransactionHolder> executeOpenTransaction(final SqlCommand sqlCommand) {
        return Single.create(emitter -> {

            this.buildTransaction()
                    .subscribe(transactionHolder -> {
                        this.execute(transactionHolder, sqlCommand)
                                .subscribe(emitter::onSuccess, emitter::onError);
                    }, emitter::onError);
        });
    }

    @Override
    public Single<TransactionHolder> execute(final TransactionHolder transactionHolder, final SqlCommand sqlCommand) {
        return Single.create(emitter -> {

            transactionHolder.getConnection().preparedQuery(sqlCommand.getQuery()).execute(sqlCommand.getParams(), pendingQuery -> {

                if (pendingQuery.succeeded()) {
                    emitter.onSuccess(transactionHolder);
                } else {
                    emitter.onError(new DatabaseConnectionException(pendingQuery.cause()));
                }
            });
        });
    }

    /**
     * Adquire uma conexao do pool e inicia uma nova transacao,
     * armazenando esse contexto no {@link TransactionHolder}
     */
    private Single<TransactionHolder> buildTransaction() {
        return Single.create(emitter -> {

            this.connectionPool.getConnection(pendingConnection -> {

                if (pendingConnection.succeeded()) {
                    final SqlConnection connection = pendingConnection.result();

                    connection.begin(asyncTransaction -> {
                        if (asyncTransaction.succeeded()) {
                            final Transaction transaction = asyncTransaction.result();
                            final TransactionHolder transactionHolder = new TransactionHolder(connection, transaction);

                            emitter.onSuccess(transactionHolder);
                        } else {
                            emitter.onError(new DatabaseConnectionException(asyncTransaction.cause()));
                        }
                    });
                } else {
                    emitter.onError(new DatabaseConnectionException(pendingConnection.cause()));
                }
            });
        });
    }
}
