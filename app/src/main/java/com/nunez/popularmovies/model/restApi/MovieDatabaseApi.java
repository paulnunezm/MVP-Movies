package com.nunez.popularmovies.model.restApi;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.nunez.popularmovies.PopularMovies;
import com.nunez.popularmovies.utils.Constants;

/**
 * A class that contains the Movie data base endpoints
 */
 public class MovieDatabaseApi  {

    private String API_KEY = "api_key=";
    private static final String BASE_URL =  "http://api.themoviedb.org/3";
    private static final String GET_MOVIES = BASE_URL + "/discover/movie";
    private static final String GET_POPULAR_MOVIES = BASE_URL + "/movie/popular";
    private static final String GET_TOP_RATED = BASE_URL + "/movie/top_rated";
    private static final String GET_MOVIE_DETAILS = BASE_URL + "/movie/";
    private static final String GET_MOVIE_DETAILS_VIDEO = "/videos";
    private static final String GET_MOVIE_DETAILS_REVIEWS = "/reviews";

    private SharedPreferences sharedPreferences;


    /**
     *
     * @param mAPI_KEY the movie database api key
     */
    public  MovieDatabaseApi(@NonNull String mAPI_KEY) {

       API_KEY =  API_KEY.concat(mAPI_KEY);
    }


    public String getPopularMovies(){

        sharedPreferences = PopularMovies.context.getSharedPreferences(Constants.PREFS, 0);

        String sort;

        if(sharedPreferences.getString(Constants.PREFS_SORT, Constants.SORT_POPULAR)
                .equals(Constants.SORT_POPULAR)){
            //return GET_POPULAR_MOVIES + '?' + API_KEY;
            sort = "popularity.desc";
        }else{
            //return GET_TOP_RATED + '?' + API_KEY;
            sort = "vote_average.desc";
        }

        return GET_MOVIES+"?certification_country=US&sort_by="+sort+"&"+API_KEY;
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
