package com.nunez.popularmovies.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.Genres;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.mvp.presenters.RecyclerViewClickListener;
import com.nunez.popularmovies.utils.Constants;

import java.util.ArrayList;

/**
 * Created by paulnunez on 11/16/15.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private static String TAG = MoviesAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<Movie> mMovies;
    private int mLastVisibleItem;
    private RecyclerViewClickListener mRecyclerClickListener;
    private MovieViewHolder clickedView;


    public MoviesAdapter(ArrayList<Movie> movies,  int lastVisibleItem) {
        mMovies = movies;
        mLastVisibleItem = lastVisibleItem;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View movieView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        mContext = parent.getContext();


        return new MovieViewHolder(movieView, mRecyclerClickListener);
    }

    public void setRecyclerListListener(RecyclerViewClickListener mRecyclerClickListener) {
        this.mRecyclerClickListener = mRecyclerClickListener;
    }

    public void clearData(){
        mMovies.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {

        Movie currentMovie = mMovies.get(position);

        holder.title.setText(currentMovie.title);
        holder.rating.setText(String.format(mContext.getResources()
                .getString(R.string.format_rating), currentMovie.rating));

        String genres  = "";
        ArrayList<Integer> genreCodes = currentMovie.genres;

        if(genreCodes != null && genreCodes.size() > 0 ){
            genres= (Genres.list.get(genreCodes.get(0)));
            if(genreCodes.size()>1){
                genres = genres.concat(", " + Genres.list.get(genreCodes.get(1)));
            }
        }
        holder.genres.setText(genres);

        Glide.with(mContext).
                load(Constants.POSTER_BASE_URL+currentMovie.posertPath)
                .placeholder(mContext.getResources().getColor(R.color.movie_placeholder))
                .error(mContext.getResources().getDrawable(R.drawable.ic_trailers))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        animateTitle(holder);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

//                        setTitleBackgroundColor(((GlideBitmapDrawable) resource).getBitmap(), holder, position);
                        animateTitle(holder);

                        return false;
                    }
                })
                .into(holder.poster);

//        if(!holder.containerAnimated){
//            holder.containerAnimated = true;
////            holder.container.setTranslationY(holder.container.getHeight());
//
//            holder.container.animate()
//                    .translationY(0)
//                    .setDuration(500)
//                    .setInterpolator(new AccelerateDecelerateInterpolator())
////                    .setStartDelay(20 * position)
//                    .start();
////            Animation animation = AnimationUtils.loadAnimation(mContext, android.);
////            holder.container.setAnimation(new TranslateAnimation(0,0, 50,0));
//        }

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    private void animateTitle(MovieViewHolder holder) {

        int cellPosition = holder.getAdapterPosition();

        if (!holder.animated && cellPosition>=0) {
            holder.animated = true;
            holder.title.animate()
                    .alpha(1)
                    .setDuration(200)
                    .setStartDelay(40 * cellPosition)
//                    .setStartDelay(500)
                    .start();
        }
    }

    public ArrayList<Movie> getMovies(){
        return mMovies;
    }

    private void clearPreviousClickedView(){

        if(clickedView != null){
            clickedView.clickState.
                    setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        }
    }


    public void setTitleBackgroundColor(Bitmap bitmap, final MovieViewHolder holder, final int position){

        if (bitmap != null) {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {


                @Override
                public void onGenerated(Palette palette) {

                    Palette.Swatch vibrantDarkSwatch = palette.getDarkVibrantSwatch();

                    if (vibrantDarkSwatch != null) {

                        int color = vibrantDarkSwatch.getRgb();

                        int alphaColor = Color.argb(170, Color.red(color), Color.green(color), Color.blue(color));

                        holder.title.setBackgroundColor(alphaColor);

                    }
                }
            });
        }
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      private final RecyclerViewClickListener onClickListener;
      protected     TextView                  title;
      protected     TextView                  rating;
      protected     TextView                  genres;
      protected     ImageView                 poster;
      protected     CardView                  container;
        protected boolean animated = false;
        protected boolean containerAnimated = false;
        private FrameLayout clickState;


        public MovieViewHolder(View itemView, final RecyclerViewClickListener onClickListener) {
            super(itemView);

            container = (CardView) itemView.findViewById(R.id.item_movie_container);
            title = (TextView) itemView.findViewById(R.id.item_movie_title);
            poster= (ImageView) itemView.findViewById(R.id.item_poster);
            clickState = (FrameLayout) itemView.findViewById(R.id.click_state);
            rating = (TextView) itemView.findViewById(R.id.item_movie_rating);
            genres = (TextView) itemView.findViewById(R.id.item_movie_genres);

            /**
             * Important
             */
//            clickState.setonf(this);
            clickState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        onClickListener.onClick(v, getAdapterPosition(), 0f, 0f);
                    }
                }
            });
            this.onClickListener = onClickListener;
           }


//        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d(TAG, "onTouch: ");
            if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_MOVE) {

//                clickedItem = position;
//                notifyDataSetChanged();

                onClickListener.onClick(v, getAdapterPosition(), event.getX(), event.getY());
            }
            return true;
        }


        @Override
        public void onClick(View v) {
          onClickListener.onClick(poster, getAdapterPosition(), 0f, 0f);
        }
    }
}
