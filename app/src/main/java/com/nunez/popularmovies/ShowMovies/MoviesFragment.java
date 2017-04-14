package com.nunez.popularmovies.ShowMovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.mvp.presenters.RecyclerViewClickListener;
import com.nunez.popularmovies.mvp.views.MoviesView;
import com.nunez.popularmovies.views.adapters.MoviesAdapter;
import com.nunez.popularmovies.views.custom_views.AutofitRecyclerView;

import java.util.ArrayList;

/**
 * Created by paulnunez on 2/25/16.
 */
public class MoviesFragment extends Fragment implements MoviesView, RecyclerViewClickListener {

    public static final String TAG = MoviesFragment.class.getSimpleName();
    public static final String EXTRA_MOVIE_ID = "movie_id";

    private boolean mAutoUpdated;
    private GridLayoutManager mLayoutMangager;
    private MoviesPresenter mMoviesPresenter;
    private MoviesAdapter mAdapter;
    private ProgressBar mProgress;
    private AutofitRecyclerView mRecycler;
    private FrameLayout mNoMovies;
    private ConnectivityManager connectivityManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoviesPresenter = new MoviesPresenter();
        mMoviesPresenter.attachView(this);
        setHasOptionsMenu(true);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container);

        initializeViews(rootView);
        initializeRecyclerView();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: ");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState == null){
            if(isNetworkConnected()){
                mMoviesPresenter.requestMovies();
            }else{
                showNoMovies();
                showConnectionError();
            }
        }else{
            mMoviesPresenter.setMovieList((ArrayList<Movie>) savedInstanceState.getSerializable("movies"));
            mMoviesPresenter.showMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("movies", mMoviesPresenter.getMovieList());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mMoviesPresenter.start();
        setHasOptionsMenu(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        initializeRecyclerView();
        ((Callback) getActivity()).onResumeFragment();
    }


    public void initializeRecyclerView(){

        int columns = 2;

//        mLayoutMangager = new GridLayoutManager(getContext(),2);
//        mRecycler.setLayoutManager(mLayoutMangager);
    }

    public void initializeViews(View view){
        mRecycler         = (AutofitRecyclerView) view.findViewById(R.id.recycler);
        mProgress         = (ProgressBar) view.findViewById(R.id.progress);
        mNoMovies         = (FrameLayout) view.findViewById(R.id.no_movies);
    }

    public void refreshMovies(){
       if(isNetworkConnected() || ((MainActivity) getActivity()).currentSpinnerPos == 2 ){
           mMoviesPresenter.requestMovies();
       }else{
           showNoMovies();
           showConnectionError();
       }
    }

    @Override
    public void showMovies(ArrayList<Movie> movieList) {
        mRecycler.setVisibility(View.VISIBLE);
        mNoMovies.setVisibility(View.GONE);
        mAutoUpdated = true;
        mAdapter = new MoviesAdapter(movieList,5);
        mAdapter.setRecyclerListListener(this);
        mRecycler.setAdapter(mAdapter);

        ((Callback) getActivity()).onMoviesRecieved(movieList.get(0).getId());
    }

    @Override
    public void showNoMovies() {
        if(mAdapter != null) mAdapter.clearData();
        mNoMovies.setVisibility(View.VISIBLE);
    }

    public void showConnectionError(){
        Snackbar.make(mNoMovies, getResources()
                .getString(R.string.error_connection), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        mNoMovies.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingLabel() {

    }

    @Override
    public void hideActionLabel() {

    }

    @Override
    public void showError() {

    }

    @Override
    public boolean isTheListEmpty() {
        return false;
    }

    @Override
    public void appendMovies(ArrayList<Movie> movieList) {

    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View v, int position, float x, float y) {

        Movie movie = mAdapter.getMovies().get(position);

        ((Callback) getActivity()).onItemSelected(v, String.valueOf(movie.id));
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(View poster, String movieId);
        void onMoviesRecieved(String movieId);
        void onResumeFragment();
    }


}
