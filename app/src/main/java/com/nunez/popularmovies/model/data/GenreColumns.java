package com.nunez.popularmovies.model.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;

/**
 * Created by paulnunez on 3/10/16.
 */
public interface GenreColumns {
    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";
    @DataType(INTEGER) @NotNull String GENRE = "genre";

    // Declaring one to many relation
    @DataType(INTEGER) @References(table = MoviesDatabase.TRAILERS,
            column = TrailersColumns.MOVIE_ID) String MOVIE_ID = "movie_id";
}
