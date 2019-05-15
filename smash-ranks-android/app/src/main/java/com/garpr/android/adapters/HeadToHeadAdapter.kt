package com.garpr.android.adapters

import com.garpr.android.R
import com.garpr.android.data.models.HeadToHeadMatch
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.WinsLosses

class HeadToHeadAdapter : BaseMultiAdapter(LAYOUT_KEY_MAP) {

    companion object {
        private val LAYOUT_KEY_MAP = mapOf<Class<*>, Int>(
                HeadToHeadMatch::class.java to R.layout.item_head_to_head_match,
                LiteTournament::class.java to R.layout.divider_tournament,
                String::class.java to R.layout.item_string,
                WinsLosses::class.java to R.layout.item_wins_losses
        )
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return when (val item = getItem(position)) {
            is String -> {
                Long.MIN_VALUE
            }

            is WinsLosses -> {
                Long.MIN_VALUE + 1L
            }

            else -> {
                item.hashCode().toLong()
            }
        }
    }

}
