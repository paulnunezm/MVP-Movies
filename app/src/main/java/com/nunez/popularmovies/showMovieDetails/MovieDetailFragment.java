package com.nunez.popularmovies.showMovieDetails;

import android.animation.Animator;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by paulnunez on 3/9/16.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailsContract.View {

  public static  String MOVIE_ID     = "movie_id";
  private static String LOG_TAG      = MovieDetailFragment.class.getSimpleName();
  public static  String FRAGMENT_TAG = LOG_TAG;

  @BindView(R.id.image_poster)
  ImageView mPoster;

  @BindView(R.id.text_title_bgnd)
  View mTitleBackground;

  @BindView(R.id.details_scrollView)
  NestedScrollView mScrollView;

  @BindView(R.id.text_title)
  TextView mTitle;

  @BindView(R.id.text_description)
  TextView mDescription;

  @BindView(R.id.text_description_title)
  TextView mDescriptionTitle;

  @BindView(R.id.text_trailers)
  TextView mTrailersTitle;

  @BindView(R.id.text_reviews)
  TextView mReviewsTitle;

  @BindView(R.id.text_release)
  TextView mReleaseDate;

  @BindView(R.id.text_rating)
  TextView mRatings;

  @Nullable
  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.recyler_trailers)
  RecyclerView mTrailersRecycleView;

  @BindView(R.id.recyler_reviews)
  RecyclerView mReviewsRecyclerView;

  @BindView(R.id.button_fab)
  ImageButton fab;

  @BindView(R.id.progress)
  ProgressBar mProgress;

  @BindView(R.id.container)
  View mDetailsContainer;

  @BindView(R.id.no_movies)
  View mErrorScreen;

  private String                mMovieId;
  private String                mTrailerUrl;
  private boolean               isFavorite;
  private boolean               canClickFab;
  private boolean               shareInflated;
  private int                   heartInitColor;
  private MovieDetailsPresenter mDetailPresenter;
  private TrailersAdapter       mTrailersAdapter;
  private LinearLayoutManager   mReviewsLayoutManager;
  private LinearLayoutManager   mTrailersLayoutManager;
  private ReviewsAdapter        mReviewsAdapter;
  private ShareActionProvider   mShareActionProvider;


  public MovieDetailFragment() {
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

    Bundle args = getArguments();

    if (args != null) {
      mMovieId = args.getString(MOVIE_ID);
    }

    shareInflated = (container.findViewById(R.id.action_share) != null);

    canClickFab = true;

    ButterKnife.bind(this, rootView);
    initalizeViews(rootView);

    mDetailPresenter = new MovieDetailsPresenter(mMovieId);
    mDetailPresenter.attachView(this);

    heartInitColor = getActivity().getResources().getColor(R.color.gray_dark);

    return rootView;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Inflate menu resource file.
    menu.clear();
    inflater.inflate(R.menu.fragment_detail, menu);

    MenuItem shareItem = menu.findItem(R.id.action_share);
    mShareActionProvider =
        (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

    if (mTrailerUrl != null) {
      mShareActionProvider.setShareIntent(createShareIntent());
    }

    super.onCreateOptionsMenu(menu, inflater);
  }


  @Override
  public void onStart() {
    super.onStart();
    mDetailPresenter.startDetail(isNetworkConnected());
  }

  @Override
  public void onResume() {
    super.onResume();
  }


  public void initalizeViews(View v) {

    AppCompatActivity activity = (AppCompatActivity) getActivity();
    if (toolbar != null) {
      toolbar.setTitle("");
      activity.setSupportActionBar(toolbar);
      ActionBar actionBar = activity.getSupportActionBar();
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    mReviewsRecyclerView.setNestedScrollingEnabled(false);
    mTrailersRecycleView.setNestedScrollingEnabled(false);
  }


  public Intent createShareIntent() {
    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(Intent.EXTRA_TEXT, mTrailerUrl);
    return sharingIntent;
  }


  @Override
  public void setTrailerLink(String url) {
    mTrailerUrl = "http://www.youtube.com/watch?v=" + url;

    if (mShareActionProvider != null) {
      mShareActionProvider.setShareIntent(createShareIntent());
    }

  }

  @Override
  public void showPoster(String url) {
    mScrollView.scrollTo(0, 0);


    Glide.with(PopularMovies.context).
        load(Constants.POSTER_BASE_URL + url)
        .centerCrop()
        .placeholder(PopularMovies.context.getResources().getColor(R.color.movie_placeholder))
        .error(PopularMovies.context.getResources().getDrawable(R.drawable.ic_trailers))
        .listener(new RequestListener<String, GlideDrawable>() {
          @Override
          public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
          }

          @Override
          public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

            setColors(((GlideBitmapDrawable) resource).getBitmap());
            mScrollView.scrollTo(0, 0);

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
    if (trailers != null & trailers.size() > 0) {
      mTrailersAdapter = new TrailersAdapter(trailers);
      mTrailersLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
      mTrailersRecycleView.setLayoutManager(mTrailersLayoutManager);
      mTrailersRecycleView.setHasFixedSize(true);
      mTrailersRecycleView.setAdapter(mTrailersAdapter);
      mTrailersRecycleView.setNestedScrollingEnabled(false);
    } else {
      mTrailersTitle.setVisibility(View.GONE);
    }
  }

  @Override
  public void showReviews(ArrayList<Review> reviews) {
    if (reviews != null && reviews.size() > 0) {
      mReviewsAdapter = new ReviewsAdapter(reviews);
      mReviewsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
      mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
      mReviewsRecyclerView.setAdapter(mReviewsAdapter);
      mTrailersRecycleView.setNestedScrollingEnabled(false);
    } else {
      mReviewsTitle.setVisibility(View.GONE);
    }
  }

  @Override
  public void showMessage(String message) {
    Snackbar.make(mErrorScreen, message, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void setFavorite() {
    isFavorite = true;
    fab.setBackgroundResource(R.drawable.fab);
    fab.setRotation(720);
    fab.setColorFilter(getActivity().getResources().getColor(R.color.white));
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
    mDetailsContainer.setVisibility(View.INVISIBLE);
    mErrorScreen.setVisibility(View.GONE);
    mProgress.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideLoading() {
    mProgress.setVisibility(View.GONE);
    mErrorScreen.setVisibility(View.GONE);
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
    mDetailsContainer.setVisibility(View.INVISIBLE);
    mErrorScreen.setVisibility(View.VISIBLE);
    showMessage(getResources().getString(R.string.error_connection));
  }

  @OnClick(R.id.actio_play_trailer)
  public void onBigTrailerClick() {
    playTrailer();
  }

  @OnClick(R.id.button_fab)
  public void onFabClicked() {
    if (canClickFab) {
      if (isFavorite) {
        isFavorite = false;
        mDetailPresenter.removeMovieFromDb();
      } else {
        isFavorite = true;
        mDetailPresenter.saveMovieToDb();
      }
      animateFavorite();
    }
  }

  public void setColors(Bitmap bitmap) {

    if (bitmap != null) {
      Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

        @Override
        public void onGenerated(Palette palette) {

          if (palette != null) {
            Palette.Swatch vibrantDarkSwatch = palette.getDarkVibrantSwatch();

            try {
              int color      = vibrantDarkSwatch.getRgb(); // for the status bar.
              int alphaColor = Color.argb(170, Color.red(color), Color.green(color), Color.blue(color));
              //int textColor = mutedLightSwatch.getBodyTextColor();

              // Set awesome colors to texts and backgrounds

              heartInitColor = color;
              mTitleBackground.setBackgroundColor(alphaColor);
              toolbar.setBackgroundColor(alphaColor);
              if (isFavorite) {
                fab.setColorFilter(0xFFF);
              } else {
                fab.setColorFilter(heartInitColor);
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

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
  public void changeStatusBarColor(int color) {
    Window window = getActivity().getWindow();

    // clear FLAG_TRANSLUCENT_STATUS flag:
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    // finally change the color
    window.setStatusBarColor(color);
  }

  public void animateFavoritePulse() {
    PropertyValuesHolder pvhX      = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.3f);
    PropertyValuesHolder pvhY      = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.3f);
    ObjectAnimator       scaleAnim = ObjectAnimator.ofPropertyValuesHolder(fab, pvhX, pvhY);

    scaleAnim.setDuration(500);
    scaleAnim.setRepeatCount(1);
    scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
    scaleAnim.start();
  }

  public Context getContext() {
    return getActivity();
  }

  public void animateFavorite() {
    float scaleValue = 1.3f;
    int   backgroundColorReference;
    int   rotation;
    int   heartColorReference;

    ObjectAnimator scaleAnim;

    if (!isFavorite) {
      backgroundColorReference = R.drawable.circle_white;
      rotation = 0;
      heartColorReference = R.color.color_favorite;
      scaleAnim = null;

    } else {
      backgroundColorReference = R.drawable.fab;
      rotation = 720;
      heartColorReference = R.color.white;
    }

    PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, scaleValue);
    PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleValue);
    scaleAnim = ObjectAnimator.ofPropertyValuesHolder(fab, pvhX, pvhY);

    scaleAnim.setDuration(500);
    scaleAnim.setRepeatCount(1);
    scaleAnim.setRepeatMode(ValueAnimator.REVERSE);

    //Let's change background's color to red.
    Drawable[] color = {fab.getBackground(),
        getContext().getResources().getDrawable(backgroundColorReference)};
    TransitionDrawable trans = new TransitionDrawable(color);

    //This will work also on old devices. The latest API says you have to use setBackground instead.
    fab.setBackgroundDrawable(trans);
    trans.startTransition(700);

    ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(fab, View.ROTATION, rotation);
    rotateAnim.setInterpolator(new DecelerateInterpolator());
    rotateAnim.setDuration(1400);

    AnimatorSet setAnim = new AnimatorSet();
    if (isFavorite) {
      ObjectAnimator heartColorAnim = ObjectAnimator.ofInt(fab, "colorFilter", getResources().getColor(heartColorReference));
      heartColorAnim.setDuration(700).setStartDelay(700);

      setAnim.play(scaleAnim).with(rotateAnim).with(heartColorAnim);
    } else {
      setAnim.play(rotateAnim).with(scaleAnim);
      fab.setColorFilter(heartInitColor);
    }

    setAnim.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {
        canClickFab = false;
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        canClickFab = true;
      }

      @Override
      public void onAnimationCancel(Animator animation) {

      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });

    setAnim.start();
  }

  private boolean isNetworkConnected() {
    ConnectivityManager cm =
        (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null &&
        activeNetwork.isConnectedOrConnecting();
  }

  public void playTrailer() {
    if (mTrailerUrl != null) {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTrailerUrl));

      // Verify that the intent will resolve to an activity
      if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
        startActivity(intent);
      } else {
        showMessage(getResources().getString(R.string.error_no_trailer));
      }
    } else {
      showMessage(getResources().getString(R.string.error_no_trailer));
    }
  }
}
