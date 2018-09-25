package com.garpr.android.misc

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Build
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi

object AnimationUtils {

    private interface Impl {
        fun createArgbValueAnimator(@ColorInt startColor: Int, @ColorInt endColor: Int): ValueAnimator
    }

    private class BaseImpl : Impl {
        override fun createArgbValueAnimator(startColor: Int, endColor: Int): ValueAnimator {
            return ValueAnimator.ofObject(ARGB_EVALUATOR, startColor, endColor)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private class Api21Impl : Impl {
        override fun createArgbValueAnimator(startColor: Int, endColor: Int): ValueAnimator {
            return ValueAnimator.ofArgb(startColor, endColor)
        }
    }

    // evaluators
    private val ARGB_EVALUATOR by lazy { ArgbEvaluator() }

    // interpolators
    val ACCELERATE_DECELERATE_INTERPOLATOR by lazy { AccelerateDecelerateInterpolator() }

    private val IMPL: Impl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Api21Impl()
    } else {
        BaseImpl()
    }

    fun createArgbValueAnimator(@ColorInt startColor: Int, @ColorInt endColor: Int): ValueAnimator {
        return IMPL.createArgbValueAnimator(startColor, endColor)
    }

}
