package com.garpr.android.adapters

import android.content.Context
import com.garpr.android.R
import com.garpr.android.models.LiteTournament
import com.garpr.android.models.Match
import com.garpr.android.models.Rating
import java.util.*

class PlayerAdapter(context: Context) : BaseMultiAdapter(context, LAYOUT_KEY_MAP) {

    companion object {
        private val LAYOUT_KEY_MAP: MutableMap<Class<*>, Int>

        init {
            LAYOUT_KEY_MAP = HashMap<Class<*>, Int>(4)
            LAYOUT_KEY_MAP.put(LiteTournament::class.java, R.layout.divider_tournament)
            LAYOUT_KEY_MAP.put(Match::class.java, R.layout.item_match)
            LAYOUT_KEY_MAP.put(Rating::class.java, R.layout.item_rating)
            LAYOUT_KEY_MAP.put(String::class.java, R.layout.item_string)
        }
    }

}
