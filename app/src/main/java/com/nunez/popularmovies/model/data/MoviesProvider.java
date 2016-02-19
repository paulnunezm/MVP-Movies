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

//    @TableEndpoint(table = NotesDatabase.LISTS) public static class Lists {
//
//        @ContentUri(
//                path = "lists",
//                type = "vnd.android.cursor.dir/list",
//                defaultSort = ListColumns.TITLE + " ASC")
//        public static final Uri LISTS = Uri.parse("content://" + AUTHORITY + "/lists");

    @TableEndpoint(table = MoviesDatabase.MOVIES) public static class Movies{

        @ContentUri(
                path = MoviesDatabase.MOVIES,
                type = "vnd.android.cursor.dir/movies",
                defaultSort = MoviesColumns.TITLE + " ASC")

        public static final Uri MOVIES = Uri.parse("content://" + AUTHORITY + "/movies");
    }
}
