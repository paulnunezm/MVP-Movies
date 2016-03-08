package com.nunez.popularmovies.showMovieDetails;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.nunez.popularmovies.PopularMovies;
import com.nunez.popularmovies.model.data.DbDataSource;
import com.nunez.popularmovies.model.data.MoviesColumns;
import com.nunez.popularmovies.model.data.MoviesProvider;
import com.nunez.popularmovies.model.data.ReviewsTrailers;
import com.nunez.popularmovies.model.data.TrailersColumns;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.model.entities.Review;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.model.restApi.RestMovieSource;
import com.nunez.popularmovies.utils.Callbacks;

import java.util.ArrayList;

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

        mDataSource = new RestMovieSource( this);
        mPresenterCallback = presenterCallback;
        mMovieId = movieId;
    }

    @Override
    public void requestMovieDetails() {
//        checkIfFavorite(this, mMovieId);
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
    public boolean checkIfFavorite(Context context, String id) {
        return DbDataSource.checkIfFavorite(context, id);
    }

    @Override
    public void saveMovieToDb(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesColumns.MOVIE_ID, mMovieId);
        values.put(MoviesColumns.TITLE, movie.title);
        values.put(MoviesColumns.DESCRIPTION, movie.description);
        values.put(MoviesColumns.POSTER, movie.posertPath);

        Uri inserMoviesUri;

        inserMoviesUri = PopularMovies.context.getContentResolver().insert(
                MoviesProvider.Movies.MOVIES,
                values);

        ArrayList<Video> trailers = movie.getVideosWrapper().videos;

        if(!trailers.isEmpty()){
            for (Video trailer:trailers) {
                ContentValues trailerValues = new ContentValues();
                trailerValues.put(TrailersColumns.TRAILER_ID, trailer.id);
                trailerValues.put(TrailersColumns.SITE, trailer.site);
                trailerValues.put(TrailersColumns.TITLE, trailer.name);
                trailerValues.put(TrailersColumns.MOVIE_ID, movie.id);

                PopularMovies.context.getContentResolver().insert(
                        MoviesProvider.Trailers.Trailers,
                        trailerValues);
            }
        }

        ArrayList<Review> reviews = movie.getReviewsWrapper().getReviews();

        if(!reviews.isEmpty()){
            for (Review review:reviews) {
                ContentValues reviewsValues = new ContentValues();
                reviewsValues.put(ReviewsTrailers.AUTHOR, review.getAuthor());
                reviewsValues.put(ReviewsTrailers.CONTENT, review.getContent());
                reviewsValues.put(ReviewsTrailers.URL, review.getUrl());
                reviewsValues.put(TrailersColumns.MOVIE_ID, movie.id);

                PopularMovies.context.getContentResolver().insert(
                        MoviesProvider.Reviews.Reviews,
                        reviewsValues);
            }
        }
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
