package io.github.victorhsr.retry.recommendations.application.rest.v1.recommendations.details.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
@ToString
public class RecommendationsDetailsDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("person_id")
    private String person;

    @JsonProperty("movies")
    private List<String> movies;

}
