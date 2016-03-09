package com.nunez.popularmovies.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by paulnunez on 11/14/15.
 */
public class MoviesWrapper {

    @SerializedName("results")
    public ArrayList<Movie> movies;

    int page;

    int total_pages;

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
