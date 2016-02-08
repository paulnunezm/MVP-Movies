package com.nunez.popularmovies.model.restApi;

/**
 * Created by paulnunez on 11/15/15.
 */
public interface RestDataSource extends MediaDataSource{

    void getMoviesByPage(int page);
}
