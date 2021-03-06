package com.garpr.android.features.headToHead

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.WinsLosses
import com.garpr.android.extensions.getLong

class WinsLossesGraphView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : View(context, attrs) {

    private val playerPalette = GraphPalette(context, R.color.win_background, R.color.win)
    private val opponentPalette = GraphPalette(context, R.color.lose_background, R.color.lose)
    private val rect = RectF()
    private var winsLosses: WinsLosses? = null

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

        val winLossPercentages = winsLosses.winLossPercentages
        playerPalette.arcEnd = winLossPercentages[0] * 360f
        opponentPalette.arcEnd = winLossPercentages[1] * 360f
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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculateRects()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateRects()
    }

    private fun performAnimation() {
        scaleX = 0f
        scaleY = 0f

        animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(resources.getLong(R.integer.win_losses_animation))
                .setInterpolator(INTERPOLATOR)
                .start()
    }

    fun setContent(winsLosses: WinsLosses, hasAnimated: Boolean) {
        this.winsLosses = winsLosses

        if (hasAnimated) {
            scaleX = 1f
            scaleY = 1f
            calculateRects()
        } else {
            performAnimation()
        }
    }

    companion object {
        private val INTERPOLATOR: Interpolator = OvershootInterpolator(3.8f)
    }

    private class GraphPalette(
            context: Context,
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
