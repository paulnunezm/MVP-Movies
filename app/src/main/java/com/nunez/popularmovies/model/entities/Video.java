package com.nunez.popularmovies.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by paulnunez on 2/3/16.
 */
public class Video {

    @SerializedName("key")
    public String id;

    public String name;
    public String site;

    public String getId() {
        return id;
    }
}
