package io.github.victorhsr.retry.recommendations.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Classe de dominio que representa recomendacoes de
 * filmes para um usuario
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Recommendations {

    @JsonProperty("id")
    private String id;

    @JsonProperty("person_id")
    private String person;

    @JsonProperty("movies")
    private List<String> movies;

}
