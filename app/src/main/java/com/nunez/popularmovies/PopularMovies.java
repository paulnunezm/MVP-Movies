package com.nunez.popularmovies;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by paulnunez on 11/14/15.
 */
public class PopularMovies extends Application {

//    public static  String API_KEY;



    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build());

//        API_KEY = getResources().getString(R.string.theMovieDbApiKey);


    }

//    public static String getApiKey(){
//        return API_KEY;
//    }
}
