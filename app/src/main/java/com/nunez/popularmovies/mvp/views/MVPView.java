package com.nunez.popularmovies.mvp.views;

/**
 * Created by paulnunez on 11/15/15.
 */
public interface MVPView {
    android.content.Context getContext();

    void showLoading ();

    void hideLoading ();

    void showLoadingLabel();

    void hideActionLabel ();

    void showError();
}
