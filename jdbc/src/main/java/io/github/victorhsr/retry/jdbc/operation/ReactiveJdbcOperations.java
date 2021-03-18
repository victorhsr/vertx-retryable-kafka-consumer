package io.github.victorhsr.retry.jdbc.operation;

import io.github.victorhsr.retry.jdbc.SqlCommand;
import io.github.victorhsr.retry.jdbc.operation.transaction.NotCommitedResult;
import io.github.victorhsr.retry.jdbc.operation.transaction.TransactionHolder;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.Optional;
import java.util.function.Function;

/**
 * Contrato que define operacoes JDBC que serao executadas
 * de forma reativa.
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface ReactiveJdbcOperations {

    /**
     * Executa um {@link SqlCommand} retornando o objeto
     * esperado como resposta da consulta
     *
     * @param sqlCommand      query sql a ser executada
     * @param resultExtractor funcao que ira montar o objeto de resposta da consulta
     */
    <ResultType> Single<Optional<ResultType>> executeQuery(final SqlCommand sqlCommand, final Function<RowSet<Row>, ResultType> resultExtractor);

    /**
     * Semelhante a {@link #executeQuery(SqlCommand, Function)}, porem abrindo uma transacao
     * e retornando-a para que seja fechada posteriormente pelo caller do metodo
     *
     * @param sqlCommand      query sql a ser executada
     * @param resultExtractor funcao que ira montar o objeto de resposta da consulta
     */
    <ResultType> Single<NotCommitedResult<ResultType>> executeQueryOpenTransaction(final SqlCommand sqlCommand, final Function<RowSet<Row>, ResultType> resultExtractor);

    /**
     * Semelhante a {@link #executeQueryOpenTransaction(SqlCommand, Function)}, porem
     * reutilizando uma transacao previamente aberta
     *
     * @param transactionHolder transacao a ser reutilizada
     * @param sqlCommand        query sql a ser executada
     * @param resultExtractor   funcao que ira montar o objeto de resposta da consulta
     */
    <ResultType> Single<NotCommitedResult<ResultType>> executeQuery(final TransactionHolder transactionHolder, final SqlCommand sqlCommand, final Function<RowSet<Row>, ResultType> resultExtractor);

    /**
     * Executa um {@link SqlCommand} que nao necessita de resposta
     *
     * @param sqlCommand query sql a ser executada
     */
    Completable execute(final SqlCommand sqlCommand);

    /**
     * Semelhante a {@link #execute(SqlCommand)}, porem abrindo uma transacao
     * * e retornando-a para que seja fechada posteriormente pelo caller do metodo
     *
     * @param sqlCommand query sql a ser executada
     */
    Single<TransactionHolder> executeOpenTransaction(final SqlCommand sqlCommand);

    /**
     * Semelhante a {@link #executeOpenTransaction(SqlCommand)}, porem
     * reutilizando uma transacao previamente aberta
     *
     * @param transactionHolder ransacao a ser reutilizada
     * @param sqlCommand        query sql a ser executada
     */
    Single<TransactionHolder> execute(final TransactionHolder transactionHolder, final SqlCommand sqlCommand);

}
