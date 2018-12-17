package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.setTintedImageResource
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.PreviousRankUtils
import com.garpr.android.misc.Timber
import com.garpr.android.models.RankedPlayer
import kotlinx.android.synthetic.main.item_ranking.view.*
import java.text.NumberFormat
import javax.inject.Inject

class RankingItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<RankedPlayer>,
        View.OnClickListener, View.OnLongClickListener {

    private val numberFormat = NumberFormat.getIntegerInstance()

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var previousRankUtils: PreviousRankUtils

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var timber: Timber


    override fun clear() {
        super.clear()
        previousRankView.clear()
        rank.clear()
        name.clear()
        rating.clear()
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

    override fun refresh() {
        super.refresh()

        val player = identity as? RankedPlayer

        if (player == null) {
            clear()
            return
        }

        val rankInfo = previousRankUtils.getRankInfo(player)

        if (rankInfo == null) {
            previousRankView.clear()
            previousRankView.visibility = View.GONE
        } else {
            @DrawableRes val drawableResId: Int
            @ColorRes val tintResId: Int

            when (rankInfo) {
                PreviousRankUtils.Info.DECREASE -> {
                    drawableResId = R.drawable.ic_arrow_downward_white_18dp
                    tintResId = R.color.lose
                }

                PreviousRankUtils.Info.INCREASE -> {
                    drawableResId = R.drawable.ic_arrow_upward_white_18dp
                    tintResId = R.color.win
                }
            }

            previousRankView.setTintedImageResource(drawableResId,
                    ContextCompat.getColor(context, tintResId))
            previousRankView.visibility = View.VISIBLE
        }

        rank.text = numberFormat.format(player.rank)
        name.text = player.name
        rating.text = MiscUtils.truncateFloat(player.rating)
    }

    override fun setContent(content: RankedPlayer) {
        identity = content
        refresh()
    }

}
