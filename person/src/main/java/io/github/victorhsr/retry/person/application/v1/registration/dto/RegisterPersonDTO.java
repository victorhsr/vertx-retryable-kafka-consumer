package io.github.victorhsr.retry.person.application.v1.registration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.victorhsr.retry.commons.json.localdate.deserialization.LocalDateDeserializer;
import io.github.victorhsr.retry.commons.json.localdate.serialization.LocalDateSerializer;
import io.github.victorhsr.retry.commons.validation.SelfValidatingObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
@ToString
public class RegisterPersonDTO extends SelfValidatingObject<RegisterPersonDTO> {

    @JsonProperty("name")
    @NotEmpty(message = "{person.name.empty}")
    private String name;

    @JsonProperty("email")
    @Email(message = "{person.email.invalid}")
    @NotEmpty(message = "{person.email.empty}")
    private String email;

    @JsonProperty("birth_date")
    @NotNull(message = "{person.birthDate.null}")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;

    @JsonProperty("preferences")
    private List<String> preferences;

    public RegisterPersonDTO() {
        this.preferences = new ArrayList<>();
    }
}
