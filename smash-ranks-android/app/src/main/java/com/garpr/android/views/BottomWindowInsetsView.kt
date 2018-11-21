package com.garpr.android.views

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.garpr.android.R
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

        return insets.consumeSystemWindowInsets()
    }

    override fun onDraw(canvas: Canvas) {
        // intentionally empty
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setOnApplyWindowInsetsListener(this, this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val navigationBarHeight = if (isInEditMode) {
            resources.getDimensionPixelSize(R.dimen.navigation_bar_height)
        } else if (deviceUtils.supportsTranslucentNavigationBar) {
            deviceUtils.navigationBarHeight
        } else {
            0
        }

        setMeasuredDimension(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(navigationBarHeight, MeasureSpec.EXACTLY))
    }

}
