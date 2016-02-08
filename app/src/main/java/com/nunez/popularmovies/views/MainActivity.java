package com.nunez.popularmovies.views;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.mvp.presenters.MoviesPresenter;
import com.nunez.popularmovies.mvp.presenters.RecyclerViewClickListener;
import com.nunez.popularmovies.mvp.views.MoviesView;
import com.nunez.popularmovies.showMovieDetails.MovieDetailActivity;
import com.nunez.popularmovies.views.adapters.MoviesAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesView, RecyclerViewClickListener {

    private Snackbar.Callback snackCallback;
    private TextView text;
    private CoordinatorLayout coordinatorLayout;
    private MoviesPresenter mMoviesPresenter;
    private Context mContext;
    private RecyclerView mRecycler;
    private MoviesAdapter mAdapter;
    private GridLayoutManager mLayoutMangager;
    private ProgressBar mProgress;
    private Toolbar toolbar;
    private boolean mAutoUpdated;
    public static final String EXTRA_MOVIE_ID = "movie_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeToolbar();
        initializeRecyclerView();

        mAutoUpdated = false; // Checks if already updated without user interaction.
        mMoviesPresenter = new MoviesPresenter();

        if (savedInstanceState == null)
            mMoviesPresenter.attachView(this);

//        else
//            initializeFromParams(savedInstanceState);

//        if (android.os.Build.VERSION.SDK_INT >= 19)

    }

    private void initializeToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    public void initializeRecyclerView(){
        mLayoutMangager = new GridLayoutManager(getContext(),2);
        mRecycler.setLayoutManager(mLayoutMangager);
    }

    public void initializeViews(){
        mContext          = this.getContext();
        toolbar           = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mRecycler         = (RecyclerView) findViewById(R.id.recycler);
        mProgress         = (ProgressBar) findViewById(R.id.progress);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!mAutoUpdated) mMoviesPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMovies(ArrayList<Movie> movieList) {
        mAutoUpdated = true;
        mAdapter = new MoviesAdapter(movieList,5);
        mAdapter.setRecyclerListListener(MainActivity.this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void showLoading() {
        mProgress.setVisibility(View.VISIBLE);
        Snackbar.make(coordinatorLayout, "Requesting movies...", Snackbar.LENGTH_LONG).show();
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
        return mContext;
    }

    @Override
    public void onClick(View v, int position, float x, float y) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);

        Movie movie = mAdapter.getMovies().get(position);

        Bundle args = new Bundle();
        args.putString(EXTRA_MOVIE_ID, String.valueOf(movie.id));
        intent.putExtra(EXTRA_MOVIE_ID, args);

        startActivity(intent);
    }
}
