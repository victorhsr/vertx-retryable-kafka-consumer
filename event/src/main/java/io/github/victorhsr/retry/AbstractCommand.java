package io.github.victorhsr.retry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Estrutura de dados base para comandos lancados no sistema
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/

@Getter
@Setter
@ToString
@AllArgsConstructor
public abstract class AbstractCommand<PayloadType> {

    private LocalDateTime issuedAt;
    private PayloadType payload;

}
