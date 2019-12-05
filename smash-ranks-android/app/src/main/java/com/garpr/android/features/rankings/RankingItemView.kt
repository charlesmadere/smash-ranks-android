package com.garpr.android.features.rankings

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.PreviousRank
import com.garpr.android.extensions.setTintedImageResource
import kotlinx.android.synthetic.main.item_ranking.view.*

class RankingItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), View.OnClickListener, View.OnLongClickListener {

    private var _player: AbsPlayer? = null

    val player: AbsPlayer
        get() = checkNotNull(_player)

    private val originalBackground: Drawable? = background

    @ColorInt
    private val cardBackgroundColor: Int = ContextCompat.getColor(context, R.color.card_background)

    @ColorInt
    private val loseColor: Int = ContextCompat.getColor(context, R.color.lose)

    @ColorInt
    private val winColor: Int = ContextCompat.getColor(context, R.color.win)

    var listeners: Listeners? = null

    interface Listeners {
        fun onClick(v: RankingItemView)
        fun onLongClick(v: RankingItemView)
    }

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
            rank: String,
            rating: String
    ) {
        _player = player
        name.text = player.name

        if (isIdentity) {
            name.typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(cardBackgroundColor)
        } else {
            name.typeface = Typeface.DEFAULT
            ViewCompat.setBackground(this, originalBackground)
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

        this.rank.text = rank
        this.rating.text = rating
    }

}
