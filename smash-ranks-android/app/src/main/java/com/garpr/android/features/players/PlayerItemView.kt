package com.garpr.android.features.players

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.garpr.android.features.base.BaseAdapterView
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.views.IdentityFrameLayout
import kotlinx.android.synthetic.main.item_player.view.*
import javax.inject.Inject

class PlayerItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityFrameLayout(context, attrs), BaseAdapterView<AbsPlayer>, View.OnClickListener,
        View.OnLongClickListener {

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
                regionManager.getRegion(context)))
    }

    override fun onLongClick(v: View): Boolean {
        return favoritePlayersManager.showAddOrRemovePlayerDialog(context, identity,
                regionManager.getRegion(context))
    }

    override fun setContent(content: AbsPlayer) {
        identity = content
        name.text = content.name
        refresh()
    }

}
