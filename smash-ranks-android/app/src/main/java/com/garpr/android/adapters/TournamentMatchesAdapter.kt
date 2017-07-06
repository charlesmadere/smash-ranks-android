package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes

import com.garpr.android.R
import com.garpr.android.models.FullTournament

class TournamentMatchesAdapter(context: Context) : BaseAdapter<FullTournament.Match>(context) {

    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_tournament_match
    }

}
