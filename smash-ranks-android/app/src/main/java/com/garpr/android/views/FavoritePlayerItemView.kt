package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.misc.FavoritePlayersManager
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.FavoritePlayer
import kotterknife.bindView
import javax.inject.Inject

class FavoritePlayerItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
): IdentityConstraintLayout(context, attrs), BaseAdapterView<FavoritePlayer>,
        View.OnClickListener, View.OnLongClickListener {

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var regionManager: RegionManager

    private val name: TextView by bindView(R.id.tvName)
    private val region: TextView by bindView(R.id.tvRegion)


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

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    override fun onLongClick(v: View): Boolean {
        return favoritePlayersManager.showAddOrRemovePlayerDialog(context, identity,
                regionManager.getRegion(context))
    }

    override fun setContent(content: FavoritePlayer) {
        identity = content
        name.text = content.name
        region.text = content.region.displayName
        refreshIdentity()
    }

}
