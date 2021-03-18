package io.github.victorhsr.retry.recommendations.core.recommendations.usecase.generate.port;

import io.github.victorhsr.retry.recommendations.processor.dto.MovieData;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

/**
 * output port, define como poderemos recuperar dados de filmes
 * que estao relacionados com certas tags
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public interface MoviesByTagPort {

    /**
     * Verifica e recupera filmes que estao relacionados as tags
     * passadas
     *
     * @param tags tags a serem verificadas
     */
    Observable<MovieData> getMoviesByTag(final List<String> tags);

}
