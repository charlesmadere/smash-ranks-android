package com.garpr.android.misc

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

object AnimationUtils {

    // interpolators
    val ACCELERATE_DECELERATE_INTERPOLATOR: Interpolator by lazy { AccelerateDecelerateInterpolator() }
    val DECELERATE_INTERPOLATOR: Interpolator by lazy { DecelerateInterpolator() }

}
