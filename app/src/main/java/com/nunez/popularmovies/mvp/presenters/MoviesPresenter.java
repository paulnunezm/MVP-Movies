package com.nunez.popularmovies.mvp.presenters;

import android.util.Log;

import com.nunez.popularmovies.domain.GetMoviesController;
import com.nunez.popularmovies.domain.MoviesCallback;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.model.entities.MoviesWrapper;
import com.nunez.popularmovies.mvp.views.MoviesView;

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
        getMoviesController.requestPopularMovies();
    }

    @Override
    public void stop() {
        mMoviesView = null;
    }

    public void onMoviesReceived(MoviesWrapper moviesWrapper){
        mMoviesView.hideLoading();
        mMoviesView.showMovies(moviesWrapper.movies);
    }
}
