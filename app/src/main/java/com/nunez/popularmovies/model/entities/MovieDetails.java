package com.nunez.popularmovies.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by paulnunez on 3/10/16.
 */
public class MovieDetails extends AbstractMovie {


    @SerializedName("genres")
    public ArrayList<DetailGenres> genres;


    public class DetailGenres{
        Integer id;

        public Integer getId() {
            return id;
        }
    }
}
