package com.nunez.popularmovies.mvp.presenters;

import com.nunez.popularmovies.model.entities.MoviesWrapper;

/**
 * Created by paulnunez on 11/15/15.
 */
public interface PresenterCallback {
    void onSuccess(MoviesWrapper moviesWrapper);
    String onError(String e);
}
