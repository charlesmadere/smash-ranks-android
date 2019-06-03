package com.garpr.android.features.player

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.Match
import com.garpr.android.data.models.MatchResult
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.features.base.BaseAdapterView
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.views.IdentityFrameLayout
import kotlinx.android.synthetic.main.item_match.view.*
import javax.inject.Inject

class MatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityFrameLayout(context, attrs), BaseAdapterView<Match>, View.OnClickListener,
        View.OnLongClickListener {

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var regionManager: RegionManager


    interface OnClickListener {
        fun onClick(v: MatchItemView)
    }

    init {
        setOnClickListener(this)
        setOnLongClickListener(this)

        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    override fun clear() {
        name.clear()
        super.clear()
    }

    override fun identityIsSomeoneElse() {
        super.identityIsSomeoneElse()
        styleTextViewForSomeoneElse(name)
    }

    override fun identityIsUser() {
        super.identityIsUser()
        styleTextViewForUser(name)
    }

    var match: Match? = null
        private set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            identity = value.opponent
            name.text = value.opponent.name

            when (value.result) {
                MatchResult.EXCLUDED -> name.setTextColor(context.getAttrColor(android.R.attr.textColorSecondary))
                MatchResult.LOSE -> name.setTextColor(ContextCompat.getColor(context, R.color.lose))
                MatchResult.WIN -> name.setTextColor(ContextCompat.getColor(context, R.color.win))
            }

            refresh()
        }

    override fun onClick(v: View) {
        val match = this.match ?: return
        val activity = this.activity

        if (activity is OnClickListener) {
            activity.onClick(this)
        } else {
            val opponent = match.opponent
            context.startActivity(PlayerActivity.getLaunchIntent(context, opponent.id,
                    regionManager.getRegion(context)))
        }
    }

    override fun onLongClick(v: View): Boolean {
        return favoritePlayersManager.showAddOrRemovePlayerDialog(context, match?.opponent,
                regionManager.getRegion(context))
    }

    override fun setContent(content: Match) {
        match = content
    }

}
