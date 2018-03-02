package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.extensions.getAttrColor

class DashedLineView : View {

    private val paint = Paint()
    private val rect = Rect()


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun calculateRect() {
        val height = this.height
        val width = this.width

        if (height == 0 || width == 0) {
            rect.setEmpty()
            return
        }

        rect.set(0, 0, width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (rect.isEmpty) {
            return
        }

        canvas.drawRect(rect, paint)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        paint.color = context.getAttrColor(android.R.attr.textColorSecondary)
        paint.isAntiAlias = true
        paint.pathEffect = DashPathEffect(floatArrayOf(resources.getDimension(R.dimen.root_padding),
                resources.getDimension(R.dimen.root_padding)), 0f)
        paint.style = Paint.Style.STROKE
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculateRect()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateRect()
    }

}
