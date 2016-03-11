package com.nunez.popularmovies.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by paulnunez on 3/10/16.
 */
public class Movie extends AbstractMovie {
    @SerializedName("genre_ids")
    public ArrayList<Integer> genres;

    public void setGenres(ArrayList<Integer> genres) {
        this.genres = genres;
    }

}
