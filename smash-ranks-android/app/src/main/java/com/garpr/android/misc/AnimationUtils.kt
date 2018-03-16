package com.garpr.android.misc

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator

object AnimationUtils {

    // interpolators
    val ACCELERATE_INTERPOLATOR by lazy { AccelerateInterpolator() }
    val ACCELERATE_DECELERATE_INTERPOLATOR by lazy { AccelerateDecelerateInterpolator() }

}
