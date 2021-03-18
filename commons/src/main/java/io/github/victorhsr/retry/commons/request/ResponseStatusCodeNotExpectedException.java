package io.github.victorhsr.retry.commons.request;

/**
 * Excessao lancada ao se obter um status code inexperado
 * ao se tratar uma requisicao com {@link HttpResponseHandlerBuilder.HttpResponseHandler}
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class ResponseStatusCodeNotExpectedException extends RuntimeException {

    public ResponseStatusCodeNotExpectedException(String message) {
        super(message);
    }
}
