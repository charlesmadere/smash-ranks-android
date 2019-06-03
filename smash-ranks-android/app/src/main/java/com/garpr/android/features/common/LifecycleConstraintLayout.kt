package com.garpr.android.features.common

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.garpr.android.misc.Heartbeat

open class LifecycleConstraintLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), Heartbeat {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
