package com.garpr.android.views;


import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.garpr.android.R;


/**
 * An extension of the support library's {@link SwipeRefreshLayout} that works around a few bugs.
 * Note that this class was made with support library v21.0.3 in mind. So it's possible that a
 * new one will have fix this stuff: https://developer.android.com/tools/support-library/index.html
 *
 * 1. Normally, a SwipeRefreshLayout only allows one child view, and that one child view must be
 * either an {@link AbsListView} or a {@link RecyclerView}. And if you break this paradigm, then
 * your list will be incapable of scrolling up. So in order to take advantage of this cool fix,
 * you have to use the {@link #setScrollingView(View)} method. This is talked about a bit more
 * on Stack Overflow: http://stackoverflow.com/q/23053799
 *
 * 2. If you try to use {@link SwipeRefreshLayout#setRefreshing(boolean)} early on in your
 * Activity's / Fragment's lifecycle, then the little spinner won't show appear on screen. I've
 * supplied the work around for this in this class's overridden {@link #setRefreshing(boolean)}.
 * https://code.google.com/p/android/issues/detail?id=77712
 */
public final class FlexibleSwipeRefreshLayout extends SwipeRefreshLayout {


    private View mTarget;




    public FlexibleSwipeRefreshLayout(final Context context) {
        super(context);
    }


    public FlexibleSwipeRefreshLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean canChildScrollUp() {
        final boolean canChildScrollUp;

        if (mTarget == null) {
            canChildScrollUp = super.canChildScrollUp();
        } else {
            // -1 means to check scrolling up (1 would check scrolling down)
            canChildScrollUp = ViewCompat.canScrollVertically(mTarget, -1);
        }

        return canChildScrollUp;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setColorSchemeResources(R.color.indigo_bright, R.color.indigo, R.color.indigo_dark);
    }


    @Override
    public void setRefreshing(final boolean refreshing) {
        final Runnable action = new Runnable() {
            @Override
            public void run() {
                FlexibleSwipeRefreshLayout.super.setRefreshing(refreshing);
            }
        };

        post(action);
    }


    public void setScrollingView(final View target) throws IllegalArgumentException {
        if (target instanceof AbsListView || target instanceof RecyclerView
                || target instanceof ScrollView) {
            mTarget = target;
        } else {
            throw new IllegalArgumentException("target (" + target + ") must be an AbsListView,"
                    + " RecyclerView, or ScrollView");
        }
    }


}
