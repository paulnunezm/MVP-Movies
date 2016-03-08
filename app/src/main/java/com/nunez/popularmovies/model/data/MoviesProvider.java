package com.nunez.popularmovies.model.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by paulnunez on 2/18/16.
 */
@ContentProvider(authority = MoviesProvider.AUTHORITY, database = MoviesDatabase.class)
public class MoviesProvider {
    public static final String AUTHORITY = "com.nunez.popularmovies.model.data.MoviesProvider";

    interface Path {
        String MOVIES = "movies";
        String TRAILERS = "trailers";
        String REVIEWS = "reviews";
    }


    @TableEndpoint(table = MoviesDatabase.MOVIES)
    public static class Movies {

        @ContentUri(
                path = MoviesDatabase.MOVIES,
                type = "vnd.android.cursor.dir/movies",
                defaultSort = MoviesColumns.TITLE + " ASC")
        public static final Uri MOVIES = Uri.parse("content://" + AUTHORITY + "/movies");

//        @InexactContentUri(
//                path = Path.MOVIES + "/#",
//                name = "MOVIE_ID",
//                type = "vnd.android.cursor.item/movies",
//                whereColumn = MoviesColumns.MOVIE_ID,
//                pathSegment = 1)
//
//        public static Uri withMovieId(String id) {
//            return Uri.parse("content://" + AUTHORITY + "/"+Path.MOVIES+"/" + id);
//        }

//        public static String getMovieId(String)
    }

//        @TableEndpoint(table = MoviesDatabase.TRAILERS) public static class Trailers {
//            @ContentUri(
//                    path = MoviesDatabase.TRAILERS + "/#",
//                    type = "vnd.android.cursor.item/movies",
//                    defaultSort = TrailersColumns.TITLE + " ASC",
//                    join = MoviesColumns.MOVIE_ID)
//            public static Uri withMovieId(String id) {
//                return Uri.parse("content://" + AUTHORITY + "/"+Path.MOVIES+"/" + id);
//            }
//        }

    @TableEndpoint(table = MoviesDatabase.TRAILERS)
    public static class Trailers {

        @ContentUri(
                path = MoviesDatabase.TRAILERS,
                type = "vnd.android.cursor.dir/trailers",
                defaultSort = MoviesColumns.TITLE + " ASC")
        public static final Uri Trailers = Uri.parse("content://" + AUTHORITY + "/"+Path.TRAILERS);
    }

    @TableEndpoint(table = MoviesDatabase.REVIEWS)
    public static class Reviews {

        @ContentUri(
                path = MoviesDatabase.REVIEWS,
                type = "vnd.android.cursor.dir/reviews",
                defaultSort = MoviesColumns.TITLE + " ASC")
        public static final Uri Reviews = Uri.parse("content://" + AUTHORITY + "/"+Path.REVIEWS);
    }
}
