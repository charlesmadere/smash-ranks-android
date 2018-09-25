package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import com.garpr.android.misc.Heartbeat

open class LifecycleTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs), Heartbeat {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
