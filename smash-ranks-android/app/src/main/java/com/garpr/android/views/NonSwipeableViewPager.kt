package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeableViewPager @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun performClick(): Boolean = false

}
