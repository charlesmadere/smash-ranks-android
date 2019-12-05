package com.garpr.android.extensions

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.smoothScrollToTop() {
    smoothScrollToPosition(0)
}
