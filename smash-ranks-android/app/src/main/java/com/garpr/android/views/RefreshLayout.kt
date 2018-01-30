package com.garpr.android.views

import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R

/**
 * A child class of the official Android [SwipeRefreshLayout] that helps us work around some of
 * its shortcomings. We should use this view instead of SwipeRefreshLayout in every case.
 */
class RefreshLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    @IdRes
    private var mScrollingChildId: Int? = null

    private var mScrollingChild: View? = null


    init {
        parseAttributes(attrs)
    }

    /*
     * http://stackoverflow.com/q/25270171/823952
     */
    override fun canChildScrollUp(): Boolean {
        return mScrollingChild?.canScrollVertically(-1) ?: super.canChildScrollUp()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mScrollingChildId?.let {
            mScrollingChild = findViewById(it) ?: throw NullPointerException(
                    "unable to find scrolling child")
        }
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        if (isInEditMode) {
            return
        }

        val ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout)
        val spinnerColorsResId = ta.getResourceId(R.styleable.RefreshLayout_spinnerColors,
                R.array.spinner_colors)
        setColorSchemeColors(*resources.getIntArray(spinnerColorsResId))

        if (ta.hasValue(R.styleable.RefreshLayout_scrollingChild)) {
            mScrollingChildId = ta.getResourceId(R.styleable.RefreshLayout_scrollingChild, 0)
        }

        ta.recycle()
    }

}
