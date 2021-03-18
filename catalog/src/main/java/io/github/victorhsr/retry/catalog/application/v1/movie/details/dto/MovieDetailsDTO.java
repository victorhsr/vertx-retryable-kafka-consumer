package io.github.victorhsr.retry.catalog.application.v1.movie.details.dto;

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
public class MovieDetailsDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("release_year")
    private int releaseYear;

}
