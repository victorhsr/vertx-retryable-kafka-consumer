package io.github.victorhsr.retry.catalog.infraestructure.adapter.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
public class MovieEntity {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("release_year")
    private int releaseYear;

}
