package com.nunez.popularmovies.ShowMovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nunez.popularmovies.R;
import com.nunez.popularmovies.utils.Constants;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_MOVIE_ID = "movie_id";

    private CoordinatorLayout coordinatorLayout;
    private SharedPreferences sortPreferences;
    private SharedPreferences.Editor prefEditor;
    private Spinner spinner;
    private boolean firstTimeOpen = true;

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


        // Initialize the sort preference, and set sort popular
        sortPreferences = getSharedPreferences(Constants.PREFS,0);
        String sortType = sortPreferences.getString(Constants.SORT_POPULAR, null);
        prefEditor = sortPreferences.edit();


        // If no sorting prefs has been set before, set popular sorting as default.
        if (sortType == null) {
            prefEditor.putString(Constants.PREFS_SORT, Constants.SORT_POPULAR);
            prefEditor.apply();
        }


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
        if (id == R.id.action_favorites) {
            //showFavoriteMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public CoordinatorLayout getCoordinatorLayout(){
        return coordinatorLayout;
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
            }

            ((MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies))
                    .refreshMovies();

        }
        firstTimeOpen = false;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
