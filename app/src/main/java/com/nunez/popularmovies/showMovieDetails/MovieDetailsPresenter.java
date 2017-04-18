package com.nunez.popularmovies.showMovieDetails;

import com.nunez.popularmovies.PopularMovies;
import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.MovieDetails;
import com.nunez.popularmovies.model.entities.Review;
import com.nunez.popularmovies.model.entities.ReviewsWrapper;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.model.entities.VideosWrapper;
import com.nunez.popularmovies.utils.Callbacks;

import java.util.ArrayList;

/**
 * Created by paulnunez on 2/2/16.
 */
public class MovieDetailsPresenter implements MovieDetailsContract.Presenter,
    Callbacks.StandarCallback {

  private static String LOG_TAG = MovieDetailsPresenter.class.getSimpleName();

  private MovieDetailsContract.View                   mDetailView;
  private MovieDetailsContract.MovieDetailsController mDetailsController;
  private String                                      movieId;
  private MovieDetails                                mMovie;
  private boolean                                     isFavorite;

  public MovieDetailsPresenter() {
  }

  @Override
  public void showViews() {
    checkIfFavorite();

    showPoster(mMovie.getPosterPath());
    showTitle(mMovie.getTitle());
    showDescription(mMovie.getDescription());
    setTrailerLink(mMovie.videosWrapper);
    showTrailers(mMovie.getVideosWrapper());
    showReviews(mMovie.getReviewsWrapper());
    showReleaseDate();
    showRatings(mMovie.rating);
    mDetailView.hideLoading();
  }

  @Override
  public void showTitle(String title) {
    if (title != null) mDetailView.showTitle(title);
  }

  @Override
  public void showDescription(String description) {
    if (description != null && !description.isEmpty()) mDetailView.showDescription(description);
  }

  @Override
  public void showTrailers(VideosWrapper trailers) {
    if (trailers != null && trailers.videos != null && trailers.videos.size() > 0)
      mDetailView.showTrailers(trailers.videos);
  }

  @Override
  public void showReviews(ReviewsWrapper reviewsWrapper) {
    if (reviewsWrapper != null) {
      ArrayList<Review> reviews = reviewsWrapper.getReviews();
      if (reviews != null && !reviews.isEmpty()) mDetailView.showReviews(reviews);
    }
  }

  @Override
  public boolean checkIfFavorite() {

    if (mDetailsController.checkIfFavorite()) {
      mDetailView.setFavorite();
      return true;
    }
    return false;
  }

  @Override
  public void showReleaseDate() {
    String releaseDate = mMovie.getReleaseDate();
    if (releaseDate != null) mDetailView.showReleaseDate(releaseDate.replace("-", "/"));
  }

  @Override
  public void showRatings(String rating) {
    if (rating != null && !rating.isEmpty()) {
      mDetailView.showRatings(String.format(PopularMovies.context.getResources()
          .getString(R.string.format_rating), rating));
    }
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
  public void setController(MovieDetailsContract.MovieDetailsController controller) {
    if (controller != null) mDetailsController = controller;
  }


  @Override
  public void attachView(MovieDetailsContract.View detailsView) {
    mDetailView = detailsView;
  }

  @Override
  public void setTrailerLink(VideosWrapper videosWrapper) {
    if (videosWrapper != null) {
      ArrayList<Video> videos = videosWrapper.videos;
      if (videos != null && !videos.isEmpty()) {
        if (videos.get(0) != null && !videos.get(0).id.isEmpty()) {
          mDetailView.setTrailerLink(videos.get(0).getId());
        }
      }
    }
  }


  @Override
  public void showPoster(String url) {
    if (url != null) mDetailView.showPoster(url);
  }

  @Override
  public void start() {
    mDetailView.showLoading();

    // TODO: delegate this to the controller
    if (checkIfFavorite()) {
      mDetailsController.getFavoriteDetails();
    } else {
      mDetailsController.requestMovieDetails();

    }
  }

  @Override
  public void startDetail(boolean connection) {
    mDetailView.showLoading();

    if (checkIfFavorite()) {
      mDetailsController.getFavoriteDetails();
    } else {
      if (connection) {
        mDetailsController.requestMovieDetails();
      } else {
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
    mMovie = (MovieDetails) o;
    showViews();
  }

  @Override
  public void onError(String e) {
    mDetailView.hideLoading();
    mDetailView.showError();
  }
}
