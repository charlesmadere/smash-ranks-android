package com.garpr.android.features.favoritePlayers

import androidx.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapter
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.PlayersBundle

class FavoritePlayersAdapter : BaseAdapter<AbsPlayer>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int) = R.layout.item_favorite_player

    fun set(content: PlayersBundle?) {
        set(content?.players)
    }

}
