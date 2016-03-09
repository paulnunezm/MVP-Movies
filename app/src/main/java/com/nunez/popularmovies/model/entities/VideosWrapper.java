package com.nunez.popularmovies.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by paulnunez on 2/3/16.
 */
public class VideosWrapper {

    @SerializedName("results")
    public ArrayList<Video> videos;

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }
}
