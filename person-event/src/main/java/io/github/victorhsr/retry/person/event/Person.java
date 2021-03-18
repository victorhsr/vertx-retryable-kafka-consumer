package io.github.victorhsr.retry.person.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.victorhsr.retry.commons.json.localdate.deserialization.LocalDateDeserializer;
import io.github.victorhsr.retry.commons.json.localdate.serialization.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;


/**
 * Classe de dominio que representa uma pessoa do sistema
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
@ToString
public class Person {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("birth_date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;

    @JsonProperty("preferences")
    private List<String> preferences;

}
