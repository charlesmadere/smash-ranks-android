package com.garpr.android.misc

import android.animation.ArgbEvaluator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator

object AnimationUtils {

    // evaluators
    val ARGB_EVALUATOR by lazy { ArgbEvaluator() }

    // interpolators
    val ACCELERATE_INTERPOLATOR by lazy { AccelerateInterpolator() }
    val ACCELERATE_DECELERATE_INTERPOLATOR by lazy { AccelerateDecelerateInterpolator() }

}
