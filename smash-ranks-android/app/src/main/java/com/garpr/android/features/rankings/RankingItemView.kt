package com.garpr.android.features.rankings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.setTintedImageResource
import com.garpr.android.extensions.truncate
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.features.common.views.IdentityConstraintLayout
import kotlinx.android.synthetic.main.item_ranking.view.*
import org.koin.core.inject
import java.text.NumberFormat

class RankingItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<RankedPlayer>,
        View.OnClickListener, View.OnLongClickListener {

    var listeners: Listeners? = null
    private val numberFormat = NumberFormat.getIntegerInstance()

    val rankedPlayer: RankedPlayer
        get() = requireNotNull(identity as RankedPlayer)

    protected val previousRankUtils: PreviousRankUtils by inject()

    interface Listeners {
        fun onClick(v: RankingItemView)
        fun onLongClick(v: RankingItemView)
    }

    init {
        setOnClickListener(this)
        setOnLongClickListener(this)
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
        listeners?.onClick(this)
    }

    override fun onLongClick(v: View): Boolean {
        listeners?.onLongClick(this)
        return true
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
