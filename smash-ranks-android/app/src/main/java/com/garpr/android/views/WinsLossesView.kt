package com.garpr.android.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.WinsLosses

import java.text.NumberFormat

class WinsLossesView : AppCompatTextView, BaseAdapterView<WinsLosses> {

    private val mNumberFormat: NumberFormat = NumberFormat.getIntegerInstance()
    lateinit private var mLossesPaint: Paint
    lateinit private var mWinsPaint: Paint
    lateinit private var mLossesRect: Rect
    lateinit private var mWinsRect: Rect
    private var mContent: WinsLosses? = null


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr)

    init {
        initialize()
    }

    private fun calculateRects() {
        val height = height
        val width = width
        val content = mContent

        if (height == 0 || width == 0 || content == null) {
            mLossesRect.setEmpty()
            mWinsRect.setEmpty()
            return
        }

        val barThickness = height / 4
        val top = (height - barThickness) / 2
        val bottom = top + barThickness

        if (content.mLosses == 0 && content.mWins == 0) {
            mLossesRect.setEmpty()
            mWinsRect.setEmpty()
        } else if (content.mLosses == 0) {
            mLossesRect.setEmpty()
            mWinsRect.set(0, top, width, bottom)
        } else if (content.mWins == 0) {
            mLossesRect.set(0, top, width, bottom)
            mWinsRect.setEmpty()
        } else {
            val winsPercent = content.mWins.toFloat() / (content.mLosses + content.mWins).toFloat()
            val start = Math.round(winsPercent * width.toFloat())
            mLossesRect.set(start, top, width, bottom)
            mWinsRect.set(0, top, start, bottom)
        }
    }

    private fun initialize() {
        mLossesPaint = Paint()
        mLossesPaint.isAntiAlias = true
        mLossesPaint.color = ContextCompat.getColor(context, R.color.lose_background)
        mLossesPaint.isDither = true
        mLossesPaint.style = Paint.Style.FILL

        mWinsPaint = Paint()
        mWinsPaint.isAntiAlias = true
        mWinsPaint.color = ContextCompat.getColor(context, R.color.win_background)
        mWinsPaint.isDither = true
        mWinsPaint.style = Paint.Style.FILL

        mLossesRect = Rect()
        mWinsRect = Rect()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        calculateRects()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (!mLossesRect.isEmpty) {
            canvas.drawRect(mLossesRect, mLossesPaint)
        }

        if (!mWinsRect.isEmpty) {
            canvas.drawRect(mWinsRect, mWinsPaint)
        }

        super.onDraw(canvas)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            setContent(WinsLosses(8, 5))
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int,
            bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculateRects()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateRects()
        invalidate()
    }

    override fun setContent(content: WinsLosses) {
        mContent = content

        text = resources.getString(R.string.x_em_dash_y,
                mNumberFormat.format(content.mWins.toLong()),
                mNumberFormat.format(content.mLosses.toLong()))

        calculateRects()
        invalidate()
    }

}
