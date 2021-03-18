package io.github.victorhsr.retry.recommendations.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.victorhsr.retry.commons.date.localdate.LocalDateParser;
import io.github.victorhsr.retry.commons.json.localdate.deserialization.LocalDateDeserializer;
import io.github.victorhsr.retry.commons.json.localdate.serialization.LocalDateSerializer;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * Estrutura de dados necessaria para gerar
 * recomendacoes para o usuário
 * </p>
 * <p>
 * TL;DR: necessario para nao criarmos um acoplamento
 * direto entre o servico de recomendacoes e a estrutura
 * das mensagens enviadas. Caso a mensagem mude, ou mesmo a forma
 * com a qual as recomendacoes sao engatilhadas, nao precisaremos
 * revisitar as classes que fazem uso da mesma
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
@DataObject
@NoArgsConstructor
public class PersonData {

    @JsonProperty("id")
    private String id;

    @JsonProperty("birth_date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;

    @JsonProperty("preferences")
    private List<String> preferences;

    public PersonData(JsonObject jsonObject) {
        this.id = jsonObject.getString("id");
        this.birthDate = new LocalDateParser().deserialize(jsonObject.getString("birth_date"), LocalDateSerializer.LOCAL_DATE_FORMAT);
        this.preferences = jsonObject.getJsonArray("preferences").getList();
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
