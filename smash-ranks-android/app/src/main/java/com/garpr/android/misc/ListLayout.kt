package com.garpr.android.misc

import android.support.v7.widget.RecyclerView

interface ListLayout {

    val recyclerView: RecyclerView?

    fun smoothScrollToTop()

}
