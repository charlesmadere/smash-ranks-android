package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.setTintedImageDrawable

class ColorAccentTintedImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private fun applyTint() {
        setTintedImageDrawable(context.getAttrColor(R.attr.colorAccent))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        applyTint()
    }

}
