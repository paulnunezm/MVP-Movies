package com.nunez.popularmovies.model.entities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Movie {

    public int id;

    @SerializedName("overview")
    public String description;

    public String title;

    @Expose @SerializedName("poster_path")
    public String posertPath;


    // Not parsed
    public VideosWrapper videosWrapper;
    public ReviewsWrapper reviewsWrapper;



    public String getId() {
        return String.valueOf(id);
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posertPath;
    }

    public String getDescription() {
        return description;
    }

    public VideosWrapper getVideosWrapper() {
        return videosWrapper;
    }

    public ReviewsWrapper getReviewsWrapper() {
        return reviewsWrapper;
    }

    public void setVideosWrapper(VideosWrapper videosWrapper) {
        this.videosWrapper = videosWrapper;
    }

    public void setId(String id) {
        this.id = Integer.valueOf(id);
    }

    public void setReviewsWrapper(ReviewsWrapper reviewsWrapper) {
        this.reviewsWrapper = reviewsWrapper;
    }
}
