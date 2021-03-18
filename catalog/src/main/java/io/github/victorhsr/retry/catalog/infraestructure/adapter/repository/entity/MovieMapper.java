package io.github.victorhsr.retry.catalog.infraestructure.adapter.repository.entity;

import io.github.victorhsr.retry.catalog.core.movie.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieEntity movieToEntity(final Movie movie);

    Movie entityToMovie(final MovieEntity movieEntity);

}