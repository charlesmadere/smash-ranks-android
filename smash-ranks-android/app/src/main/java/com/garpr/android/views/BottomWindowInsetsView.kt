package com.garpr.android.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BottomWindowInsetsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : View(context, attrs), OnApplyWindowInsetsListener {

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this, this)
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat? {
        layoutParams = layoutParams.apply {
            height = insets.systemWindowInsetBottom
        }

        return insets.replaceSystemWindowInsets(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                0)
    }

    override fun onDraw(canvas: Canvas) {
        // intentionally empty, this view shouldn't draw anything
    }

}
