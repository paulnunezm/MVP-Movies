package com.nunez.popularmovies.domain;

import android.content.Context;

import com.nunez.popularmovies.model.entities.MoviesWrapper;
import com.nunez.popularmovies.model.restApi.RestDataSource;
import com.nunez.popularmovies.model.restApi.RestMovieSource;
import com.nunez.popularmovies.utils.Callbacks;

/**
 * This class is an implementation of {@link GetMoviesUsecase}
 */
public class GetMoviesController implements GetMoviesUsecase {

    RestDataSource mDataSource;
    Callbacks.StandarCallback callback;
    MoviesCallback mPresenterCallback;


    public GetMoviesController(MoviesCallback presenterCallback) {

        mPresenterCallback = presenterCallback;

//        callback = new MoviesCallback() {
//            @Override
//            public void onSuccess(MoviesWrapper moviesWrapper) {
//                sendMoviesToPresenter(moviesWrapper);
//            }
//
//            @Override
//            public String onError(String e) {
//               mPresenterCallback.onError(e);
//                return null;
//            }
//        };

        callback = new Callbacks.StandarCallback() {
            @Override
            public void onSuccess(Object o) {
                sendMoviesToPresenter((MoviesWrapper) o);
            }

            @Override
            public void onError(String e) {
                mPresenterCallback.onError(e);
            }
        };

        mDataSource = new RestMovieSource(callback);
    }

    @Override
    public void requestPopularMovies() {
        mDataSource.getMovies();
    }

    @Override
    public void sendMoviesToPresenter(MoviesWrapper response) {
        mPresenterCallback.onSuccess(response);
    }

    @Override
    public void execute() {
        requestPopularMovies();
    }

}
