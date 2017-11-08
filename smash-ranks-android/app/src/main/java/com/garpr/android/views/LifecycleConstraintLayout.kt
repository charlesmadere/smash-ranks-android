package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import com.garpr.android.misc.Heartbeat

open class LifecycleConstraintLayout : ConstraintLayout, Heartbeat {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

}
