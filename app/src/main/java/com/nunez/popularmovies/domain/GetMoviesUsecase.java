package com.nunez.popularmovies.domain;

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
     * Send the popular movies response from the api through a callback.
     *
     * @param response
     */
    void sendMoviesToPresenter(MoviesWrapper response);

}
