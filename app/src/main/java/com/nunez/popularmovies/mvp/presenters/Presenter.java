package com.nunez.popularmovies.mvp.presenters;



/**
 * Interface that represents a Presenter in the model view presenter Pattern
 * defines methods to manage the Activity / Fragment lifecycle
 *
 * Grabbed from
 * {https://github.com/saulmm/Material-Movies/blob/master/HackVG/app/src/main/java/com/hackvg/android/mvp/presenters/Presenter.java}
 */
public  interface Presenter {

    /**
     * Called when the presenter is initialized
     */
     void start ();

    /**
     * Called when the presenter is stop, i.e when an activity
     * or a fragment finishes
     */
     void stop ();


}
