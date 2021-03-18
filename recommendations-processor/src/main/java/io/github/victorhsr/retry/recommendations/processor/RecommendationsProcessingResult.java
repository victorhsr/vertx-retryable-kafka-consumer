package io.github.victorhsr.retry.recommendations.processor;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.victorhsr.retry.recommendations.processor.dto.MovieData;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Getter
@Setter
@ToString
@DataObject
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationsProcessingResult {

    @JsonProperty("movies")
    private List<MovieData> movies;

    public RecommendationsProcessingResult(JsonObject jsonObject) {
        this.movies = Arrays.asList(Json.decodeValue(jsonObject.getJsonArray("movies").toBuffer(), MovieData[].class));
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

}
