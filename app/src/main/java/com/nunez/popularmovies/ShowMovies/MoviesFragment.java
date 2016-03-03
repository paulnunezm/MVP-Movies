package com.nunez.popularmovies.ShowMovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.mvp.presenters.RecyclerViewClickListener;
import com.nunez.popularmovies.mvp.views.MoviesView;
import com.nunez.popularmovies.showMovieDetails.MovieDetailActivity;
import com.nunez.popularmovies.views.adapters.MoviesAdapter;

import java.util.ArrayList;

/**
 * Created by paulnunez on 2/25/16.
 */
public class MoviesFragment extends Fragment implements MoviesView, RecyclerViewClickListener {

    public static final String EXTRA_MOVIE_ID = "movie_id";

    private boolean mAutoUpdated;
    private GridLayoutManager mLayoutMangager;
    private MoviesPresenter mMoviesPresenter;
    private MoviesAdapter mAdapter;
    private ProgressBar mProgress;
    private RecyclerView mRecycler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMoviesPresenter = new MoviesPresenter();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container);

        initializeViews(rootView);
        initializeRecyclerView();

        mMoviesPresenter.attachView(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!mAutoUpdated) mMoviesPresenter.start();
    }


    public void initializeRecyclerView(){
        mLayoutMangager = new GridLayoutManager(getContext(),2);
        mRecycler.setLayoutManager(mLayoutMangager);
    }

    public void initializeViews(View view){
        mRecycler         = (RecyclerView) view.findViewById(R.id.recycler);
        mProgress         = (ProgressBar) view.findViewById(R.id.progress);

    }

    @Override
    public void showMovies(ArrayList<Movie> movieList) {
        mAutoUpdated = true;
        mAdapter = new MoviesAdapter(movieList,5);
        mAdapter.setRecyclerListListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void showLoading() {
        mProgress.setVisibility(View.VISIBLE);
        Snackbar.make(((MainActivity) getActivity()).getCoordinatorLayout(),
                "Requesting movies...", Snackbar.LENGTH_LONG).show();
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

    @Override
    public void onClick(View v, int position, float x, float y) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);

        Movie movie = mAdapter.getMovies().get(position);

        Bundle args = new Bundle();
        args.putString(EXTRA_MOVIE_ID, String.valueOf(movie.id));
        intent.putExtra(EXTRA_MOVIE_ID, args);

        startActivity(intent);
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
        public void onItemSelected(Uri dateUri);
    }
}
