package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import com.garpr.android.misc.Heartbeat

open class LifecycleCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : CardView(context, attrs), Heartbeat {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
