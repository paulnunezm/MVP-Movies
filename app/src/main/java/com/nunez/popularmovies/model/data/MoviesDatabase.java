package com.nunez.popularmovies.model.data;

import net.simonvt.schematic.annotation.Table;


/**
 * Created by paulnunez on 2/18/16.
 */
@net.simonvt.schematic.annotation.Database(
        version =  MoviesDatabase.VERSION)
public final class MoviesDatabase {
    public static final int VERSION = 7;

    @Table(MoviesColumns.class) public static final String MOVIES = "movies";
    @Table(TrailersColumns.class) public static final String TRAILERS = "trailers";
    @Table(ReviewsColumns.class) public static final String REVIEWS = "reviews";
    @Table(GenreColumns.class) public static final String GENRES = "genres";



}
