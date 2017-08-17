package com.garpr.android.adapters

import android.content.Context
import com.garpr.android.R
import com.garpr.android.models.LiteTournament
import com.garpr.android.models.Match
import com.garpr.android.models.Rating

class PlayerAdapter(context: Context) : BaseMultiAdapter(context, LAYOUT_KEY_MAP) {

    companion object {
        private val LAYOUT_KEY_MAP: Map<Class<*>, Int> = mapOf(
                LiteTournament::class.java to R.layout.divider_tournament,
                Match::class.java to R.layout.item_match,
                Rating::class.java to R.layout.item_rating,
                String::class.java to R.layout.item_string)
    }

}
