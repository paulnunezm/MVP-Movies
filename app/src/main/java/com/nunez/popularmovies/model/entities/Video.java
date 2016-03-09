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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
