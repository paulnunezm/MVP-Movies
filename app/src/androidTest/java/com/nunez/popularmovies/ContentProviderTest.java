package com.nunez.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.nunez.popularmovies.model.data.MoviesColumns;
import com.nunez.popularmovies.model.data.MoviesProvider;
import com.nunez.popularmovies.model.data.TrailersColumns;

import java.util.Map;
import java.util.Set;

/**
 * Created by paulnunez on 2/19/16.
 */
public class ContentProviderTest extends AndroidTestCase{
    private static final String LOG_TAG = ContentProviderTest.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        getContext().getContentResolver().delete(MoviesProvider.Movies.MOVIES,null, null);
    }

    public void TestMovieWithTrailer(){

        ContentValues values = new ContentValues();
        values.put(MoviesColumns.MOVIE_ID, "124");
        values.put(MoviesColumns.TITLE, "23");

        Uri createdMovieUri;
        ContentResolver contentResolver = getContext().getContentResolver();

        createdMovieUri = contentResolver.insert(
                MoviesProvider.Movies.MOVIES,
                values);

        Cursor movieCurosr = contentResolver.query(
                createdMovieUri,
                null,
                null,
                null,
                null
        );


        System.out.println(movieCurosr);

        validateCursor("TestMovieWithTrailer", movieCurosr, values);
//        movieCurosr.moveToFirst();

        ContentValues trailerValues = new ContentValues();
        values.put(TrailersColumns.MOVIE_ID, "124");
        values.put(TrailersColumns.TITLE, "myTrailer");
        values.put(TrailersColumns.TRAILER_ID, "1ijndspdoafk");
        values.put(TrailersColumns.SITE, "YouTube");

        Uri createdTrailerUri;
        ContentResolver contentTrailerResolver = getContext().getContentResolver();

        createdTrailerUri = contentTrailerResolver.insert(
                MoviesProvider.Movies.MOVIES,
                values);

        Cursor trailerCursor = contentResolver.query(
                createdMovieUri,
                null,
                null,
                null,
                null
        );


        validateCursor("TestMovieWithTrailer", trailerCursor, trailerValues);

        assertEquals(trailerCursor.getString(5), "124");

         movieCurosr = contentResolver.query(
                createdMovieUri,
                null,
                null,
                null,
                null
        );

        if(movieCurosr.moveToFirst()){
            for (int i = 0; i < movieCurosr.getColumnCount(); i++) {
                Log.d("TEST", movieCurosr.getString(i));
            }
        }
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }
}
