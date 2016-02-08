package com.nunez.popularmovies.model.restApi;

/**
 * Created by paulnunez on 11/15/15.
 */
public interface MediaDataSource{

    void getMovies();
    void getMovieDetails(String id);


    // all the methods of querying the api.

}
