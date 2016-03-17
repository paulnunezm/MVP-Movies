package com.nunez.popularmovies.model.entities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class AbstractMovie implements Serializable{

    public int id;

    @SerializedName("overview")
    public String description;

    public String title;

    @Expose @SerializedName("poster_path")
    public String posertPath;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("vote_average")
    public String rating;


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

    public String getReleaseDate() {
        return releaseDate;
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


    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosertPath(String posertPath) {
        this.posertPath = posertPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

}
