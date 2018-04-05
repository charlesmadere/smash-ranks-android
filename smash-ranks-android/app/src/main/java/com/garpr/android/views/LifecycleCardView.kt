package com.garpr.android.views

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import com.garpr.android.misc.Heartbeat

open class LifecycleCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : CardView(context, attrs), Heartbeat {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
