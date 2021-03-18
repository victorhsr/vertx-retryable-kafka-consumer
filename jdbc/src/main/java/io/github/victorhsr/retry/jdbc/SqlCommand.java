package io.github.victorhsr.retry.jdbc;

import io.vertx.sqlclient.Tuple;
import lombok.Getter;
import lombok.Setter;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
public class SqlCommand {

    private String query;
    private Tuple params;

    public SqlCommand(String query) {
        this.query = query;
    }

    public SqlCommand(String query, Tuple params) {
        this.query = query;
        this.params = params;
    }

}
