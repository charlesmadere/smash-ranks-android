package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.LitePlayer
import com.garpr.android.models.WinsLosses
import kotterknife.bindView

class WinsLossesView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), BaseAdapterView<WinsLosses> {

    private var hasAnimated = true

    private val playerColumnView: WinsLossesColumnView by bindView(R.id.playerColumnView)
    private val opponentColumnView: WinsLossesColumnView by bindView(R.id.opponentColumnView)
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

        playerColumnView.winsLosses = content
        opponentColumnView.winsLosses = content
        winsLossesGraphView.setWinsLosses(content, hasAnimated)

        if (hasAnimated) {
            alpha = 1f
        } else {
            hasAnimated = false
            performAnimation()
        }
    }

}
