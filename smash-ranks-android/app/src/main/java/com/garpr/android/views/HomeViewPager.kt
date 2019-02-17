package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.garpr.android.misc.HomeTab

class HomeViewPager @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    var currentTab: HomeTab
        get() = HomeTab.values()[currentItem]
        set(value) { setCurrentItem(value.ordinal, false) }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun performClick(): Boolean = false

}
