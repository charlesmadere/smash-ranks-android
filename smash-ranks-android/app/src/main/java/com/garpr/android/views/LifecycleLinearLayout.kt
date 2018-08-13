package com.garpr.android.views

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.widget.LinearLayout
import com.garpr.android.misc.Heartbeat

open class LifecycleLinearLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), Heartbeat {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
