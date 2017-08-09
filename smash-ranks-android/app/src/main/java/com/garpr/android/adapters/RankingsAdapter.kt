package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.models.RankedPlayer
import com.garpr.android.models.RankingsBundle

class RankingsAdapter(context: Context) : BaseAdapter<RankedPlayer>(context) {

    @LayoutRes
    private var mLayoutResId: Int = 0


    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        return mLayoutResId
    }

    fun set(bundle: RankingsBundle?) {
        if (bundle?.rankings?.isNotEmpty() == true) {
            if (bundle.hasPreviousRank()) {
                mLayoutResId = R.layout.item_ranking_with_previous_rank
            } else {
                mLayoutResId = R.layout.item_ranking
            }

            set(bundle.rankings)
        } else {
            clear()
        }
    }

}
