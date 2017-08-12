package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes

import com.garpr.android.R
import com.garpr.android.models.FullTournament

class TournamentMatchesAdapter(context: Context) : BaseAdapter<FullTournament.Match>(context) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_tournament_match
    }

    fun set(content: FullTournament?) {
        set(content?.matches)
    }

}
