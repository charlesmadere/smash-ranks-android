package com.garpr.android.adapters

import android.content.Context
import com.garpr.android.R
import com.garpr.android.models.HeadToHeadMatch
import com.garpr.android.models.LiteTournament
import com.garpr.android.models.WinsLosses

class HeadToHeadAdapter(context: Context) : BaseMultiAdapter(context, LAYOUT_KEY_MAP) {

    companion object {
        private val LAYOUT_KEY_MAP = mapOf<Class<*>, Int>(
                HeadToHeadMatch::class.java to R.layout.item_head_to_head_match,
                LiteTournament::class.java to R.layout.divider_tournament,
                String::class.java to R.layout.item_string,
                WinsLosses::class.java to R.layout.item_wins_losses)
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

}
