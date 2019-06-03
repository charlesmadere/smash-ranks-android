package com.garpr.android.features.players

import androidx.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapter
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.PlayersBundle

class PlayersAdapter : BaseAdapter<AbsPlayer>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int) = R.layout.item_player

    fun set(content: FullTournament?) {
        set(content?.players)
    }

    fun set(content: PlayersBundle?) {
        set(content?.players)
    }

}
