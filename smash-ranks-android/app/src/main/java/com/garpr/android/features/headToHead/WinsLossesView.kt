package com.garpr.android.features.headToHead

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.R
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.WinsLosses
import com.garpr.android.extensions.getLong
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.misc.AnimationUtils
import kotlinx.android.synthetic.main.item_wins_losses.view.*
import java.text.NumberFormat

class WinsLossesView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<WinsLosses> {

    private var hasAnimated = false

    companion object {
        private val NUMBER_FORMAT = NumberFormat.getIntegerInstance()
    }

    init {
        if (isInEditMode) {
            hasAnimated = true
            setContent(WinsLosses(LitePlayer("0", "PewPewU"), 8,
                    LitePlayer("1", "Shroomed"), 5))
        }
    }

    private fun performAnimation() {
        alpha = 0f

        animate()
                .alpha(1f)
                .setDuration(resources.getLong(R.integer.win_losses_animation))
                .setInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR)
                .start()
    }

    override fun setContent(content: WinsLosses) {
        playerName.text = content.player.name
        playerWins.text = NUMBER_FORMAT.format(content.playerWins)

        opponentName.text = content.opponent.name
        opponentWins.text = NUMBER_FORMAT.format(content.opponentWins)

        winsLossesGraph.setContent(Pair(content, hasAnimated))

        if (hasAnimated) {
            alpha = 1f
        } else {
            hasAnimated = true
            performAnimation()
        }
    }

}
