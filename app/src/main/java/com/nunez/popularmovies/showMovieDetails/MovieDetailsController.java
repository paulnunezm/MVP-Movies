package com.nunez.popularmovies.showMovieDetails;

import android.util.Log;

import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.model.restApi.RestMovieSource;
import com.nunez.popularmovies.utils.Callbacks;

/**
 * Created by paulnunez on 2/3/16.
 */
public class MovieDetailsController implements MovieDetailsContract.MovieDetailsController,
    Callbacks.StandarCallback{

    private RestMovieSource mDataSource;
    private Callbacks.StandarCallback mPresenterCallback;
    private String mMovieId;

    /**
     *
     * @param movieId
     * @param presenterCallback
     */
    public MovieDetailsController(String movieId, Callbacks.StandarCallback presenterCallback){

        mDataSource = new RestMovieSource(this);
        mPresenterCallback = presenterCallback;
        mMovieId = movieId;
    }

    @Override
    public void requestMovieDetails() {
        mDataSource.getMovieDetails(mMovieId);
    }

    public void sendErrorToPresenter(String e){
        mPresenterCallback.onError(e);
    }

    @Override
    public void sendMovieDetailsToPresenter(Movie movieDetails) {
        mPresenterCallback.onSuccess(movieDetails);
    }

    @Override
    public void execute() {
        requestMovieDetails();
    }


//    Callback implementation

    @Override
    public void onSuccess(Object movie) {
        sendMovieDetailsToPresenter((Movie) movie);
    }

    @Override
    public void onError(String e) {
        sendErrorToPresenter(e);
    }
}
