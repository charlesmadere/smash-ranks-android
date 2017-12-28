package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorRes
import android.support.annotation.StyleableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import com.garpr.android.R
import com.garpr.android.models.WinsLosses

class WinsLossesGraphView : View {

    private lateinit var playerPalette: GraphPalette
    private lateinit var opponentPalette: GraphPalette
    private val rect = RectF()
    private var winsLosses: WinsLosses? = null


    companion object {
        private const val OVERSHOOT_TENSION: Float = 3.8f
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleableRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun calculateRects() {
        val winsLosses = this.winsLosses
        val height = this.height
        val width = this.width

        if (winsLosses == null || height == 0 || width == 0) {
            rect.setEmpty()
            return
        }

        val size: Float = width.toFloat()
        rect.set(paddingStart.toFloat(), paddingTop + (height.toFloat() / 2f) - (size / 2f),
                paddingRight + size, paddingBottom + (height.toFloat() / 2f) + (size / 2f))

        val percentPlayerWins: Float
        val percentOpponentWins: Float

        when {
            winsLosses.playerWins == 0 -> {
                percentPlayerWins = 0f
                percentOpponentWins = 1f
            }

            winsLosses.opponentWins == 0 -> {
                percentPlayerWins = 1f
                percentOpponentWins = 0f
            }

            else -> {
                percentPlayerWins = winsLosses.playerWins.toFloat() /
                        (winsLosses.playerWins + winsLosses.opponentWins).toFloat()
                percentOpponentWins = 1f - percentPlayerWins
            }
        }

        playerPalette.arcEnd = percentPlayerWins * 360f
        opponentPalette.arcEnd = percentOpponentWins * 360f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (rect.isEmpty) {
            return
        }

        if (playerPalette.arcEnd != 0f) {
            canvas.drawArc(rect, 90f, playerPalette.arcEnd, true,
                    playerPalette.fillPaint)
            canvas.drawArc(rect, 90f, playerPalette.arcEnd, true,
                    playerPalette.strokePaint)
        }

        if (opponentPalette.arcEnd != 0f) {
            canvas.drawArc(rect, 90f + playerPalette.arcEnd, opponentPalette.arcEnd,
                    true, opponentPalette.fillPaint)
            canvas.drawArc(rect, 90f + playerPalette.arcEnd, opponentPalette.arcEnd,
                    true, opponentPalette.strokePaint)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        playerPalette = GraphPalette(R.color.win_background, R.color.win)
        opponentPalette = GraphPalette(R.color.lose_background, R.color.lose)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculateRects()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateRects()
    }

    private fun performScaleAnimation() {
        scaleX = 0f
        scaleY = 0f

        animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(resources.getInteger(R.integer.win_losses_animation).toLong())
                .setInterpolator(OvershootInterpolator(OVERSHOOT_TENSION))
                .start()
    }

    internal fun setWinsLosses(winsLosses: WinsLosses, animate: Boolean) {
        this.winsLosses = winsLosses

        if (animate) {
            performScaleAnimation()
        } else {
            scaleX = 1f
            scaleY = 1f
            calculateRects()
        }
    }

    private inner class GraphPalette(
            @ColorRes private val fillColorResId: Int,
            @ColorRes private val strokeColorResId: Int
    ) {
        internal var arcEnd: Float = 0f
        internal val fillPaint = Paint()
        internal val strokePaint = Paint()

        init {
            fillPaint.color = ContextCompat.getColor(context, fillColorResId)
            fillPaint.isAntiAlias = true
            fillPaint.style = Paint.Style.FILL

            strokePaint.color = ContextCompat.getColor(context, strokeColorResId)
            strokePaint.isAntiAlias = true
            strokePaint.strokeWidth = context.resources.getDimension(R.dimen.root_padding_eighth)
            strokePaint.style = Paint.Style.STROKE
        }
    }

}