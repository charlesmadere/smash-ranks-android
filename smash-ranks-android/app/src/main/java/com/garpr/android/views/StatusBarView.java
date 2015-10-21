package com.garpr.android.views;


import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.widget.Space;
import android.util.AttributeSet;

import com.garpr.android.R;


/**
 * A View that will apply the height of the Android status bar to itself on versions of Android
 * greater than or equal to {@link Build.VERSION_CODES#LOLLIPOP}. On Android versions older
 * than that, this view simply won't appear.
 */
public class StatusBarView extends Space {


    public StatusBarView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public StatusBarView(final Context context, final AttributeSet attrs,
            final int defStyle) {
        super(context, attrs, defStyle);
    }


    private int getStatusBarHeight() {
        final Resources res = getResources();

        // How do I get the height of Android's status bar?
        // http://stackoverflow.com/a/3410200/823952
        int statusBarHeightResId = res.getIdentifier("status_bar_height", "dimen", "android");

        if (statusBarHeightResId == 0) {
            statusBarHeightResId = R.dimen.status_bar_height;
        }

        return res.getDimensionPixelSize(statusBarHeightResId);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            heightMeasureSpec = getStatusBarHeight();
        } else {
            heightMeasureSpec = 0;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


}
