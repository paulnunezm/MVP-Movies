package com.nunez.popularmovies.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    private Context mContext;

    public TrailersAdapter(ArrayList<Video> videos) {
        mTrailers = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View trailerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_trailer, parent, false);

        if (mContext == null) {
            mContext = parent.getContext();
        }


        return new ViewHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mTrailerName.setText(mTrailers.get(position).name);
        holder.trailerId = mTrailers.get(position).id;
//        holder.mTrailerName.setText(String.format(res.getString(R.string.format_reviews,
//                mTrailers.get(position).name)));

        // Checks if the view is the last to show/hide a divider
//        int visibility = (position == getItemCount()-1) ? View.GONE : View.VISIBLE;
//        holder.mDivider.setAlpha(0);

        if(position == getItemCount()-1){
            holder.mDivider.setAlpha(0);
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="
                        + holder.trailerId));

                // Verify that the intent will resolve to an activity
                if(intent.resolveActivity(mContext.getPackageManager()) != null){

                    mContext.startActivity(intent);
                }else{
//                    showError()
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTrailerName;
        protected View mDivider;
        protected String trailerId;
        protected View container;

        public ViewHolder(View itemView) {
            super(itemView);

            container = itemView;
            mTrailerName = (TextView) itemView.findViewById(R.id.item_detail_trailer);
            mDivider = itemView.findViewById(R.id.divider);
        }
    }

}
