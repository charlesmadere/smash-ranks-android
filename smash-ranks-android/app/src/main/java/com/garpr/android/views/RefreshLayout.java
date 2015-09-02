package com.garpr.android.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.garpr.android.R;


public class RefreshLayout extends SwipeRefreshLayout {


    private static final int DEFAULT_SPINNER_COLORS = R.array.spinner_colors;

    private int mScrollingChildId;
    private View mScrollingChild;




    public RefreshLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
    }


    @Override
    public boolean canChildScrollUp() {
        final boolean canChildScrollUp;

        if (mScrollingChild == null) {
            canChildScrollUp = super.canChildScrollUp();
        } else {
            // -1 means to check scrolling up (1 would check scrolling down)
            canChildScrollUp = ViewCompat.canScrollVertically(mScrollingChild, -1);
        }

        return canChildScrollUp;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (mScrollingChildId != 0) {
            final View scrollingChild = findViewById(mScrollingChildId);

            if (scrollingChild == null) {
                throw new IllegalStateException("unable to find scrolling child with view ID of "
                        + mScrollingChild);
            }

            setScrollingChild(scrollingChild);
        }
    }


    private void parseAttributes(final AttributeSet attrs) {
        final TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.refresh_layout, 0, 0);

        if (ta.hasValue(R.styleable.refresh_layout_scrolling_child)) {
            mScrollingChildId = ta.getResourceId(R.styleable.refresh_layout_scrolling_child, 0);
        }

        final int spinnerColorsResId = ta.getResourceId(
                R.styleable.refresh_layout_spinner_colors, DEFAULT_SPINNER_COLORS);
        final int spinnerColors[] = getResources().getIntArray(spinnerColorsResId);
        setColorSchemeColors(spinnerColors);

        if (ta.hasValue(R.styleable.refresh_layout_spinner_size)) {
            setSize(ta.getInt(R.styleable.refresh_layout_spinner_size, DEFAULT));
        }

        ta.recycle();
    }


    @Override
    public void setRefreshing(final boolean refreshing) {
        post(new Runnable() {
            @Override
            public void run() {
                RefreshLayout.super.setRefreshing(refreshing);
            }
        });
    }


    public void setScrollingChild(final View scrollingChild) throws IllegalArgumentException {
        if (scrollingChild instanceof AbsListView || scrollingChild instanceof RecyclerView
                || scrollingChild instanceof ScrollView) {
            mScrollingChild = scrollingChild;
        } else {
            throw new IllegalArgumentException("scrollingChild (" + scrollingChild +
                    ") must be an AbsListView, RecyclerView, or ScrollView");
        }
    }


}
