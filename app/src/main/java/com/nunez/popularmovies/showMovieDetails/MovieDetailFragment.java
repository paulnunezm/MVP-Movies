package com.nunez.popularmovies.showMovieDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

  @BindView(R.id.container_description)
  View desriptionContainer;

  @BindView(R.id.container_reviews)
  View reviewsContainer;

  @BindView(R.id.container_trailers)
  View trailersContainer;


  private String                 mMovieId;
  private String                 mTrailerUrl;
  private boolean                isFavorite;
  private boolean                canClickFab;
  private boolean                shareInflated;
  private int                    heartInitColor;
  private MovieDetailsPresenter  mDetailPresenter;
  private MovieDetailsController movieDetailsController;
  private TrailersAdapter        mTrailersAdapter;
  private LinearLayoutManager    mReviewsLayoutManager;
  private LinearLayoutManager    mTrailersLayoutManager;
  private ReviewsAdapter         mReviewsAdapter;
  private ShareActionProvider    mShareActionProvider;
  private FabAnimator            fabAnimator;

  public MovieDetailFragment() {
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

    Bundle args = getArguments();
    if (args != null) mMovieId = args.getString(MOVIE_ID);

    shareInflated = (container.findViewById(R.id.action_share) != null);
    canClickFab = true;

    ButterKnife.bind(this, rootView);
    initalizeViews();

    // Instantiate presenter and controller
    mDetailPresenter = new MovieDetailsPresenter();
    mDetailPresenter.attachView(this);
    movieDetailsController = new MovieDetailsController(mMovieId, mDetailPresenter);
    mDetailPresenter.setController(movieDetailsController);

    heartInitColor = getActivity().getResources().getColor(R.color.gray_dark);

    postponeEnterTransition();

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

    if (mTrailerUrl != null) mShareActionProvider.setShareIntent(createShareIntent());

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

  public void initalizeViews() {

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
    if (mShareActionProvider != null) mShareActionProvider.setShareIntent(createShareIntent());
  }

  @Override
  public void showPoster(String url) {
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

            setViewColorsFromBitman(((GlideBitmapDrawable) resource).getBitmap());
            showEnterAnimation();

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
    mReviewsAdapter = new ReviewsAdapter(reviews);
    mReviewsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
    mReviewsRecyclerView.setAdapter(mReviewsAdapter);
    mTrailersRecycleView.setNestedScrollingEnabled(false);

    mScrollView.scrollTo(0, 0);
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
    mErrorScreen.setVisibility(View.GONE);
    mProgress.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideLoading() {
    mProgress.setVisibility(View.GONE);
    mErrorScreen.setVisibility(View.GONE);
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

  public void setViewColorsFromBitman(Bitmap bitmap) {
    if (bitmap != null) {
      new DetailsViewsColorChanger(bitmap, mTitleBackground,
          toolbar, fab, isFavorite, getActivity().getWindow()).changeColors();
    }
  }

  private void showEnterAnimation() {
    startPostponedEnterTransition();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Slide slide = new Slide(Gravity.BOTTOM);
      slide.setStartDelay(200);
      slide.setInterpolator(new DecelerateInterpolator());

      TransitionManager.beginDelayedTransition(mScrollView, slide);
      desriptionContainer.setVisibility(View.VISIBLE);
      trailersContainer.setVisibility(View.VISIBLE);
      reviewsContainer.setVisibility(View.VISIBLE);
    }
  }


  public Context getContext() {
    return getActivity();
  }

  public void animateFavorite() {
    if (fabAnimator == null) {
      fabAnimator = new FabAnimator(
          fab,
          heartInitColor,
          getContext().getResources());
    }
    fabAnimator.animateFab(isFavorite);
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
