package com.garpr.android.features.rankings

import androidx.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.features.common.adapters.BaseAdapter

class RankingsAdapter : BaseAdapter<RankedPlayer>() {

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
