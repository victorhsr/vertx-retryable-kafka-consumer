package io.github.victorhsr.retry.catalog.infraestructure;

import io.github.victorhsr.retry.catalog.core.movie.usecase.details.DefaultMovieDetailsUC;
import io.github.victorhsr.retry.catalog.core.movie.usecase.details.MovieDetailsUC;
import io.github.victorhsr.retry.catalog.core.movie.usecase.details.port.MovieByIdPort;
import io.github.victorhsr.retry.catalog.core.movie.usecase.filtermovies.DefaultFilterMoviesUC;
import io.github.victorhsr.retry.catalog.core.movie.usecase.filtermovies.FilterMoviesUC;
import io.github.victorhsr.retry.catalog.core.movie.usecase.filtermovies.port.FilterMoviesByTagsPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Classe que prove instancias para que sejam
 * gerenciadas pelo spring-context
 * </p>
 *
 * <p>
 * TL;DR O core da aplicacao nao esta acoplado a nenhum
 * framework DI, ou banco de dados/file system/etc,
 * seguindo a logica de uma aplicacao hexagonal
 * </p>
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Configuration
public class MovieConfiguration {

    @Bean
    public FilterMoviesUC filterMoviesUC(final FilterMoviesByTagsPort filterMoviesByTagsPort) {
        return new DefaultFilterMoviesUC(filterMoviesByTagsPort);
    }

    @Bean
    public MovieDetailsUC movieDetailsUC(final MovieByIdPort movieByIdPort){
        return new DefaultMovieDetailsUC(movieByIdPort);
    }

}
