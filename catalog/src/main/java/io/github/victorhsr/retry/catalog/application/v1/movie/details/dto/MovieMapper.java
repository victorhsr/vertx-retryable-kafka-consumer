package io.github.victorhsr.retry.catalog.application.v1.movie.details.dto;

import io.github.victorhsr.retry.catalog.core.movie.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieDetailsDTO movieToMovieDetailsDTO(final Movie movie);

}
