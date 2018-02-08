package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.setTintedImageDrawable

class TintedImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : LifecycleImageView(context, attrs, defStyleAttr) {

    @ColorInt
    private var tintColor: Int = 0


    init {
        parseAttributes(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setTintedImageDrawable(tintColor)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TintedImageView)
        tintColor = ta.getColor(R.styleable.TintedImageView_tintColor,
                context.getAttrColor(android.R.attr.textColorSecondary))
        ta.recycle()
    }

}
