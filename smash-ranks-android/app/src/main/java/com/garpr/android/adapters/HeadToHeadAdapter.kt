package com.garpr.android.adapters

import android.content.Context
import com.garpr.android.R
import com.garpr.android.models.HeadToHeadMatch
import com.garpr.android.models.LiteTournament
import com.garpr.android.models.WinsLosses

class HeadToHeadAdapter(
        context: Context
) : BaseMultiAdapter(
        context,
        LAYOUT_KEY_MAP
) {

    private val ids = mutableMapOf<Any, Int>()


    companion object {
        private val LAYOUT_KEY_MAP: Map<Class<*>, Int> = mapOf(
                HeadToHeadMatch::class.java to R.layout.item_head_to_head_match,
                LiteTournament::class.java to R.layout.divider_tournament,
                String::class.java to R.layout.item_string,
                WinsLosses::class.java to R.layout.item_wins_losses)
    }

    init {
        setHasStableIds(true)
    }

    private fun buildIds() {
        for (i in 0 until itemCount) {
            ids.put(getItem(i), i)
        }
    }

    override fun clear() {
        super.clear()
        ids.clear()
    }

    override fun getItemId(position: Int): Long {
        return ids[getItem(position)]?.toLong() ?: throw NoSuchElementException(
                "element at position $position doesn't exist! (itemCount: $itemCount)")
    }

    override fun set(items: List<Any>?) {
        super.set(items)

        if (ids.isEmpty()) {
            buildIds()
        }
    }

}
