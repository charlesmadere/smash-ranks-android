package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.LinearLayout
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.LitePlayer
import com.garpr.android.models.WinsLosses
import kotterknife.bindView

class WinsLossesView : LinearLayout, BaseAdapterView<WinsLosses> {

    private var playerArcEnd: Float = 0f
    private val playerFillPaint = Paint()
    private val playerStrokePaint = Paint()
    private var opponentArcEnd: Float = 0f
    private val opponentFillPaint = Paint()
    private val opponentStrokePaint = Paint()
    private val rect = RectF()

    private val playerColumnView: WinsLossesColumnView by bindView(R.id.playerColumnView)
    private val opponentColumnView: WinsLossesColumnView by bindView(R.id.opponentColumnView)


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

        val size: Float = height.toFloat() / 5f
        val start: Float = width.toFloat() / 2f
        rect.set(start - size, size, start + size, size + (size * 2f))

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

        playerArcEnd = percentPlayerWins * 360f
        opponentArcEnd = percentOpponentWins * 360f
    }

    override fun invalidate() {
        calculateRects()
        super.invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (rect.isEmpty) {
            return
        }

        if (playerArcEnd != 0f) {
            canvas.drawArc(rect, 90f, playerArcEnd, true, playerFillPaint)
            canvas.drawArc(rect, 90f, playerArcEnd, true, playerStrokePaint)
        }

        if (opponentArcEnd != 0f) {
            canvas.drawArc(rect, 90f + playerArcEnd, opponentArcEnd, true,
                    opponentFillPaint)
            canvas.drawArc(rect, 90f + playerArcEnd, opponentArcEnd, true,
                    opponentStrokePaint)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        setWillNotDraw(false)

        playerFillPaint.color = ContextCompat.getColor(context, R.color.win_background)
        playerFillPaint.isAntiAlias = true
        playerFillPaint.style = Paint.Style.FILL

        playerStrokePaint.color = ContextCompat.getColor(context, R.color.win)
        playerStrokePaint.isAntiAlias = true
        playerStrokePaint.strokeWidth = resources.getDimension(R.dimen.root_padding_eighth)
        playerStrokePaint.style = Paint.Style.STROKE

        opponentFillPaint.color = ContextCompat.getColor(context, R.color.lose_background)
        opponentFillPaint.isAntiAlias = true
        opponentFillPaint.style = Paint.Style.FILL

        opponentStrokePaint.color = ContextCompat.getColor(context, R.color.lose)
        opponentStrokePaint.isAntiAlias = true
        opponentStrokePaint.strokeWidth = resources.getDimension(R.dimen.root_padding_eighth)
        opponentStrokePaint.style = Paint.Style.STROKE

        if (isInEditMode) {
            setContent(WinsLosses(LitePlayer("0", "PewPewU"), 8,
                    LitePlayer("1", "Shroomed"), 5))
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        invalidate()
    }

    override fun setContent(content: WinsLosses) {
        winsLosses = content
    }

    private var winsLosses: WinsLosses? = null
        set(value) {
            field = value
            playerColumnView.winsLosses = value
            opponentColumnView.winsLosses = value
            invalidate()
        }

}
