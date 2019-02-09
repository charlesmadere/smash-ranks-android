package com.garpr.android.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.DeviceUtils
import javax.inject.Inject

class BottomWindowInsetsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : View(context, attrs), OnApplyWindowInsetsListener {

    @Inject
    protected lateinit var deviceUtils: DeviceUtils


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
        // intentionally empty
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
        }

        ViewCompat.setOnApplyWindowInsetsListener(this, this)
    }

}
