package com.nunez.popularmovies.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by paulnunez on 3/10/16.
 */
public class MovieDetails extends AbstractMovie {


    @SerializedName("genres")
    public ArrayList<DetailGenres> genres;


    private class DetailGenres{
        Integer id;
    }
}
