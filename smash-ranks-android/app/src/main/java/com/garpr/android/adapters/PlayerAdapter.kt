package com.garpr.android.adapters

import android.content.Context
import com.garpr.android.R
import com.garpr.android.models.LiteTournament
import com.garpr.android.models.Match
import com.garpr.android.models.Rating

class PlayerAdapter(context: Context) : BaseMultiAdapter(context, LAYOUT_KEY_MAP) {

    companion object {
        private val LAYOUT_KEY_MAP = mapOf<Class<*>, Int>(
                LiteTournament::class.java to R.layout.divider_tournament,
                Match::class.java to R.layout.item_match,
                Rating::class.java to R.layout.item_rating,
                String::class.java to R.layout.item_string)
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

}
