package com.garpr.android.misc

import android.support.v7.widget.RecyclerView
import com.garpr.android.extensions.smoothScrollToTop

interface ListLayout {

    val recyclerView: RecyclerView?

    fun smoothScrollToTop() {
        recyclerView?.smoothScrollToTop()
    }

}
