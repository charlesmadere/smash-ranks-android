package com.garpr.android.views;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * This view's code is taken from Stack Overflow: http://stackoverflow.com/a/9650884/823952
 */
public final class NonSwipeableViewPager extends ViewPager {


    public NonSwipeableViewPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(final MotionEvent ev) {
        return false;
    }


    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        return false;
    }


}
