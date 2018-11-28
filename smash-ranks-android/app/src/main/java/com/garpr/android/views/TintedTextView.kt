package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.garpr.android.R
import com.garpr.android.extensions.compoundDrawablesRelativeCompat
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.setTintedDrawableColor
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.Refreshable

class TintedTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleTextView(context, attrs), ColorListener, Refreshable {

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
        setTintedDrawableColor(palette?.mutedSwatch?.rgb ?: drawableTintColor)
    }

    override fun refresh() {
        setTintedDrawableColor(drawableTintColor)
    }

    fun setEndCompoundDrawableRelativeWithIntrinsicBounds(@DrawableRes drawableResId: Int) {
        val drawables = compoundDrawablesRelativeCompat
        drawables[2] = ContextCompat.getDrawable(context, drawableResId)
        compoundDrawablesRelativeCompat = drawables

        refresh()
    }

}
