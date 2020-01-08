package com.garpr.android.features.headToHead

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.WinsLosses
import com.garpr.android.extensions.getLong
import com.garpr.android.misc.AnimationUtils
import kotlinx.android.synthetic.main.item_wins_losses.view.*
import java.text.NumberFormat

class WinsLossesView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var _opponent: AbsPlayer? = null

    val opponent: AbsPlayer
        get() = checkNotNull(_opponent)

    var _player: AbsPlayer? = null

    val player: AbsPlayer
        get() = checkNotNull(_player)

    private var hasAnimated = false
    var listeners: Listeners? = null

    private val opponentClickListener = OnClickListener {
        listeners?.onOpponentClick(this)
    }

    private val playerClickListener = OnClickListener {
        listeners?.onPlayerClick(this)
    }

    interface Listeners {
        fun onOpponentClick(v: WinsLossesView)
        fun onPlayerClick(v: WinsLossesView)
    }

    companion object {
        private val NUMBER_FORMAT = NumberFormat.getIntegerInstance()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        playerSelectionArea.setOnClickListener(playerClickListener)
        opponentSelectionArea.setOnClickListener(opponentClickListener)
    }

    private fun performAnimation() {
        alpha = 0f

        animate()
                .alpha(1f)
                .setDuration(resources.getLong(R.integer.win_losses_animation))
                .setInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR)
                .start()
    }

    fun setContent(content: WinsLosses) {
        _player = content.player
        playerName.text = content.player.name
        playerWins.text = NUMBER_FORMAT.format(content.playerWins)

        _opponent = content.opponent
        opponentName.text = content.opponent.name
        opponentWins.text = NUMBER_FORMAT.format(content.opponentWins)

        winsLossesGraph.setContent(content, hasAnimated)

        if (hasAnimated) {
            alpha = 1f
        } else {
            hasAnimated = true
            performAnimation()
        }
    }

}
