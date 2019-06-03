package com.garpr.android.features.tournaments

import androidx.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapter
import com.garpr.android.data.models.AbsTournament

class TournamentsAdapter : BaseAdapter<AbsTournament>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int) = R.layout.item_tournament

}
