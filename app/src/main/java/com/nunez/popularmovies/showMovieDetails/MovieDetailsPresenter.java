package com.nunez.popularmovies.showMovieDetails;

import com.nunez.popularmovies.PopularMovies;
import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.MovieDetails;
import com.nunez.popularmovies.model.entities.ReviewsWrapper;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.utils.Callbacks;

import java.util.ArrayList;

/**
 * Created by paulnunez on 2/2/16.
 */
public class MovieDetailsPresenter implements MovieDetailsContract.Presenter,
        Callbacks.StandarCallback{

    private static String LOG_TAG = MovieDetailsPresenter.class.getSimpleName();

    private MovieDetailsContract.View mDetailView;
    private MovieDetailsController mDetailsController;
    private String movieId;
    private MovieDetails mMovie;
    private boolean isFavorite;

    public MovieDetailsPresenter(String id){
        mDetailsController = new MovieDetailsController(id, this);
        movieId = id;
    }

    @Override
    public void showViews() {
        checkIfFavorite();

        showPoster(mMovie.getPosterPath());
        showTitle(mMovie.getTitle());
        showDescription(mMovie.getDescription());
        setTrailerLink();
        showTrailers();
        showReviews();
        showReleaseDate();
        showRatings();
        mDetailView.hideLoading();
    }

    @Override
    public void showTitle(String title) {
        mDetailView.showTitle(title);
    }

    @Override
    public void showDescription(String description) {
        mDetailView.showDescription(description);
    }

    @Override
    public void showTrailers() {
        mDetailView.showTrailers(mMovie.getVideosWrapper().videos);
    }

    @Override
    public void showReviews() {
        ReviewsWrapper reviewsWrapper = mMovie.getReviewsWrapper();
        mDetailView.showReviews(reviewsWrapper.getReviews());
    }

    @Override
    public boolean checkIfFavorite() {

        if(mDetailsController.checkIfFavorite(movieId)){
            mDetailView.setFavorite();
            return true;
        }
        return  false;
    }

    @Override
    public void showReleaseDate() {
        String releaseDate = mMovie.getReleaseDate();
        if(releaseDate != null) mDetailView.showReleaseDate(releaseDate.replace("-", "/"));
    }

    @Override
    public void showRatings() {
        String rating = mMovie.rating;

        if (rating != null) mDetailView.showRatings(String.format(PopularMovies.context.getResources()
                .getString(R.string.format_rating), rating));
    }



    @Override
    public void saveMovieToDb() {
        mDetailsController.saveMovieToDb(mMovie);
    }

    @Override
    public void removeMovieFromDb() {
        mDetailsController.removeMovieFromDb(movieId);
    }


    @Override
    public void attachView(MovieDetailsContract.View detailsView) {
        mDetailView = detailsView;
    }

    @Override
    public void setTrailerLink() {
        ArrayList<Video> videos = mMovie.getVideosWrapper().videos;

        if(!videos.isEmpty()) mDetailView.setTrailerLink(videos.get(0).getId());
    }


    @Override
    public void showPoster(String url) {
        mDetailView.showPoster(url);
    }

    @Override
    public void start() {
        mDetailView.showLoading();

        if(checkIfFavorite()){
            mDetailsController.getFavoriteDetails();
        }else{
            mDetailsController.requestMovieDetails();

        }
    }

    @Override
    public void startDetail(boolean connection) {
        mDetailView.showLoading();

        if(checkIfFavorite()){
            mDetailsController.getFavoriteDetails();
        }else{
            if(connection){
                mDetailsController.requestMovieDetails();
            }else{
                mDetailView.showError();
            }
        }
    }

    @Override
    public void stop() {

    }



    // Callback implementation
    @Override
    public void onSuccess(Object o) {
        mMovie =  (MovieDetails) o;
        showViews();
    }

    @Override
    public void onError(String e) {
        mDetailView.hideLoading();
        mDetailView.showError();
    }
}
