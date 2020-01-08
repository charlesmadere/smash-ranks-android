package com.garpr.android.features.tournament

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
import com.garpr.android.data.models.FullTournament
import com.garpr.android.extensions.getAttrColor
import kotlinx.android.synthetic.main.item_tournament_match.view.*

class TournamentMatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), View.OnClickListener {

    private val originalBackground: Drawable? = background

    private var _match: FullTournament.Match? = null

    val match: FullTournament.Match
        get() = checkNotNull(_match)

    @ColorInt
    private val exclusionColor: Int = context.getAttrColor(android.R.attr.textColorSecondary)

    @ColorInt
    private val loseColor: Int = ContextCompat.getColor(context, R.color.lose)

    @ColorInt
    private val winColor: Int = ContextCompat.getColor(context, R.color.win)

    var listener: Listener? = null

    interface Listener {
        fun onClick(v: TournamentMatchItemView)
    }

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    fun setContent(match: FullTournament.Match, winnerIsIdentity: Boolean, loserIsIdentity: Boolean) {
        _match = match

        loserName.text = match.loserName
        winnerName.text = match.winnerName

        if (match.isExcluded) {
            loserName.setTextColor(exclusionColor)
            winnerName.setTextColor(exclusionColor)
        } else {
            loserName.setTextColor(loseColor)
            winnerName.setTextColor(winColor)
        }

        if (winnerIsIdentity) {
            winnerName.typeface = Typeface.DEFAULT_BOLD
            loserName.typeface = Typeface.DEFAULT
            setBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
        } else if (loserIsIdentity) {
            winnerName.typeface = Typeface.DEFAULT
            loserName.typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
        } else {
            winnerName.typeface = Typeface.DEFAULT
            loserName.typeface = Typeface.DEFAULT
            ViewCompat.setBackground(this, originalBackground)
        }
    }

}
