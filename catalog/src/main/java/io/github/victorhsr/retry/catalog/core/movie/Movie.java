package io.github.victorhsr.retry.catalog.core.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Classe de dominio que representa um filme do catalogo
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
@NoArgsConstructor
public class Movie {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("release_year")
    private int releaseYear;

    public Movie(final String id, final String title, final int releaseYear, final String... tags) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.tags = Objects.isNull(tags) ? new ArrayList<>() : Arrays.asList(tags);
    }

    /**
     * Verifica se o filme pertence a uma
     * determinada tag
     *
     * @param tag tag a ser verificada
     */
    public boolean belongsToTag(final String tag) {
        return this.tags.contains(tag.toLowerCase());
    }
}
