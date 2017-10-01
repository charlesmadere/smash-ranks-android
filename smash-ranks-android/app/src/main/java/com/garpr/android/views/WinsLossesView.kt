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
    private val mPlayerPaint = Paint()
    private var mOpponentArcEnd: Float = 0f
    private val mOpponentPaint = Paint()
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

        val size: Float = height.toFloat() / 4f

        mRect.set(size, size, size * 3f, size * 3f)

        val percentOpponentWins: Float = when {
            c.playerWins == 0 -> 1f
            c.opponentWins == 0 -> 0f
            else -> 1f - (c.playerWins.toFloat() / (c.playerWins + c.opponentWins).toFloat())
        }

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

        canvas.drawArc(mRect, 0f, 360f, true, mPlayerPaint)
        canvas.drawArc(mRect, 0f, mOpponentArcEnd, true, mOpponentPaint)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setWillNotDraw(false)

        if (isInEditMode) {
            setContent(WinsLosses(LitePlayer("0", "PewPewU"), 8,
                    LitePlayer("1", "Shroomed"), 5))
        }

        mPlayerPaint.color = ContextCompat.getColor(context, R.color.win)
        mPlayerPaint.isAntiAlias = true
        mPlayerPaint.isDither = true
        mPlayerPaint.style = Paint.Style.FILL

        mOpponentPaint.color = ContextCompat.getColor(context, R.color.lose)
        mOpponentPaint.isAntiAlias = true
        mOpponentPaint.isDither = true
        mOpponentPaint.style = Paint.Style.FILL
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
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
