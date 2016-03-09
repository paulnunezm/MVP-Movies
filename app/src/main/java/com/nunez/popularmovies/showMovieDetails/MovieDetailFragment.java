package com.nunez.popularmovies.showMovieDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nunez.popularmovies.model.entities.Review;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.views.adapters.ReviewsAdapter;
import com.nunez.popularmovies.views.adapters.TrailersAdapter;

import java.util.ArrayList;

/**
 * Created by paulnunez on 3/9/16.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailsContract.View,
    View.OnClickListener{

    private static String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private String mMovieId;
    private String mTrailerUrl;
    private boolean isFavorite;

    private Context mContext;
    private MovieDetailsContract.Presenter mDetailPresenter;

    private ImageView mPoster;
    private View mTitleBackground;
    private NestedScrollView mScrollView;
    private LinearLayoutManager mTrailersLayoutManager;
    private TextView mTitle;
    private TextView mDescription;
    private TextView mDescriptionTitle;
    private TextView mTrailersTitle;
    private TextView mReviewsTitle;
    private TextView mReleaseDate;
    private TextView mRatings;
    private Toolbar toolbar;
    private TrailersAdapter mTrailersAdapter;
    private LinearLayoutManager mReviewsLayoutManager;
    private RecyclerView mTrailersRecycleView;
    private RecyclerView mReviewsRecyclerView;
    private ReviewsAdapter mReviewsAdapter;
    private ImageButton fab;
    private ProgressBar mProgress;
    private View mDetailsContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 3/9/16 getArguments

        MovieDetailsPresenter mDetailPresenter = new MovieDetailsPresenter(mMovieId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void setTrailerLink(String url) {

    }

    @Override
    public void showPoster(String url) {

    }

    @Override
    public void showTitle(String title) {

    }

    @Override
    public void showDescription(String description) {

    }

    @Override
    public void showTrailers(ArrayList<Video> trailers) {

    }

    @Override
    public void showReviews(ArrayList<Review> reviews) {

    }

    @Override
    public void setFavorite() {

    }

    @Override
    public void showReleaseDate(String release) {

    }

    @Override
    public void showRatings(String rating) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
    public void onClick(View v) {

    }
}
