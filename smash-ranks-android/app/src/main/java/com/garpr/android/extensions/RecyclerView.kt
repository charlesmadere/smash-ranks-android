package com.garpr.android.extensions

import androidx.recyclerview.widget.RecyclerView
import com.garpr.android.views.TournamentInfoItemView

fun RecyclerView.findTournamentInfoItemViewChild(): TournamentInfoItemView? {
    repeat(childCount) {
        val child = getChildAt(it)

        if (child is TournamentInfoItemView) {
            return child
        }
    }

    return null
}

fun RecyclerView.smoothScrollToTop() {
    smoothScrollToPosition(0)
}
