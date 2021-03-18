package io.github.victorhsr.retry.recommendations.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
@ToString
@DataObject
@NoArgsConstructor
public class MovieData {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("release_year")
    private int releaseYear;

    @JsonProperty("tags")
    private List<String> tags;

    public MovieData(JsonObject jsonObject) {
        this.id = jsonObject.getString("id");
        this.title = jsonObject.getString("title");
        this.tags = jsonObject.getJsonArray("tags").getList();
        this.releaseYear = jsonObject.getInteger("release_year");
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

}
