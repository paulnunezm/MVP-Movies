package com.nunez.popularmovies.showMovieDetails;

import com.nunez.popularmovies.domain.UseCase;
import com.nunez.popularmovies.model.entities.MovieDetails;
import com.nunez.popularmovies.model.entities.Review;
import com.nunez.popularmovies.model.entities.ReviewsWrapper;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.model.entities.VideosWrapper;
import com.nunez.popularmovies.mvp.views.MVPView;

import java.util.ArrayList;

/**
 * Contract that defines the MVP interfaces for this feature
 */
public interface MovieDetailsContract {

    interface Presenter extends com.nunez.popularmovies.mvp.presenters.Presenter {
        void attachView(View detailsView);

        void setTrailerLink(VideosWrapper videosWrapper);
        void showViews();
        void showPoster(String url);
        void showTitle (String title);
        void showDescription(String description);

        void showTrailers(VideosWrapper trailers);

        void showReviews(ReviewsWrapper reviewsWrapper);
        boolean checkIfFavorite();
        void showReleaseDate();

        void showRatings(String rating);
        void startDetail(boolean connection);

        void saveMovieToDb();
        void removeMovieFromDb();

        void setController(MovieDetailsContract.MovieDetailsController controller);
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

         boolean checkIfFavorite();
         void saveMovieToDb(MovieDetails movie);
         void removeMovieFromDb(String id);
         void getFavoriteDetails();

    }
}
