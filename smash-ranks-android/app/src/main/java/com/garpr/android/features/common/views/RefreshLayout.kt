package com.garpr.android.features.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.extensions.requireViewByIdCompat
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.ListLayout

/**
 * A child class of the official Android [SwipeRefreshLayout] that helps us work around some of
 * its shortcomings. We should use this view instead of SwipeRefreshLayout in every case.
 */
class RefreshLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs), Heartbeat, ListLayout {

    @IdRes
    private val scrollingChildId: Int

    private var scrollingChild: View? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout)
        scrollingChildId = ta.getResourceId(R.styleable.RefreshLayout_scrollingChildId, View.NO_ID)
        ta.recycle()
    }

    /*
     * http://stackoverflow.com/q/25270171/823952
     */
    override fun canChildScrollUp(): Boolean {
        return scrollingChild?.canScrollVertically(-1) ?: super.canChildScrollUp()
    }

    override fun getRecyclerView(): RecyclerView? {
        return scrollingChild as? RecyclerView?
    }

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (scrollingChildId != View.NO_ID) {
            scrollingChild = requireViewByIdCompat(scrollingChildId)
        }

        setColorSchemeColors(*resources.getIntArray(R.array.spinner_colors))
        setProgressBackgroundColorSchemeResource(R.color.card_background)
    }

}
