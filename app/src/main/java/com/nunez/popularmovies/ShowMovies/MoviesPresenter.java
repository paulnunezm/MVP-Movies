package com.nunez.popularmovies.ShowMovies;

import android.content.SharedPreferences;
import android.util.Log;

import com.nunez.popularmovies.PopularMovies;
import com.nunez.popularmovies.domain.MoviesCallback;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.model.entities.MoviesWrapper;
import com.nunez.popularmovies.mvp.presenters.Presenter;
import com.nunez.popularmovies.mvp.views.MoviesView;
import com.nunez.popularmovies.utils.Constants;

import java.util.ArrayList;

/**
 * Created by paulnunez on 11/15/15.
 */
public class MoviesPresenter implements Presenter {
    private static final String LOG_TAG = MoviesPresenter.class.getSimpleName();

    GetMoviesController getMoviesController;
    MoviesView          mMoviesView;
    MoviesCallback      mCallback;
    ArrayList<Movie>    mMovies;


    public MoviesPresenter() {
        mCallback = new MoviesCallback() {
            @Override
            public void onSuccess(MoviesWrapper moviesWrapper) {
                onMoviesReceived(moviesWrapper);
            }

            @Override
            public String onError(String e) {
                Log.d(LOG_TAG, e);
                return null;
            }
        };

        getMoviesController = new GetMoviesController(mCallback);
    }

    public void attachView(MoviesView moviesView) {
        mMoviesView = moviesView;
    }

    @Override
    public void start() {
        mMoviesView.showLoading();

    }

    public void requestMovies(){
        mMoviesView.showLoading();

        SharedPreferences sharedPreferences = PopularMovies.context
                .getSharedPreferences(Constants.PREFS, 0);

        // if spannable is on favorites
        if(sharedPreferences.getString(Constants.PREFS_SORT, Constants.SORT_POPULAR)
                .equals(Constants.SORT_FAVORITES)){

            getMoviesController.requestFavoriteMovies();
        }else{
            getMoviesController.requestPopularMovies();
        }

    }

    public void showMovies(){
        mMoviesView.hideLoading();

        if (mMovies != null && !mMovies.isEmpty()){
            mMoviesView.showMovies(mMovies);
        }else{
            mMoviesView.showNoMovies();
        }
    }

    @Override
    public void stop() {
        mMoviesView = null;
    }

    public ArrayList<Movie> getMovieList(){
        return mMovies;
    }

    public void setMovieList(ArrayList<Movie> list){
        mMovies = list;
    }

    public void onMoviesReceived(MoviesWrapper moviesWrapper){
        mMovies = moviesWrapper.movies;
        showMovies();
    }
}
