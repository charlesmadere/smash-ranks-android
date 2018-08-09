package com.garpr.android.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.DeviceUtils
import javax.inject.Inject

class NavigationBarSpace @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : View(context, attrs) {

    @Inject
    protected lateinit var deviceUtils: DeviceUtils


    override fun onDraw(canvas: Canvas) {
        // intentionally empty
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
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
