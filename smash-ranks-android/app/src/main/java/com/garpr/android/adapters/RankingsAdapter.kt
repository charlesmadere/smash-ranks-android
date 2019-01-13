package com.garpr.android.adapters

import android.content.Context
import androidx.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.RankingsBundle

class RankingsAdapter(context: Context) : BaseAdapter<RankedPlayer>(context) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int) = R.layout.item_ranking

    fun set(bundle: RankingsBundle?) {
        if (bundle?.rankings?.isNotEmpty() == true) {
            set(bundle.rankings)
        } else {
            clear()
        }
    }

}
