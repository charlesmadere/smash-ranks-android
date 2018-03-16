package com.garpr.android.extensions

import android.support.v7.widget.RecyclerView

fun RecyclerView.smoothScrollToPosition(position: Int) {
    layoutManager?.smoothScrollToPosition(this, RecyclerView.State(), position)
            ?: scrollToPosition(position)
}

fun RecyclerView.smoothScrollToTop() {
    smoothScrollToPosition(0)
}
