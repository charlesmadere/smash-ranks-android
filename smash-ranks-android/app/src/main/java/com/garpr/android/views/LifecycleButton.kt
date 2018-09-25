package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import com.garpr.android.misc.Heartbeat

open class LifecycleButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : AppCompatButton(context, attrs), Heartbeat {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
