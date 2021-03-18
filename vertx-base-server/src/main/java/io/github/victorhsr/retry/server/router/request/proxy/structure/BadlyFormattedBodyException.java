package io.github.victorhsr.retry.server.router.request.proxy.structure;

/**
 * Excessao lancadao quando um corpo de requisicao
 * mal formatado eh recebido pelo servidor
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class BadlyFormattedBodyException extends RuntimeException {

    public BadlyFormattedBodyException(Throwable cause) {
        super(cause);
    }
}
