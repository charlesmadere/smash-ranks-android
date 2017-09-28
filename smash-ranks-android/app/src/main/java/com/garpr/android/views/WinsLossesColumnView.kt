package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorRes
import android.support.annotation.StyleableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.WinsLosses
import kotterknife.bindView
import java.text.NumberFormat

class WinsLossesColumnView : LinearLayout, BaseAdapterView<WinsLosses> {

    private var mContent: WinsLosses? = null
    private val mNumberFormat = NumberFormat.getIntegerInstance()
    private lateinit var mPaint: Paint
    private lateinit var mPlayerOrOpponent: PlayerOrOpponent
    private lateinit var mRect: Rect

    private val mPlayerName: TextView by bindView(R.id.playerName)
    private val mWinCount: TextView by bindView(R.id.winCount)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        parseAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        parseAttributes(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleableRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        parseAttributes(attrs)
    }

    private fun calculateRects() {
        val height = height
        val width = width
        val content = mContent

        if (height == 0 || width == 0 || content == null) {
            mRect.setEmpty()
            return
        }

        val barThickness = width / 4
        val start = (width - barThickness) / 4
        val end = start + barThickness

        if (content.losses == 0 && content.wins == 0) {
            mRect.setEmpty()
        } else if (mPlayerOrOpponent == PlayerOrOpponent.PLAYER) {

        } else if (mPlayerOrOpponent == PlayerOrOpponent.OPPONENT) {

        }
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
        if (!mRect.isEmpty) {
            canvas.drawRect(mRect, mPaint)
        }

        super.onDraw(canvas)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            // TODO
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

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.WinsLossesColumnView)
        val playerOrOpponent = ta.getInt(R.styleable.WinsLossesColumnView_playerOrOpponent,
                -1)
        ta.recycle()

        if (playerOrOpponent < 0 || playerOrOpponent >= PlayerOrOpponent.values().size) {
            throw RuntimeException("playerOrOpponent attribute is an illegal value: $playerOrOpponent")
        }

        mPlayerOrOpponent = PlayerOrOpponent.values()[playerOrOpponent]

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = ContextCompat.getColor(context, mPlayerOrOpponent.mColor)
        mPaint.isDither = true
        mPaint.style = Paint.Style.FILL

        mRect = Rect()
    }

    override fun setContent(content: WinsLosses) {

    }

    private enum class PlayerOrOpponent(
            @ColorRes internal val mColor: Int
    ) {
        PLAYER(R.color.win),
        OPPONENT(R.color.lose)
    }

}
