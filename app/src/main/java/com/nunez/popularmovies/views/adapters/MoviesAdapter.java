package com.nunez.popularmovies.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.mvp.presenters.RecyclerViewClickListener;
import com.nunez.popularmovies.utils.Constants;

import java.util.ArrayList;

/**
 * Created by paulnunez on 11/16/15.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context mContext;
    private ArrayList<Movie> mMovies;
    private int mLastVisibleItem;
    private RecyclerViewClickListener mRecyclerClickListener;


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

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {

        Movie currentMovie = mMovies.get(position);

        holder.title.setText(currentMovie.title);

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

        if(!holder.containerAnimated){
            holder.containerAnimated = true;
//            holder.container.setTranslationY(holder.container.getHeight());

            holder.container.animate()
                    .translationY(0)
                    .setDuration(500)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
//                    .setStartDelay(20 * position)
                    .start();
//            Animation animation = AnimationUtils.loadAnimation(mContext, android.);
//            holder.container.setAnimation(new TranslateAnimation(0,0, 50,0));
        }

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


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        protected TextView title;
        protected ImageView poster;
        protected FrameLayout container;
        protected boolean animated = false;
        protected boolean containerAnimated = false;
        private final RecyclerViewClickListener onClickListener;


        public MovieViewHolder(View itemView, RecyclerViewClickListener onClickListener) {
            super(itemView);

            container = (FrameLayout) itemView.findViewById(R.id.item_movie_container);
            title = (TextView) itemView.findViewById(R.id.item_movie_title);
            poster= (ImageView) itemView.findViewById(R.id.item_poster);

            /**
             * Important
             */
            poster.setOnTouchListener(this);
            this.onClickListener = onClickListener;

            // Maybe to get the rgb values later
//            poster.setDrawingCacheEnabled(true);
           }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_MOVE) {

                onClickListener.onClick(v, getAdapterPosition(), event.getX(), event.getY());
            }
            return true;
        }


    }
}
