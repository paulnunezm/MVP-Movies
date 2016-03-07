package com.nunez.popularmovies;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by paulnunez on 11/14/15.
 */
public class PopularMovies extends Application {

    public static  Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build());

    }
}
