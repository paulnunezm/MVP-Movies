package com.nunez.popularmovies.showMovieDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;

import com.nunez.popularmovies.R;
import com.nunez.popularmovies.ShowMovies.MainActivity;

/**
 * Created by paulnunez on 12/7/15.
 */
public class MovieDetailActivity extends AppCompatActivity{

    private static String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    private String mMovieId;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mContext = getApplicationContext();
        Bundle args = getIntent().getBundleExtra(MainActivity.EXTRA_MOVIE_ID);

        if(args != null) {
            mMovieId  = args.getString(MainActivity.EXTRA_MOVIE_ID);

            Bundle bundle = new Bundle();
            bundle.putString(MovieDetailFragment.MOVIE_ID, mMovieId);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(slide);
            getWindow().setExitTransition(new Explode());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Context getContext() {
        return mContext;
    }


}
