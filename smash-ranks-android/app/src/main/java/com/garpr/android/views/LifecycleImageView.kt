package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.garpr.android.misc.Heartbeat

open class LifecycleImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), Heartbeat {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
