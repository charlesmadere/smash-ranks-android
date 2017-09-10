package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.PlayersBundle

class FavoritePlayersAdapter(context: Context) : BaseAdapter<AbsPlayer>(context) {

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
