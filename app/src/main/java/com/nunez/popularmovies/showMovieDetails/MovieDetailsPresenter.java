package com.nunez.popularmovies.showMovieDetails;

import com.nunez.popularmovies.R;
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
    private String movieId;
    private Movie mMovie;
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
    public void checkIfFavorite() {

        if(mDetailsController.checkIfFavorite(mDetailView.getContext(), movieId))
            mDetailView.setFavorite();
    }

    @Override
    public void showReleaseDate() {
        mDetailView.showReleaseDate(mMovie.getReleaseDate().replace("-","/"));
    }

    @Override
    public void showRatings() {
        mDetailView.showRatings(String.format(mDetailView.getContext().getResources()
            .getString(R.string.format_rating), mMovie.rating));
    }

    @Override
    public void saveMovieToDb() {
        mDetailsController.saveMovieToDb(mMovie);
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
