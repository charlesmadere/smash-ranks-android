package com.garpr.android.features.setIdentity

import androidx.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.features.base.BaseAdapter
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.PlayersBundle

class PlayersSelectionAdapter : BaseAdapter<AbsPlayer>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int) = R.layout.item_player_selection

    fun set(content: PlayersBundle?) {
        set(content?.players)
    }

}
