package com.garpr.android.adapters

import android.content.Context
import androidx.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.models.RankedPlayer
import com.garpr.android.models.RankingsBundle

class RankingsAdapter(context: Context) : BaseAdapter<RankedPlayer>(context) {

    @LayoutRes
    private var layoutResId: Int? = null


    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        return layoutResId ?: throw RuntimeException("layoutResId is null")
    }

    fun set(bundle: RankingsBundle?) {
        if (bundle?.rankings?.isNotEmpty() == true) {
            layoutResId = if (bundle.hasPreviousRank()) {
                R.layout.item_ranking_with_previous_rank
            } else {
                R.layout.item_ranking
            }

            set(bundle.rankings)
        } else {
            clear()
        }
    }

}
