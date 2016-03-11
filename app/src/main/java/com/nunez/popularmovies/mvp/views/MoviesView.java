package com.nunez.popularmovies.mvp.views;

import com.nunez.popularmovies.model.entities.Movie;

import java.util.ArrayList;

/**
 * Created by paulnunez on 11/15/15.
 */
public interface MoviesView extends MVPView {

    void showMovies(ArrayList<Movie> movieList);

    void showNoMovies();

    boolean isTheListEmpty ();

    void appendMovies (ArrayList<Movie> movieList);
}
