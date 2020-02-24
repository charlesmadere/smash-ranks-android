package com.garpr.android.features.rankings

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.PreviousRank
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.setTintedImageResource
import com.garpr.android.extensions.truncate
import kotlinx.android.synthetic.main.item_ranking_card.view.*

class RankingItemCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), View.OnClickListener, View.OnLongClickListener {

    private var _player: AbsPlayer? = null

    val player: AbsPlayer
        get() = checkNotNull(_player)

    @ColorInt
    private val loseColor: Int = ContextCompat.getColor(context, R.color.lose)

    @ColorInt
    private val winColor: Int = ContextCompat.getColor(context, R.color.win)

    var listeners: Listeners? = null

    init {
        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    override fun onClick(v: View) {
        listeners?.onClick(this)
    }

    override fun onLongClick(v: View): Boolean {
        listeners?.onLongClick(this)
        return true
    }

    fun setContent(
            player: AbsPlayer,
            isIdentity: Boolean,
            previousRank: PreviousRank,
            rank: String?,
            rating: String?,
            regionDisplayName: String?
    ) {
        _player = player
        name.text = player.name
        name.typeface = if (isIdentity) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

        this.regionDisplayName.visibility = if (regionDisplayName.isNullOrBlank()) {
            this.regionDisplayName.clear()
            GONE
        } else {
            this.regionDisplayName.text = regionDisplayName
            VISIBLE
        }

        when (previousRank) {
            PreviousRank.DECREASE -> {
                previousRankView.setTintedImageResource(R.drawable.ic_arrow_downward_white_18dp, loseColor)
                previousRankView.visibility = VISIBLE
            }

            PreviousRank.GONE -> {
                previousRankView.visibility = GONE
            }

            PreviousRank.INCREASE -> {
                previousRankView.setTintedImageResource(R.drawable.ic_arrow_upward_white_18dp, winColor)
                previousRankView.visibility = VISIBLE
            }

            PreviousRank.INVISIBLE -> {
                previousRankView.visibility = INVISIBLE
            }
        }

        this.rank.text = if (rank.isNullOrBlank()) {
            this.rank.visibility = INVISIBLE
            resources.getText(R.string.question_mark)
        } else {
            this.rank.visibility = VISIBLE
            rank
        }

        this.rating.text = if (rating.isNullOrBlank()) {
            this.rating.visibility = INVISIBLE
            0f.truncate()
        } else {
            this.rating.visibility = VISIBLE
            rating
        }
    }

    interface Listeners {
        fun onClick(v: RankingItemCardView)
        fun onLongClick(v: RankingItemCardView)
    }

}
