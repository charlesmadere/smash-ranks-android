package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import com.garpr.android.misc.Heartbeat

open class LifecycleImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs), Heartbeat {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
