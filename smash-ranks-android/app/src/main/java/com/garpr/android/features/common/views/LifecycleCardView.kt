package com.garpr.android.features.common.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.ViewCompat
import com.garpr.android.misc.Heartbeat
import com.google.android.material.card.MaterialCardView

open class LifecycleCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : MaterialCardView(context, attrs), Heartbeat {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
