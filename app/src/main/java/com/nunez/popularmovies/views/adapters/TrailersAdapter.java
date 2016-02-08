package com.nunez.popularmovies.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nunez.popularmovies.R;
import com.nunez.popularmovies.model.entities.Video;

import java.util.ArrayList;

/**
 * Created by paulnunez on 2/7/16.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {
    private ArrayList<Video> mTrailers;

    public TrailersAdapter(ArrayList<Video> videos) {
        mTrailers = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View trailerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_trailer, parent, false);


        return new ViewHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTrailerName.setText(mTrailers.get(position).name);
//        holder.mTrailerName.setText(String.format(res.getString(R.string.format_reviews,
//                mTrailers.get(position).name)));

        // Checks if the view is the last to show/hide a divider
        int visibility = (position == getItemCount()-1) ? View.GONE : View.VISIBLE;
        holder.mDivider.setVisibility(visibility);

    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTrailerName;
        protected View mDivider;

        public ViewHolder(View itemView) {
            super(itemView);

            mTrailerName = (TextView) itemView.findViewById(R.id.item_detail_trailer);
            mDivider = itemView.findViewById(R.id.divider);
        }
    }

}
