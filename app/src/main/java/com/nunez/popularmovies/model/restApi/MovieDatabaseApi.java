package com.nunez.popularmovies.model.restApi;

import android.support.annotation.NonNull;

/**
 * A class that contains the Movie data base endpoints
 */
 public class MovieDatabaseApi  {

    private String API_KEY = "api_key=";
    private static final String BASE_URL =  "http://api.themoviedb.org/3";
//    private static final String GET_POPULAR_MOVIES = BASE_URL + "/discover/movie";
    private static final String GET_POPULAR_MOVIES = BASE_URL + "/movie/popular";
    private static final String GET_MOVIE_DETAILS = BASE_URL + "/movie/";
    private static final String GET_MOVIE_DETAILS_VIDEO = "/videos";
    private static final String GET_MOVIE_DETAILS_REVIEWS = "/reviews";



    /**
     *
     * @param mAPI_KEY the movie database api key
     */
    public  MovieDatabaseApi(@NonNull String mAPI_KEY) {

       API_KEY =  API_KEY.concat(mAPI_KEY);
    }

    public String getPopularMovies(String sortType){
        return null;
    }

    public String getPopularMovies(){
        return GET_POPULAR_MOVIES + '?' + API_KEY;
    }

    public String getMovieDetails(String id){
        return GET_MOVIE_DETAILS + id + '?' + API_KEY;
    }

    public String getMovieVideo(String id){
        return GET_MOVIE_DETAILS + id + GET_MOVIE_DETAILS_VIDEO + '?' + API_KEY;
    }

    public String getMovieReviews(String id){
        return GET_MOVIE_DETAILS + id + GET_MOVIE_DETAILS_REVIEWS + '?' + API_KEY;
    }
}
