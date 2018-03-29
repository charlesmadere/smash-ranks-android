package com.garpr.android.extensions

import android.support.v7.widget.RecyclerView

fun RecyclerView.smoothScrollToTop() {
    smoothScrollToPosition(0)
}
