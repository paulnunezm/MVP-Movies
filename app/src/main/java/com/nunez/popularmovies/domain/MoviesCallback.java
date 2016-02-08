package com.nunez.popularmovies.domain;

import com.nunez.popularmovies.model.entities.MoviesWrapper;

/**
 * Created by paulnunez on 11/15/15.
 */
public interface MoviesCallback {

     void onSuccess(MoviesWrapper moviesWrapper);
     String onError(String e);
}
