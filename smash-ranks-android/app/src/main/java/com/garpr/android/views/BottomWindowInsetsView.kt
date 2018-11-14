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

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat? {
        layoutParams = layoutParams.apply {
            height = insets.systemWindowInsetBottom
        }

        return insets.consumeSystemWindowInsets()
    }

    override fun onDraw(canvas: Canvas) {
        // intentionally empty
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ViewCompat.setOnApplyWindowInsetsListener(this, this)
    }

}
