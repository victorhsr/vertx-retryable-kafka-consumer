package io.github.victorhsr.retry.recommendations.application.rest.v1.recommendations.details.dto;

import io.github.victorhsr.retry.recommendations.event.Recommendations;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Mapper
public interface RecommendationsMapper {

    RecommendationsMapper INSTANCE = Mappers.getMapper(RecommendationsMapper.class);

    RecommendationsDetailsDTO recommendationsToRecommendationsDetails(final Recommendations recommendations);

}
