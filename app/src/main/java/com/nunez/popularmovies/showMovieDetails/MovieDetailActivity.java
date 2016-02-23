package com.nunez.popularmovies.showMovieDetails;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.data.MoviesColumns;
import com.nunez.popularmovies.model.data.MoviesProvider;
import com.nunez.popularmovies.model.entities.Review;
import com.nunez.popularmovies.model.entities.Video;
import com.nunez.popularmovies.utils.Constants;
import com.nunez.popularmovies.ShowMovies.MainActivity;
import com.nunez.popularmovies.views.adapters.ReviewsAdapter;
import com.nunez.popularmovies.views.adapters.TrailersAdapter;
import com.nunez.popularmovies.views.custom_views.MyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by paulnunez on 12/7/15.
 */
public class MovieDetailActivity extends Activity implements MovieDetailsContract.View,
        View.OnClickListener{

    private static String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    private String mMovieId;
    private String mTrailerUrl;
    private boolean isFavorite;

    private Context mContext;
    private MovieDetailsContract.Presenter mDetailPresenter;
    private TextView mTitle;
    private TextView mDescription;
    private TextView mDescriptionTitle;
    private TextView mTrailersTitle;
    private TextView mReviewsTitle;
    private ImageView mPoster;
    private View mTitleBackground;
    private NestedScrollView mScrollView;
    private RecyclerView mTrailersRecycleView;
    private MyLinearLayoutManager mTrailersLayoutManager;
    private TrailersAdapter mTrailersAdapter;
    private MyLinearLayoutManager mReviewsLayoutManager;
    private ReviewsAdapter mReviewsAdapter;
    private RecyclerView mReviewsRecyclerView;
    private ImageButton fab;
    private ProgressBar mProgress;
    private View mDetailsContainer;

    @Override @TargetApi (Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mContext = getApplicationContext();
        Bundle args = getIntent().getBundleExtra(MainActivity.EXTRA_MOVIE_ID);

        if(args != null) {
            mMovieId  = args.getString(MainActivity.EXTRA_MOVIE_ID);
        }

        mDetailPresenter = new MovieDetailsPresenter(mMovieId);
        mDetailPresenter.attachView(this);

        initializeViews();
//        prepareRecylers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDetailPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initializeViews(){
        mScrollView = (NestedScrollView) findViewById(R.id.details_scrollView);
        mPoster = (ImageView) findViewById(R.id.image_poster);
        mTitleBackground = findViewById(R.id.text_title_bgnd);
        mTitle = (TextView) findViewById(R.id.text_title);
        mDescriptionTitle = (TextView) findViewById(R.id.text_description_title);
        mDescription = (TextView) findViewById(R.id.text_description);
        mTrailersTitle = (TextView) findViewById(R.id.text_trailers);
        mTrailersRecycleView = (RecyclerView) findViewById(R.id.recyler_trailers);
        mReviewsTitle = (TextView) findViewById(R.id.text_reviews);
        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.recyler_reviews);
        fab = (ImageButton) findViewById(R.id.button_fab);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mDetailsContainer = findViewById(R.id.container);

        findViewById(R.id.actio_play_trailer).setOnClickListener(this);
        fab.setOnClickListener(this);

//        fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
    }


    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showLoading() {

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

    }

    @Override
    public void showError() {

    }

    @Override
    public void setTrailerLink(String url) {
        mTrailerUrl = url;
    }

    @Override
    public void showPoster(String url) {
        Glide.with(getContext()).
                load(Constants.POSTER_BASE_URL + url)
                .centerCrop()
                .placeholder(getContext().getResources().getColor(R.color.movie_placeholder))
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

    public void setColors(Bitmap bitmap){

        if (bitmap != null) {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                @Override
                public void onGenerated(Palette palette) {

                    if (palette != null) {
                        Palette.Swatch vibrantDarkSwatch = palette.getDarkVibrantSwatch();
                        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                        Palette.Swatch mutedLightSwatch = palette.getLightMutedSwatch();
                        try {

                            int color = vibrantDarkSwatch.getRgb(); // for the status bar.
                            int alphaColor = Color.argb(170, Color.red(color), Color.green(color), Color.blue(color));
//                            int textColor = mutedLightSwatch.getBodyTextColor();

                            // Set awesome colors to texts and backgrounds

//                            mScrollView.setBackgroundColor(mutedLightSwatch.getRgb());
                            mTitleBackground.setBackgroundColor(alphaColor);
//                            mDescriptionTitle.setTextColor(textColor);//vibrantSwatchTitleTextColor);
//                            mDescription.setTextColor(textColor);
//                            mTrailersTitle.setTextColor(textColor);
//                            mReviewsTitle.setTextColor(textColor);

                            // Set awesome drawable colors
//                            Drawable[] drawables = mDescriptionTitle.getCompoundDrawables();
//                            drawables[0].setColorFilter(textColor, PorterDuff.Mode.MULTIPLY);
//
//                            Drawable[] drawableTrailersTitle = mTrailersTitle.getCompoundDrawables();
//                            drawableTrailersTitle[0].setColorFilter(textColor, PorterDuff.Mode.MULTIPLY);
//                            Drawable[] drawableReviewsTitle = mReviewsTitle.getCompoundDrawables();
//                            drawableReviewsTitle[0].setColorFilter(textColor, PorterDuff.Mode.MULTIPLY);

                            changeStatusBarColor(color);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }


    }

    @Override
    public void showTrailers(ArrayList<Video> trailers){
        mTrailersAdapter = new TrailersAdapter(trailers);
        mTrailersLayoutManager = new MyLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mTrailersRecycleView.setLayoutManager(mTrailersLayoutManager);
        mTrailersRecycleView.setHasFixedSize(true);
        mTrailersRecycleView.setAdapter(mTrailersAdapter);
        mTrailersRecycleView.setNestedScrollingEnabled(false);
    }

    @Override
    public void showReviews(ArrayList<Review> reviews) {

        if(reviews.size() > 0){
            mReviewsAdapter = new ReviewsAdapter(reviews);
            mReviewsLayoutManager = new MyLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
//        mReviewsRecyclerView.setHasFixedSize(true);
            mReviewsRecyclerView.setAdapter(mReviewsAdapter);
            mTrailersRecycleView.setNestedScrollingEnabled(false);
        }else{
            mReviewsTitle.setVisibility(View.GONE);
        }

    }

    @Override
    public void setFavorite() {
        isFavorite = true;
//        fab.setBackgroundColor(getContext().getResources().getColor(R.color.color_favorite));
        fab.setBackgroundResource(R.drawable.fab);
    }

    public void playTrailer(){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="
                + mTrailerUrl));

        // Verify that the intent will resolve to an activity
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            showError();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void changeStatusBarColor(int color){
        Window window = MovieDetailActivity.this.getWindow();

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


    public void animateFavorite(){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.3f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.3f);
        ObjectAnimator scaleAnim = ObjectAnimator.ofPropertyValuesHolder(fab, pvhX, pvhY);
//            scaleAnim.setInterpolator(new BounceInterpolator());
        scaleAnim.setDuration(500);
        scaleAnim.setRepeatCount(1);
        scaleAnim.setRepeatMode(ValueAnimator.REVERSE);

        //Let's change background's color to red.
        Drawable[] color = {fab.getBackground(),
                getContext().getResources().getDrawable(R.drawable.fab)};
        TransitionDrawable trans = new TransitionDrawable(color);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
        fab.setBackgroundDrawable(trans);

        trans.startTransition(1000);

        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(fab, View.ROTATION, 720);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setDuration(1400);

        AnimatorSet setAnim = new AnimatorSet();
        setAnim.play(scaleAnim).with(rotateAnim);
        setAnim.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.actio_play_trailer){
            playTrailer();

        }else if(id == R.id.button_fab){

            if(!isFavorite){
                ContentValues values = new ContentValues();
                values.put(MoviesColumns.MOVIE_ID, mMovieId);
                values.put(MoviesColumns.TITLE, String.valueOf(mTitle.getText()));

                Uri inserMoviesUri;

                inserMoviesUri = getContentResolver().insert(
                        MoviesProvider.Movies.MOVIES,
                        values);

                animateFavorite();
            }else{
                animateFavoritePulse();
            }

        }
    }

}
