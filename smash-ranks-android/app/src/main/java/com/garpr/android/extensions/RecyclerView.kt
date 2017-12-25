package com.garpr.android.extensions

import android.support.v7.widget.RecyclerView

fun RecyclerView.smoothScrollToTop() {
    layoutManager?.smoothScrollToPosition(this, RecyclerView.State(), 0)
            ?: scrollToPosition(0)
}
