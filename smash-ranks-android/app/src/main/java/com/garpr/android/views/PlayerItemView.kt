package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.models.AbsPlayer
import kotterknife.bindView
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

    private val name: TextView by bindView(R.id.tvName)


    override fun identityIsSomeoneElse() {
        super.identityIsSomeoneElse()
        styleTextViewForSomeoneElse(name)
    }

    override fun identityIsUser() {
        super.identityIsUser()
        styleTextViewForUser(name)
    }

    override fun onClick(v: View) {
        identity?.let {
            context.startActivity(PlayerActivity.getLaunchIntent(context, it,
                    regionManager.getRegion(context)))
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
        }

        setOnClickListener(this)
        setOnLongClickListener(this)
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
