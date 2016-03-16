package com.nunez.popularmovies.showMovieDetails;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nunez.popularmovies.PopularMovies;
import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.Review;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.utils.Constants;
import com.nunez.popularmovies.views.adapters.ReviewsAdapter;
import com.nunez.popularmovies.views.adapters.TrailersAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by paulnunez on 3/9/16.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailsContract.View,
    View.OnClickListener{

    private static String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    public static String MOVIE_ID = "movie_id";
    public static String FRAGMENT_TAG = LOG_TAG;

    private String mMovieId;
    private String mTrailerUrl;
    private boolean isFavorite;

    private MovieDetailsPresenter mDetailPresenter;
    private TrailersAdapter mTrailersAdapter;
    private LinearLayoutManager mReviewsLayoutManager;
    private LinearLayoutManager mTrailersLayoutManager;
    private ReviewsAdapter mReviewsAdapter;

    @Bind(R.id.image_poster)             ImageView mPoster;
    @Bind(R.id.text_title_bgnd)          View mTitleBackground;
    @Bind(R.id.details_scrollView)       NestedScrollView mScrollView;
    @Bind(R.id.text_title)               TextView mTitle;
    @Bind(R.id.text_description)         TextView mDescription;
    @Bind(R.id.text_description_title)   TextView mDescriptionTitle;
    @Bind(R.id.text_trailers)            TextView mTrailersTitle;
    @Bind(R.id.text_reviews)             TextView mReviewsTitle;
    @Bind(R.id.text_release)             TextView mReleaseDate;
    @Bind(R.id.text_rating)              TextView mRatings;
    @Nullable @Bind(R.id.toolbar)        Toolbar toolbar;
    @Bind(R.id.recyler_trailers)         RecyclerView mTrailersRecycleView;
    @Bind(R.id.recyler_reviews)          RecyclerView mReviewsRecyclerView;
    @Bind(R.id.button_fab)               ImageButton fab;
    @Bind(R.id.progress)                 ProgressBar mProgress;
    @Bind(R.id.container)                View mDetailsContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Bundle args = getArguments();

        if(args != null){
            mMovieId = args.getString(MOVIE_ID);
        }

        ButterKnife.bind(this, rootView);
        initalizeViews(rootView);

        mDetailPresenter = new MovieDetailsPresenter(mMovieId);
        mDetailPresenter.attachView(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDetailPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void initalizeViews(View v){
        v.findViewById(R.id.actio_play_trailer).setOnClickListener(this);
        fab.setOnClickListener(this);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(toolbar != null){
            toolbar.setTitle("");
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }



    @Override
    public void setTrailerLink(String url) {
        mTrailerUrl = url;
    }

    @Override
    public void showPoster(String url) {
        Glide.with(PopularMovies.context).
                load(Constants.POSTER_BASE_URL + url)
                .centerCrop()
                .placeholder(PopularMovies.context.getResources().getColor(R.color.movie_placeholder))
                .error(PopularMovies.context.getDrawable(R.drawable.ic_trailers))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        setColors(((GlideBitmapDrawable) resource).getBitmap());

                        return false;
                    }
                })
                .into(mPoster);
    }

    @Override
    public void showTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void showDescription(String description) {
        mDescription.setText(description);
    }

    @Override
    public void showTrailers(ArrayList<Video> trailers) {
        if(trailers != null & trailers.size()>0){
            mTrailersAdapter = new TrailersAdapter(trailers);
            mTrailersLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mTrailersRecycleView.setLayoutManager(mTrailersLayoutManager);
            mTrailersRecycleView.setHasFixedSize(true);
            mTrailersRecycleView.setAdapter(mTrailersAdapter);
            mTrailersRecycleView.setNestedScrollingEnabled(false);
        }else{
            mTrailersTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void showReviews(ArrayList<Review> reviews) {
        if(reviews!=null && reviews.size()>0 ){
            mReviewsAdapter = new ReviewsAdapter(reviews);
            mReviewsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
            mReviewsRecyclerView.setAdapter(mReviewsAdapter);
            mTrailersRecycleView.setNestedScrollingEnabled(false);
        }else{
            mReviewsTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void setFavorite() {
        isFavorite = true;
        fab.setBackgroundResource(R.drawable.fab);
    }

    @Override
    public void showReleaseDate(String release) {
        mReleaseDate.setText(release);
    }

    @Override
    public void showRatings(String rating) {
        mRatings.setText(rating);
    }

    @Override
    public void showLoading() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgress.setVisibility(View.GONE);
        mDetailsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingLabel() {

    }

    @Override
    public void hideActionLabel() {
        mProgress.setVisibility(View.GONE);
        mDetailsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.actio_play_trailer){
            playTrailer();

        }else if(id == R.id.button_fab){

            if(!isFavorite){
                isFavorite = true;
                mDetailPresenter.saveMovieToDb();
                animateFavorite();
            }else{
                animateFavoritePulse();
            }

        }
    }

    public void setColors(Bitmap bitmap){

        if (bitmap != null) {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                @Override
                public void onGenerated(Palette palette) {

                    if (palette != null) {
                        Palette.Swatch vibrantDarkSwatch = palette.getDarkVibrantSwatch();

                        try {
                            int color = vibrantDarkSwatch.getRgb(); // for the status bar.
                            int alphaColor = Color.argb(170, Color.red(color), Color.green(color), Color.blue(color));
                            //int textColor = mutedLightSwatch.getBodyTextColor();

                            // Set awesome colors to texts and backgrounds

                            //mScrollView.setBackgroundColor(mutedLightSwatch.getRgb());
                            mTitleBackground.setBackgroundColor(alphaColor);
                            toolbar.setBackgroundColor(alphaColor);
                            if(isFavorite){
                                fab.setColorFilter(0xFFF);
                            }else{
                                fab.setColorFilter(color);
                            }
//                            mDescriptionTitle.setTextColor(textColor);//vibrantSwatchTitleTextColor);
//                            mDescription.setTextColor(textColor);
//                            mTrailersTitle.setTextColor(textColor);
//                            mReviewsTitle.setTextColor(textColor);

                            // Set awesome drawable colors
//                            Drawable[] drawables = mDescriptionTitle.getCompoundDrawables();
//                            drawables[0].setColorFilter(textColor, PorterDuff.Mode.MULTIPLY);

//                            Drawable[] drawableTrailersTitle = mTrailersTitle.getCompoundDrawables();
//                            drawableTrailersTitle[0].setColorFilter(textColor, PorterDuff.Mode.MULTIPLY);
//                            Drawable[] drawableReviewsTitle = mReviewsTitle.getCompoundDrawables();
//                            drawableReviewsTitle[0].setColorFilter(textColor, PorterDuff.Mode.MULTIPLY);

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                changeStatusBarColor(color);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void changeStatusBarColor(int color){
        Window window = getActivity().getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(color);
    }

    public void animateFavoritePulse(){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.3f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.3f);
        ObjectAnimator scaleAnim = ObjectAnimator.ofPropertyValuesHolder(fab, pvhX, pvhY);

        scaleAnim.setDuration(500);
        scaleAnim.setRepeatCount(1);
        scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnim.start();
    }

    public Context getContext(){
        return  getActivity();
    }

    public void animateFavorite(){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.3f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.3f);
        ObjectAnimator scaleAnim = ObjectAnimator.ofPropertyValuesHolder(fab, pvhX, pvhY);
        //scaleAnim.setInterpolator(new BounceInterpolator());
        scaleAnim.setDuration(500);
        scaleAnim.setRepeatCount(1);
        scaleAnim.setRepeatMode(ValueAnimator.REVERSE);

        //Let's change background's color to red.
        Drawable[] color = {fab.getBackground(),
                getContext().getResources().getDrawable(R.drawable.fab)};
        TransitionDrawable trans = new TransitionDrawable(color);

        //This will work also on old devices. The latest API says you have to use setBackground instead.
        fab.setBackgroundDrawable(trans);
        trans.startTransition(700);

        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(fab, View.ROTATION, 720);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setDuration(1400);

        ObjectAnimator heartColorAnim = ObjectAnimator.ofInt(fab, "colorFilter", getResources().getColor(R.color.white));
        heartColorAnim.setDuration(700).setStartDelay(700);

        AnimatorSet setAnim = new AnimatorSet();
        setAnim.play(scaleAnim).with(rotateAnim).with(heartColorAnim);
        setAnim.start();
    }

    public void playTrailer(){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="
                + mTrailerUrl));

        // Verify that the intent will resolve to an activity
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivity(intent);
        }else{
            showError();
        }
    }
}
