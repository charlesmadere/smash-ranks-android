package com.garpr.android.misc

import androidx.recyclerview.widget.RecyclerView
import com.garpr.android.extensions.smoothScrollToTop

interface ListLayout {

    fun getRecyclerView(): RecyclerView?

    fun smoothScrollToTop() {
        getRecyclerView()?.smoothScrollToTop()
    }

}
