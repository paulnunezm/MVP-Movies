package com.nunez.popularmovies.ShowMovies;

import android.content.SharedPreferences;
import android.util.Log;

import com.nunez.popularmovies.PopularMovies;
import com.nunez.popularmovies.domain.MoviesCallback;
import com.nunez.popularmovies.model.entities.MoviesWrapper;
import com.nunez.popularmovies.mvp.presenters.Presenter;
import com.nunez.popularmovies.mvp.views.MoviesView;
import com.nunez.popularmovies.utils.Constants;

/**
 * Created by paulnunez on 11/15/15.
 */
public class MoviesPresenter implements Presenter {
    private static final String LOG_TAG = MoviesPresenter.class.getSimpleName();

    GetMoviesController getMoviesController;
    MoviesView          mMoviesView;
    MoviesCallback      mCallback;

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

    @Override
    public void stop() {
        mMoviesView = null;
    }

    public void onMoviesReceived(MoviesWrapper moviesWrapper){
        mMoviesView.hideLoading();
        if (!moviesWrapper.movies.isEmpty()){
            mMoviesView.showMovies(moviesWrapper.movies);
        }else{
            mMoviesView.showNoMovies();
        }
    }
}
