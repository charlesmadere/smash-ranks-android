package com.garpr.android.views

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.AbsListView
import android.widget.ScrollView
import com.garpr.android.R

/**
 * A child class of the official Android [SwipeRefreshLayout] that helps us work around some of
 * its shortcomings. We should use this view instead of SwipeRefreshLayout in every case.
 */
class RefreshLayout(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    private var scrollingChildId: Int = 0
    private var scrollingChild: View? = null


    init {
        parseAttributes(attrs)
    }

    /*
     * http://stackoverflow.com/q/25270171/823952
     */
    override fun canChildScrollUp(): Boolean {
        if (scrollingChild == null) {
            return super.canChildScrollUp()
        } else {
            return ViewCompat.canScrollVertically(scrollingChild, -1)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (scrollingChildId != 0) {
            findScrollingChild()
        }
    }

    private fun findScrollingChild() {
        val scrollingChild = findViewById(scrollingChildId)

        if (scrollingChild == null) {
            throw NullPointerException("unable to find scrolling child")
        } else if (scrollingChild is AbsListView || scrollingChild is NestedScrollView
                || scrollingChild is RecyclerView || scrollingChild is ScrollView) {
            this.scrollingChild = scrollingChild
        } else {
            throw IllegalStateException("scrollingChild (" + scrollingChild +
                    ") must be an AbsListView, RecyclerView, or ScrollView")
        }
    }

    private fun parseAttributes(attrs: AttributeSet) {
        if (isInEditMode) {
            return
        }

        val ta = context.obtainStyledAttributes(attrs,
                R.styleable.RefreshLayout)

        val spinnerColorsResId = ta.getResourceId(R.styleable.RefreshLayout_spinnerColors,
                R.array.spinner_colors)
        setColorSchemeColors(*resources.getIntArray(spinnerColorsResId))

        setProgressBackgroundColorSchemeResource(
                ta.getResourceId(R.styleable.RefreshLayout_spinnerBackground,
                        R.color.card_background))

        if (ta.hasValue(R.styleable.RefreshLayout_scrollingChild)) {
            scrollingChildId = ta.getResourceId(R.styleable.RefreshLayout_scrollingChild, 0)
        }

        ta.recycle()
    }

}
