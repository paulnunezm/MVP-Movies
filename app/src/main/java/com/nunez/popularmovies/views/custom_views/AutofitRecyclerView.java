package com.nunez.popularmovies.views.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.nunez.popularmovies.R;

/**
 * Created by paulnunez on 3/11/16.
 */
public class AutofitRecyclerView extends RecyclerView {
    private GridLayoutManager manager;
    private int columnWidth = -1;

    public AutofitRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public AutofitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutofitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
//            int[] attrsArray = {
//                    android.R.attr.columnWidth
//            };
//            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.AutofitRecyclerView,
                    0, 0);
            columnWidth = a.getDimensionPixelSize(0, -1);
            a.recycle();
        }

        manager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(manager);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth > 0) {
            int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
            manager.setSpanCount(spanCount);
        }
    }
}
