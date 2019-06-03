package com.garpr.android.features.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
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
    private val drawableTintColor: Int


    init {
        @SuppressLint("CustomViewStyleable")
        val ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        drawableTintColor = ta.getColor(R.styleable.View_drawableTintColor,
                context.getAttrColor(android.R.attr.textColorSecondary))
        ta.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        refresh()
    }

    override fun onPaletteBuilt(palette: Palette?) {
        setTintedImageColor(palette?.mutedSwatch?.rgb ?: drawableTintColor)
    }

    override fun refresh() {
        setTintedImageColor(drawableTintColor)
    }

}
