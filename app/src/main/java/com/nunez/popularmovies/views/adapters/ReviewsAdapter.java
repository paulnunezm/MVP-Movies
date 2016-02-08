package com.nunez.popularmovies.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.Review;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by paulnunez on 2/7/16.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private ArrayList<Review> mReviews;
    private Resources res;

    public ReviewsAdapter(ArrayList<Review> reviews) {
        mReviews = reviews;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_reviews, parent, false);

        if (res == null) {
            res = parent.getContext().getResources();
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        Review review = mReviews.get(position);

        holder.reviewer.setText(String.format(res.getString(R.string.format_reviewer),
                review.getAuthor()));
        holder.review.setText(String.format(res.getString(R.string.format_reviews),
                review.getContent()));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView reviewer;
        private TextView review;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewer = (TextView) itemView.findViewById(R.id.text_reviewer);
            review = (TextView) itemView.findViewById(R.id.text_review);
        }
    }
}
