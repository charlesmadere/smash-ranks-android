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

    private var mContent: WinsLosses? = null
    private var mPlayerArcEnd: Float = 0f
    private val mPlayerFillPaint = Paint()
    private val mPlayerStrokePaint = Paint()
    private var mOpponentArcEnd: Float = 0f
    private val mOpponentFillPaint = Paint()
    private val mOpponentStrokePaint = Paint()
    private val mRect = RectF()

    private val mPlayerColumnView: WinsLossesColumnView by bindView(R.id.playerColumnView)
    private val mOpponentColumnView: WinsLossesColumnView by bindView(R.id.opponentColumnView)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleableRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun calculateRects() {
        val c = mContent

        if (height == 0 || width == 0 || c == null) {
            mRect.setEmpty()
            return
        }

        val size: Float = height.toFloat() / 5f
        val start: Float = width.toFloat() / 2f
        mRect.set(start - size, size, start + size, size + (size * 2f))

        val percentPlayerWins: Float
        val percentOpponentWins: Float

        when {
            c.playerWins == 0 -> {
                percentPlayerWins = 0f
                percentOpponentWins = 1f
            }

            c.opponentWins == 0 -> {
                percentPlayerWins = 1f
                percentOpponentWins = 0f
            }

            else -> {
                percentPlayerWins = c.playerWins.toFloat() / (c.playerWins + c.opponentWins).toFloat()
                percentOpponentWins = 1f - percentPlayerWins
            }
        }

        mPlayerArcEnd = percentPlayerWins * 360f
        mOpponentArcEnd = percentOpponentWins * 360f
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

        if (mRect.isEmpty) {
            return
        }

        if (mPlayerArcEnd != 0f) {
            canvas.drawArc(mRect, 90f, mPlayerArcEnd, true, mPlayerFillPaint)
            canvas.drawArc(mRect, 90f, mPlayerArcEnd, true, mPlayerStrokePaint)
        }

        if (mOpponentArcEnd != 0f) {
            canvas.drawArc(mRect, 90f + mPlayerArcEnd, mOpponentArcEnd, true, mOpponentFillPaint)
            canvas.drawArc(mRect, 90f + mPlayerArcEnd, mOpponentArcEnd, true, mOpponentStrokePaint)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        setWillNotDraw(false)

        mPlayerFillPaint.color = ContextCompat.getColor(context, R.color.win_background)
        mPlayerFillPaint.isAntiAlias = true
        mPlayerFillPaint.style = Paint.Style.FILL

        mPlayerStrokePaint.color = ContextCompat.getColor(context, R.color.win)
        mPlayerStrokePaint.isAntiAlias = true
        mPlayerStrokePaint.strokeWidth = resources.getDimension(R.dimen.root_padding_eighth)
        mPlayerStrokePaint.style = Paint.Style.STROKE

        mOpponentFillPaint.color = ContextCompat.getColor(context, R.color.lose_background)
        mOpponentFillPaint.isAntiAlias = true
        mOpponentFillPaint.style = Paint.Style.FILL

        mOpponentStrokePaint.color = ContextCompat.getColor(context, R.color.lose)
        mOpponentStrokePaint.isAntiAlias = true
        mOpponentStrokePaint.strokeWidth = resources.getDimension(R.dimen.root_padding_eighth)
        mOpponentStrokePaint.style = Paint.Style.STROKE

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
        mContent = content
        mPlayerColumnView.setContent(content)
        mOpponentColumnView.setContent(content)
        invalidate()
    }

}
