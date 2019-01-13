package com.garpr.android.adapters

import android.content.Context
import com.garpr.android.R
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.Match

class PlayerAdapter(context: Context) : BaseMultiAdapter(context, LAYOUT_KEY_MAP) {

    companion object {
        private val LAYOUT_KEY_MAP = mapOf<Class<*>, Int>(
                FullPlayer::class.java to R.layout.item_player_profile,
                LiteTournament::class.java to R.layout.divider_tournament,
                Match::class.java to R.layout.item_match,
                String::class.java to R.layout.item_string)
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

}
