package com.garpr.android.features.favoritePlayers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.base.BaseAdapterView
import com.garpr.android.features.common.IdentityConstraintLayout
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.RegionManager
import kotlinx.android.synthetic.main.item_favorite_player.view.*
import javax.inject.Inject

class FavoritePlayerItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<FavoritePlayer>,
        View.OnClickListener, View.OnLongClickListener {

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var regionManager: RegionManager


    init {
        setOnClickListener(this)
        setOnLongClickListener(this)

        if (!isInEditMode) {
            appComponent.inject(this)
        }
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
        val identity = this.identity ?: return
        context.startActivity(PlayerActivity.getLaunchIntent(context, identity,
                (identity as FavoritePlayer).region))
    }

    override fun onLongClick(v: View): Boolean {
        return favoritePlayersManager.showAddOrRemovePlayerDialog(context, identity,
                regionManager.getRegion(context))
    }

    override fun setContent(content: FavoritePlayer) {
        identity = content
        name.text = content.name
        region.text = content.region.displayName
        refresh()
    }

}
