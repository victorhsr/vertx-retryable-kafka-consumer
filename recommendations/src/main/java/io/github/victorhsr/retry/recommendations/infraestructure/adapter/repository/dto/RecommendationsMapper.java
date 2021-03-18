package io.github.victorhsr.retry.recommendations.infraestructure.adapter.repository.dto;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/

import io.github.victorhsr.retry.recommendations.event.Recommendations;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Mapper
public interface RecommendationsMapper {

    RecommendationsMapper INSTANCE = Mappers.getMapper(RecommendationsMapper.class);

    RecommendationsEntity recommendationsToEntity(final Recommendations recommendations);

    Recommendations entityToRecommendations(final RecommendationsEntity recommendationsEntity);

}
