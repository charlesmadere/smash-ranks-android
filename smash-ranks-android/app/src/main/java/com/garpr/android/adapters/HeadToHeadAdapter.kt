package com.garpr.android.adapters

import android.content.Context
import com.garpr.android.R
import com.garpr.android.models.LiteTournament
import com.garpr.android.models.Match
import com.garpr.android.models.WinsLosses

class HeadToHeadAdapter(context: Context) : BaseMultiAdapter(context, LAYOUT_KEY_MAP) {

    companion object {
        private val LAYOUT_KEY_MAP: MutableMap<Class<*>, Int> = mutableMapOf()

        init {
            LAYOUT_KEY_MAP.put(LiteTournament::class.java, R.layout.divider_tournament)
            LAYOUT_KEY_MAP.put(Match::class.java, R.layout.item_match)
            LAYOUT_KEY_MAP.put(String::class.java, R.layout.item_string)
            LAYOUT_KEY_MAP.put(WinsLosses::class.java, R.layout.item_wins_losses)
        }
    }

}
