package com.nunez.popularmovies.showMovieDetails;

import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.model.entities.ReviewsWrapper;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.utils.Callbacks;

/**
 * Created by paulnunez on 2/2/16.
 */
public class MovieDetailsPresenter implements MovieDetailsContract.Presenter,
        Callbacks.StandarCallback{

    private static String LOG_TAG = MovieDetailsPresenter.class.getSimpleName();

    private MovieDetailsContract.View mDetailView;
    private MovieDetailsController mDetailsController;
    private Movie mMovie;

    public MovieDetailsPresenter(String id){
        mDetailsController = new MovieDetailsController(id, this);
    }

    @Override
    public void showViews() {
        showPoster(mMovie.getPosterPath());
        showTitle(mMovie.getTitle());
        showDescription(mMovie.getDescription());
        setTrailerLink();
        showTrailers();
        showReviews();
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
    public void attachView(MovieDetailsContract.View detailsView) {
        mDetailView = detailsView;
    }

    @Override
    public void setTrailerLink() {
        Video video = mMovie.getVideosWrapper().videos.get(0);
        mDetailView.setTrailerLink(video.getId());
    }


    @Override
    public void showPoster(String url) {
        mDetailView.showPoster(url);
    }

    @Override
    public void start() {
        mDetailView.showLoading();
        mDetailsController.requestMovieDetails();
    }

    @Override
    public void stop() {

    }

    // Callback implementation
    @Override
    public void onSuccess(Object o) {
        mMovie =  (Movie) o;
        showViews();
    }

    @Override
    public void onError(String e) {
        mDetailView.hideLoading();
        mDetailView.showError();
    }
}
