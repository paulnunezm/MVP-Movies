package com.nunez.popularmovies.ShowMovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nunez.popularmovies.R;
import com.nunez.popularmovies.showMovieDetails.MovieDetailActivity;
import com.nunez.popularmovies.showMovieDetails.MovieDetailFragment;
import com.nunez.popularmovies.utils.Constants;

import static android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener,
    MoviesFragment.Callback{
    public static final  String EXTRA_MOVIE_ID   = "movie_id";
    public static final  String SPINNER_POSITION = "spinnerPosition";
    private static final String TAG              = MainActivity.class.getSimpleName();
    public  int                      currentSpinnerPos;
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferences sortPreferences;
    private SharedPreferences.Editor prefEditor;
    private Spinner spinner;
    private boolean firstTimeOpen = true;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        spinner           = (Spinner) findViewById(R.id.spinner_sort);

        spinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movies_sort_array, R.layout.item_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.item_spinner);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        sortPreferences = getSharedPreferences(Constants.PREFS,0);
        prefEditor = sortPreferences.edit();

        if(savedInstanceState == null){

            // Initialize the sort preference, and set sort popular
            String sortType = sortPreferences.getString(Constants.SORT_POPULAR, null);
            currentSpinnerPos  = 0;

            // If no sorting prefs has been set before, set popular sorting as default.
            if (sortType == null) {
                prefEditor.putString(Constants.PREFS_SORT, Constants.SORT_POPULAR);
                prefEditor.apply();
            }
        }

        /** Check if two pane layout**/
        if(findViewById(R.id.movie_detail_container) != null){
            mTwoPane = true;

//            if(savedInstanceState == null){
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.movie_detail_container,
//                                new MovieDetailFragment(), MovieDetailFragment.FRAGMENT_TAG)
//                        .commit();
//            }

        }else{
            mTwoPane = false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: s");
        outState.putInt(SPINNER_POSITION, currentSpinnerPos);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.d(TAG, "onRestoreInstanceState: 2");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentSpinnerPos = (int) savedInstanceState.get(SPINNER_POSITION);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!firstTimeOpen){

            switch (position){
                case 0: // pop movies
                    prefEditor.putString(Constants.PREFS_SORT, Constants.SORT_POPULAR);
                    prefEditor.apply();
                    break;

                case 1: // top rated
                    prefEditor.putString(Constants.PREFS_SORT, Constants.SORT_RATING);
                    prefEditor.apply();
                    break;

                case 2:
                    prefEditor.putString(Constants.PREFS_SORT, Constants.SORT_FAVORITES);
                    prefEditor.apply();
            }

            if(currentSpinnerPos != position){
                currentSpinnerPos = position;
                ((MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies))
                        .refreshMovies();
            }

        }
        firstTimeOpen = false;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onItemSelected(View poster, String movieId) {
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putString(MovieDetailFragment.MOVIE_ID, movieId);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        }else{
            Intent intent = new Intent(this, MovieDetailActivity.class);
            Bundle args = new Bundle();
            args.putString(EXTRA_MOVIE_ID, String.valueOf(movieId));
            intent.putExtra(EXTRA_MOVIE_ID, args);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this); //poster, "poster"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onMoviesRecieved(String movieId) {
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putString(MovieDetailFragment.MOVIE_ID, movieId);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onResumeFragment() {

        // Always refresh favorites to check if there was a change while this fragment was yet alive.
        // This could also be fixed adding a cursorLoader on the fragment if on favorite.
        if( currentSpinnerPos == 2 ){
            ((MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies))
                    .refreshMovies();
        }
    }
}
