package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes

import com.garpr.android.R
import com.garpr.android.models.AbsTournament

class TournamentsAdapter(context: Context) : BaseAdapter<AbsTournament>(context) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int) = R.layout.item_tournament

}
