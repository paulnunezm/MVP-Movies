package com.nunez.popularmovies.ShowMovies;

import com.nunez.popularmovies.domain.MoviesCallback;
import com.nunez.popularmovies.model.data.DbDataSource;
import com.nunez.popularmovies.model.entities.MoviesWrapper;
import com.nunez.popularmovies.model.restApi.RestMovieSource;
import com.nunez.popularmovies.utils.Callbacks;

/**
 * This class is an implementation of {@link GetMoviesUsecase}
 */
public class GetMoviesController implements GetMoviesUsecase, Callbacks.StandarCallback {

    RestMovieSource mDataSource;
    Callbacks.StandarCallback callback;
    MoviesCallback mPresenterCallback;


    public GetMoviesController(MoviesCallback presenterCallback) {

        mPresenterCallback = presenterCallback;

//        callback = new Callbacks.StandarCallback() {
//            @Override
//            public void onSuccess(Object o) {
//                sendMoviesToPresenter((MoviesWrapper) o);
//            }
//
//            @Override
//            public void onError(String e) {
//                mPresenterCallback.onError(e);
//            }
//        };

        mDataSource = new RestMovieSource(this);
    }

    @Override
    public void requestPopularMovies() {
        mDataSource.getMovies();
    }

    @Override
    public void requestFavoriteMovies() {
        DbDataSource.favoriteMovies(this);
    }


    @Override
    public void sendMoviesToPresenter(MoviesWrapper response) {
        mPresenterCallback.onSuccess(response);
    }

    @Override
    public void execute() {
        requestPopularMovies();
    }

    @Override
    public void onSuccess(Object o) {
        sendMoviesToPresenter((MoviesWrapper) o);
    }

    @Override
    public void onError(String e) {
        mPresenterCallback.onError(e);
    }
}
