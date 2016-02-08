package com.nunez.popularmovies.utils;

import java.util.Objects;

/**
 * Created by paulnunez on 2/3/16.
 */
public class Callbacks {

    public interface StandarCallback{
        void onSuccess(Object o);
        void onError(String e);
    }
}
