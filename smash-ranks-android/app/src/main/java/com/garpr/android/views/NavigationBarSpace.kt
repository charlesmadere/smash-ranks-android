package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.DeviceUtils
import javax.inject.Inject

class NavigationBarSpace : View {

    @Inject
    protected lateinit var deviceUtils: DeviceUtils


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
                @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

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
