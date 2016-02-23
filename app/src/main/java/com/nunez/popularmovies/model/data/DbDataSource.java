package com.nunez.popularmovies.model.data;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by paulnunez on 2/22/16.
 */
public class DbDataSource {

   public static boolean checkIfFavorite(Context context, String id){
       Cursor mCursor = context.getContentResolver().query(
               MoviesProvider.Movies.MOVIES,
               null,
               MoviesColumns.MOVIE_ID + " = " + id,
               null,
               null
       );

       if(mCursor.moveToFirst()) return true;

       return false;
   }

}
