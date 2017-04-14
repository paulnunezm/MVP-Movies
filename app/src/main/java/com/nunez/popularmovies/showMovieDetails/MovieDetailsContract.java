package com.nunez.popularmovies.showMovieDetails;

import com.nunez.popularmovies.domain.UseCase;
import com.nunez.popularmovies.model.entities.MovieDetails;
import com.nunez.popularmovies.model.entities.Review;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.mvp.views.MVPView;

import java.util.ArrayList;

/**
 * Contract that defines the MVP interfaces for this feature
 */
public interface MovieDetailsContract {

    interface Presenter extends com.nunez.popularmovies.mvp.presenters.Presenter {
        void attachView(View detailsView);
        void setTrailerLink();
        void showViews();
        void showPoster(String url);
        void showTitle (String title);
        void showDescription(String description);
        void showTrailers();
        void showReviews();
        boolean checkIfFavorite();
        void showReleaseDate();
        void showRatings();
        void startDetail(boolean connection);

        void saveMovieToDb();
        void removeMovieFromDb();
    }

    interface View extends MVPView {
        void setTrailerLink(String url);
        void showPoster(String url);
        void showTitle (String title);
        void showDescription(String description);
        void showTrailers(ArrayList<Video> trailers);
        void showReviews(ArrayList<Review> reviews);

        void showMessage(String message);
        void setFavorite();
        void showReleaseDate(String release);
        void showRatings(String rating);
    }

     interface MovieDetailsController extends UseCase {
         void requestMovieDetails();
         void sendMovieDetailsToPresenter(MovieDetails movie);
         boolean checkIfFavorite(String id);
         void saveMovieToDb(MovieDetails movie);
         void removeMovieFromDb(String id);
         void getFavoriteDetails();

    }
}
