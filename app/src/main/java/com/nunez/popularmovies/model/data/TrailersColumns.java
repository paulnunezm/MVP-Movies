package com.nunez.popularmovies.model.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by paulnunez on 2/18/16.
 */
public interface TrailersColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @NotNull String MOVIE_ID = "movie_id";
    @DataType(DataType.Type.TEXT) @NotNull String TRAILER_TITLE = "trailer_title";
}