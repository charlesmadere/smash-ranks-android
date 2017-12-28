package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleableRes
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.LitePlayer
import com.garpr.android.models.WinsLosses
import kotterknife.bindView

class WinsLossesView : RelativeLayout, BaseAdapterView<WinsLosses> {

    private var animate = true

    private val playerColumnView: WinsLossesColumnView by bindView(R.id.playerColumnView)
    private val opponentColumnView: WinsLossesColumnView by bindView(R.id.opponentColumnView)
    private val winsLossesGraphView: WinsLossesGraphView by bindView(R.id.winsLossesGraphView)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleableRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            setContent(WinsLosses(LitePlayer("0", "PewPewU"), 8,
                    LitePlayer("1", "Shroomed"), 5))
        }
    }

    private fun performAlphaAnimation() {
        alpha = 0f

        animate()
                .alpha(1f)
                .setDuration(resources.getInteger(R.integer.win_losses_animation).toLong())
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
    }

    override fun setContent(content: WinsLosses) {
        if (isInEditMode) {
            animate = false
        }

        playerColumnView.winsLosses = content
        opponentColumnView.winsLosses = content
        winsLossesGraphView.setWinsLosses(content, animate)

        if (animate) {
            animate = false
            performAlphaAnimation()
        } else {
            alpha = 1f
        }
    }

}
