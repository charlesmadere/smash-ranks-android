package com.garpr.android.features.rankings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.fragmentManager
import com.garpr.android.extensions.setTintedImageResource
import com.garpr.android.extensions.truncate
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.features.common.views.IdentityConstraintLayout
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.misc.PreviousRankUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.RegionRepository
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
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository

    @Inject
    protected lateinit var previousRankUtils: PreviousRankUtils

    @Inject
    protected lateinit var regionRepository: RegionRepository

    @Inject
    protected lateinit var timber: Timber


    init {
        setOnClickListener(this)
        setOnLongClickListener(this)

        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

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
                regionRepository.getRegion(context)))
    }

    override fun onLongClick(v: View): Boolean {
        return favoritePlayersRepository.showAddOrRemovePlayerDialog(fragmentManager, identity,
                regionRepository.getRegion(context))
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
            when (rankInfo) {
                PreviousRankUtils.Info.DECREASE -> {
                    previousRankView.setTintedImageResource(R.drawable.ic_arrow_downward_white_18dp,
                            ContextCompat.getColor(context, R.color.lose))
                }

                PreviousRankUtils.Info.INCREASE -> {
                    previousRankView.setTintedImageResource(R.drawable.ic_arrow_upward_white_18dp,
                            ContextCompat.getColor(context, R.color.win))
                }

                PreviousRankUtils.Info.NO_CHANGE -> {
                    previousRankView.clear()
                }
            }

            previousRankView.visibility = View.VISIBLE
        }

        rank.text = numberFormat.format(player.rank)
        name.text = player.name
        rating.text = player.rating.truncate()
    }

    override fun setContent(content: RankedPlayer) {
        identity = content
        refresh()
    }

}
