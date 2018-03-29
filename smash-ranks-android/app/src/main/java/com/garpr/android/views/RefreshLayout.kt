package com.garpr.android.views

import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.ListLayout

/**
 * A child class of the official Android [SwipeRefreshLayout] that helps us work around some of
 * its shortcomings. We should use this view instead of SwipeRefreshLayout in every case.
 */
open class RefreshLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs), Heartbeat, ListLayout {

    @IdRes
    private var scrollingChildId: Int = View.NO_ID


    init {
        parseAttributes(attrs)
    }

    /*
     * http://stackoverflow.com/q/25270171/823952
     */
    override fun canChildScrollUp(): Boolean {
        return scrollingChild?.canScrollVertically(-1) ?: super.canChildScrollUp()
    }

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (scrollingChildId != View.NO_ID) {
            scrollingChild = findViewById(scrollingChildId) ?: throw NullPointerException(
                    "unable to find scrolling child (${resources.getResourceName(scrollingChildId)})")
        }

        setProgressBackgroundColorSchemeResource(R.color.card_background)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        if (isInEditMode) {
            return
        }

        val ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout)
        val spinnerColorsResId = ta.getResourceId(R.styleable.RefreshLayout_spinnerColors,
                R.array.spinner_colors)
        setColorSchemeColors(*resources.getIntArray(spinnerColorsResId))

        if (ta.hasValue(R.styleable.RefreshLayout_scrollingChildId)) {
            scrollingChildId = ta.getResourceId(R.styleable.RefreshLayout_scrollingChildId, View.NO_ID)
        }

        ta.recycle()
    }

    override val recyclerView: RecyclerView?
        get() = scrollingChild as? RecyclerView

    var scrollingChild: View? = null

}
