package com.nunez.popularmovies.model.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.BLOB;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by paulnunez on 2/18/16.
 */
public interface MoviesColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(INTEGER) @NotNull String MOVIE_ID = "movie_id";
    @DataType(BLOB) String POSTER = "poster";
    @DataType(TEXT) @NotNull String TITLE = "title";
    @DataType(TEXT) String DESCRIPTION = "description";
    @DataType(TEXT) String RELEASE = "release_date";
    @DataType(TEXT) String RATING = "rating";
    @DataType(TEXT) String GENRE = "genre";



    // Declaring one to many relation
//    @DataType(INTEGER) @References(table = MoviesDatabase.TRAILERS,
//            column = TrailersColumns.MOVIE_ID) String TRAILERS_ID = "trailers_id";


}

