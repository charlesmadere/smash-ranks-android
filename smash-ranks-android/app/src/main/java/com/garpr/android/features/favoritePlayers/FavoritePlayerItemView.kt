package com.garpr.android.features.favoritePlayers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.features.common.views.IdentityConstraintLayout
import kotlinx.android.synthetic.main.item_favorite_player.view.*

class FavoritePlayerItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<FavoritePlayer>,
        View.OnClickListener, View.OnLongClickListener {

    val favoritePlayer: FavoritePlayer
        get() = requireNotNull(identity as FavoritePlayer)

    var listeners: Listeners? = null

    interface Listeners {
        fun onClick(v: FavoritePlayerItemView)
        fun onLongClick(v: FavoritePlayerItemView)
    }

    init {
        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    override fun identityIsSomeoneElse() {
        super.identityIsSomeoneElse()
        styleTextViewForSomeoneElse(name)
    }

    override fun identityIsUser() {
        super.identityIsUser()
        styleTextViewForUser(name)
    }

    override fun onClick(v: View) {
        listeners?.onClick(this)
    }

    override fun onLongClick(v: View): Boolean {
        listeners?.onLongClick(this)
        return true
    }

    override fun setContent(content: FavoritePlayer) {
        identity = content
        name.text = content.name
        region.text = content.region.displayName
        refresh()
    }

}
