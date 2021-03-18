package io.github.victorhsr.retry.catalog.core.movie.usecase.filtermovies.port;

import io.github.victorhsr.retry.catalog.core.movie.Movie;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

/**
 * output port, define como poderemos obter os filmes
 * que dao match com as tags passadas
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface FilterMoviesByTagsPort {

    /**
     * Aplica uma filtragem nos filmes armazenados
     * a partir de suas tags
     *
     * @param tags tags que servirao como filtro
     * @return filmes cujo tags deram match com o filtro aplicado
     */
    Observable<Movie> filterByTags(final List<String> tags);

}
