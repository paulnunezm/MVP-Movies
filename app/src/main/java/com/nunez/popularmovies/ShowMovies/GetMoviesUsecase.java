package com.nunez.popularmovies.ShowMovies;

import com.nunez.popularmovies.domain.UseCase;
import com.nunez.popularmovies.model.entities.MoviesWrapper;

/**
 * A use case to get the popular movies
 */
public interface GetMoviesUsecase extends UseCase {

    /**
     * Request from the datasource (server) the popular movies
     */
    void requestPopularMovies();

    /**
     * Request favorite movies from the content provider
     */
    void requestFavoriteMovies();

    /**
     * Send the popular movies response from the api through a callback.
     *
     * @param response
     */
    void sendMoviesToPresenter(MoviesWrapper response);



}
