package com.nunez.popularmovies.views.custom_views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by paulnunez on 11/16/15.
 */
public class RalewayMediumTextView extends TextView {
    public RalewayMediumTextView(Context context) {
        super(context);

        if (!isInEditMode())
            init(context);
    }

    public RalewayMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode())
            init(context);
    }

    public RalewayMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode())
            init(context);
    }

    @TargetApi(21)
    public RalewayMediumTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (!isInEditMode())
            init(context);
    }

    public void init(Context context){
        Typeface t = Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf");
        this.setTypeface(t);
    }
}
