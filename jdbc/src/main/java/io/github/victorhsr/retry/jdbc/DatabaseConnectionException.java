package io.github.victorhsr.retry.jdbc;

/**
 * Excessao lancada quando um problema de conexao com a
 * base de dados ocorre
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/

public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException() {
    }

    public DatabaseConnectionException(Throwable cause) {
        super(cause);
    }

    public DatabaseConnectionException(String message) {
        super(message);
    }
}
