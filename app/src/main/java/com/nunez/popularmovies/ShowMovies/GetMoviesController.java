package com.nunez.popularmovies.ShowMovies;

import android.database.Cursor;

import com.nunez.popularmovies.PopularMovies;
import com.nunez.popularmovies.domain.MoviesCallback;
import com.nunez.popularmovies.model.data.MoviesProvider;
import com.nunez.popularmovies.model.entities.MoviesWrapper;
import com.nunez.popularmovies.model.restApi.RestDataSource;
import com.nunez.popularmovies.model.restApi.RestMovieSource;
import com.nunez.popularmovies.utils.Callbacks;
import com.nunez.provider.MoviesDatabase;

/**
 * This class is an implementation of {@link GetMoviesUsecase}
 */
public class GetMoviesController implements GetMoviesUsecase {

    RestDataSource mDataSource;
    Callbacks.StandarCallback callback;
    MoviesCallback mPresenterCallback;


    public GetMoviesController(MoviesCallback presenterCallback) {

        mPresenterCallback = presenterCallback;

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
    public void requestFavoriteMovies() {
        Cursor movies = PopularMovies.context.getContentResolver().query(
                MoviesProvider.Movies.MOVIES,
                null, null, null, null);

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
