package io.github.victorhsr.retry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.victorhsr.retry.commons.json.localdatetime.deserialization.LocalDateTimeDeserializer;
import io.github.victorhsr.retry.commons.json.localdatetime.serialization.LocalDateTimeSerializer;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidade que reflete um evento de dominio lancado no sistema.
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractDomainEvent<PayloadType> implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("issued_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime issuedAt;

    @JsonProperty("answered_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime answeredAt;

    @JsonProperty("payload")
    private PayloadType payload;

    @JsonProperty("event_type")
    private String eventType;

}