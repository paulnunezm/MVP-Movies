package com.nunez.popularmovies.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.Review;

import java.util.ArrayList;

/**
 * Created by paulnunez on 2/7/16.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private ArrayList<Review> mReviews;
    private Context mContext;
    private Resources res;

    public ReviewsAdapter(ArrayList<Review> reviews) {
        mReviews = reviews;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_detail_reviews, parent, false);

        if (res == null) {
            res = mContext.getResources();
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        final Review review = mReviews.get(position);

        holder.reviewer.setText(String.format(res.getString(R.string.format_reviewer),
                review.getAuthor()));
        holder.review.setText(review.getContent());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(review.getUrl()));
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView reviewer;
        private TextView review;
        private View container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            reviewer = (TextView) itemView.findViewById(R.id.text_reviewer);
            review = (TextView) itemView.findViewById(R.id.text_review);
        }
    }
}
