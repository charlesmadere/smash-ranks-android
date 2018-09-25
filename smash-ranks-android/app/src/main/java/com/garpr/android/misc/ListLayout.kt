package com.garpr.android.misc

import androidx.recyclerview.widget.RecyclerView
import com.garpr.android.extensions.smoothScrollToTop

interface ListLayout {

    val recyclerView: RecyclerView?

    fun smoothScrollToTop() {
        recyclerView?.smoothScrollToTop()
    }

}
