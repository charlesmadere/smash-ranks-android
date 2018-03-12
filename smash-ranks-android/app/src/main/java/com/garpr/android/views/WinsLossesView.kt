package com.garpr.android.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.LitePlayer
import com.garpr.android.models.WinsLosses
import kotterknife.bindView
import java.text.NumberFormat

class WinsLossesView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<WinsLosses> {

    private var hasAnimated = false
    private val numberFormat = NumberFormat.getIntegerInstance()

    private val opponentName: TextView by bindView(R.id.tvOpponentName)
    private val opponentWins: TextView by bindView(R.id.tvOpponentWinCount)
    private val playerName: TextView by bindView(R.id.tvPlayerName)
    private val playerWins: TextView by bindView(R.id.tvPlayerWinCount)
    private val winsLossesGraphView: WinsLossesGraphView by bindView(R.id.winsLossesGraphView)


    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            setContent(WinsLosses(LitePlayer("0", "PewPewU"), 8,
                    LitePlayer("1", "Shroomed"), 5))
        }
    }

    private fun performAnimation() {
        alpha = 0f

        animate()
                .alpha(1f)
                .setDuration(resources.getInteger(R.integer.win_losses_animation).toLong())
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
    }

    override fun setContent(content: WinsLosses) {
        if (isInEditMode) {
            hasAnimated = true
        }

        playerName.text = content.player.name
        playerWins.text = numberFormat.format(content.playerWins)

        opponentName.text = content.opponent.name
        opponentWins.text = numberFormat.format(content.opponentWins)

        winsLossesGraphView.setWinsLosses(content, hasAnimated)

        if (hasAnimated) {
            alpha = 1f
        } else {
            hasAnimated = true
            performAnimation()
        }
    }

}
