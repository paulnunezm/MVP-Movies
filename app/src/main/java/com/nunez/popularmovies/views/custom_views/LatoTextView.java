package com.nunez.popularmovies.views.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nunez.popularmovies.R;

/**
 * Created by paulnunez on 2/16/16.
 */
public class LatoTextView extends TextView {

    public LatoTextView(Context context) {
        super(context);

        if (!isInEditMode())
            init(context);
    }

    public LatoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()){
            init(context);

            String fontType;

            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.LatoTextView,
                    0, 0);

            try {
                fontType = a.getString(R.styleable.LatoTextView_fontType);

            } finally {
                a.recycle();
            }

            Typeface t = Typeface.createFromAsset(context.getAssets(), "Lato-" + fontType + ".ttf");
            this.setTypeface(t);
        }
    }

    public LatoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            init(context);

            String fontType;

            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.LatoTextView,
                    0, 0);

            try {
                fontType = a.getString(R.styleable.LatoTextView_fontType);

            } finally {
                a.recycle();
            }

            Typeface t = Typeface.createFromAsset(context.getAssets(), "Lato-" + fontType + ".ttf");
            this.setTypeface(t);
        }
    }


    public void init(Context context){
    }
}
