package io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
public class RecommendationsEntity {

    @JsonProperty("id")
    private String id;

    @JsonProperty("person_id")
    private String person;

    @JsonProperty("movies")
    private List<String> movies;

}
