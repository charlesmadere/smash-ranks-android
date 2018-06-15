package com.garpr.android.views

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v7.graphics.Palette
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.setTintedImageColor
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.Refreshable

class TintedImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleImageView(context, attrs), ColorListener, Refreshable {

    @ColorInt
    private var drawableTintColor: Int = 0


    init {
        parseAttributes(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        refresh()
    }

    override fun onPaletteBuilt(palette: Palette?) {
        setTintedImageColor(palette?.mutedSwatch?.rgb ?: drawableTintColor)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        drawableTintColor = ta.getColor(R.styleable.View_drawableTintColor,
                context.getAttrColor(android.R.attr.textColorSecondary))
        ta.recycle()
    }

    override fun refresh() {
        setTintedImageColor(drawableTintColor)
    }

}
