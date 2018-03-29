package com.garpr.android.views

import android.content.Context
import android.support.annotation.ColorInt
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.setTintedDrawableColor
import com.garpr.android.misc.Refreshable

class TintedButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleButton(context, attrs), Refreshable {

    @ColorInt
    private var drawableTintColor: Int = 0


    init {
        parseAttributes(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        refresh()
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        drawableTintColor = ta.getColor(R.styleable.View_drawableTintColor,
                context.getAttrColor(android.R.attr.textColorSecondary))
        ta.recycle()
    }

    override fun refresh() {
        setTintedDrawableColor(drawableTintColor)
    }

}
