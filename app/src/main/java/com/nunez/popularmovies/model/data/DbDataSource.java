package com.nunez.popularmovies.model.data;

import android.content.ContentResolver;
import android.database.Cursor;

import com.nunez.popularmovies.PopularMovies;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.model.entities.MovieDetails;
import com.nunez.popularmovies.model.entities.MoviesWrapper;
import com.nunez.popularmovies.model.entities.Review;
import com.nunez.popularmovies.model.entities.ReviewsWrapper;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.model.entities.VideosWrapper;
import com.nunez.popularmovies.utils.Callbacks;

import java.util.ArrayList;

/**
 * Created by paulnunez on 2/22/16.
 */
public class DbDataSource {

   public static boolean checkIfFavorite(String id){
       Cursor mCursor = PopularMovies.context.getContentResolver().query(
               MoviesProvider.Movies.MOVIES,
               null,
               MoviesColumns.MOVIE_ID + " = " + id,
               null,
               null
       );

       if(mCursor.moveToFirst()) return true;

       return false;
   }


    public static void favoriteMovies(Callbacks.StandarCallback controllerCallback){

        MoviesWrapper moviesWrapper = new MoviesWrapper();
        ArrayList<Movie> movies = new ArrayList<>();

        Cursor mCursor = PopularMovies.context.getContentResolver().query(
            MoviesProvider.Movies.MOVIES,
            null,
            null,
            null,
            null
        );




        while (mCursor != null && mCursor.moveToNext()){
            Movie movie = new Movie();
            movie.setId(mCursor.getString(1));
            movie.setPosertPath(mCursor.getString(2));
            movie.setTitle(mCursor.getString(3));
            movie.setRating(mCursor.getString(6));
            movies.add(movie);

            ArrayList<Integer> genres = new ArrayList<Integer>();

            Cursor genresCursor = PopularMovies.context.getContentResolver().query(
                    MoviesProvider.Genres.Genres,
                    null,
                    MoviesColumns.MOVIE_ID + " = " + String.valueOf(movie.id),
                    null,
                    null
            );

            while (genresCursor != null && genresCursor.moveToNext()){
                genres.add(genresCursor.getInt(1));
            }

            movie.setGenres(genres);
        }

        moviesWrapper.setMovies(movies);

        controllerCallback.onSuccess(moviesWrapper);
    }

    public static void getFavoriteDetails(Callbacks.StandarCallback controllerCallback, String id){

        ContentResolver resolver  = PopularMovies.context.getContentResolver();

        MovieDetails movie = new MovieDetails();
        VideosWrapper videosWrapper = new VideosWrapper();
        ReviewsWrapper reviewsWrapper = new ReviewsWrapper();
        ArrayList<Review> reviews = new ArrayList<>();

        try {
            Cursor movieCursor = resolver.query(
                    MoviesProvider.Movies.MOVIES,
                    null,
                    MoviesColumns.MOVIE_ID + " = " + id,
                    null,
                    null
            );

            if(movieCursor.getCount() > 0){
                movieCursor.moveToFirst();
                movie.setId(movieCursor.getString(1));
                movie.setPosertPath(movieCursor.getString(2));
                movie.setTitle(movieCursor.getString(3));
                movie.setDescription(movieCursor.getString(4));
                movie.setReleaseDate(movieCursor.getString(5));
                movie.setRating(movieCursor.getString(6));

                videosWrapper = getFavoriteTrailers(resolver, id);
                reviewsWrapper = getFavoriteReviews(resolver, id);

                movie.setVideosWrapper(videosWrapper);
                movie.setReviewsWrapper(reviewsWrapper);

                controllerCallback.onSuccess(movie);
            }

            movieCursor.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private static VideosWrapper getFavoriteTrailers(ContentResolver resolver, String id){
        VideosWrapper wrapper = new VideosWrapper();
        ArrayList<Video> videos  = new ArrayList<>();

        Cursor trailerCursor = resolver.query(
                MoviesProvider.Trailers.Trailers,
                null,
                MoviesColumns.MOVIE_ID + " = " + id,
                null,
                null
        );

        while (trailerCursor.moveToNext()){
            Video video = new Video();

            video.setName(trailerCursor.getString(1));
            video.setId(trailerCursor.getString(2));
            video.setSite(trailerCursor.getString(3));

            videos.add(video);
        }

        trailerCursor.close();
        wrapper.setVideos(videos);

        return wrapper;
    }

    private static ReviewsWrapper getFavoriteReviews(ContentResolver resolver, String id){

        ReviewsWrapper wrapper = new ReviewsWrapper();
        ArrayList<Review> reviews = new ArrayList<>();

        String[] columns = {ReviewsColumns.AUTHOR, ReviewsColumns.CONTENT, ReviewsColumns.URL};

//        KEY_ROOM + "= '" + r + "'"
        Cursor reviewCursor = resolver.query(
                MoviesProvider.Reviews.Reviews,
                columns,
                MoviesColumns.MOVIE_ID + " = " + Integer.valueOf(id),
                null,
                null
        );

        while (reviewCursor.moveToNext()){
            Review review = new Review();

            review.setAuthor(reviewCursor.getString(0));
            review.setContent(reviewCursor.getString(1));
            review.setUrl(reviewCursor.getString(2));
            reviews.add(review);
        }

        reviewCursor.close();

        wrapper.setReviews(reviews);
        return wrapper;
    }

}
