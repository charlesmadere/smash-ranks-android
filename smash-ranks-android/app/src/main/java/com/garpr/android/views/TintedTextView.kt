package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.setTintedImageColor

class TintedTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : LifecycleTextView(context, attrs, defStyleAttr) {

    @ColorInt
    private var drawableTintColor: Int = 0


    init {
        parseAttributes(attrs)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setTintedImageColor(drawableTintColor)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setTintedImageColor(drawableTintColor)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        drawableTintColor = ta.getColor(R.styleable.View_drawableTintColor,
                context.getAttrColor(android.R.attr.textColorSecondary))
        ta.recycle()
    }

}