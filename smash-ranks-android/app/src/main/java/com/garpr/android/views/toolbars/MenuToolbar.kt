package com.garpr.android.views.toolbars

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import com.garpr.android.R
import com.garpr.android.extensions.colorCompat
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.getLong
import com.garpr.android.extensions.statusBarColorCompat
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.Refreshable

abstract class MenuToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : Toolbar(context, attrs), Heartbeat, Refreshable {

    private var startWithTransparentTextColors: Boolean = false
    private val sparseMenuItemsArray = SparseBooleanArray()


    // begin animation Variables
    private val animationDuration: Long by lazy {
        resources.getLong(android.R.integer.config_shortAnimTime)
    }

    private val textColorPrimary: Int by lazy {
        context.getAttrColor(android.R.attr.textColorPrimary)
    }

    private val textColorSecondary: Int by lazy {
        context.getAttrColor(android.R.attr.textColorSecondary)
    }

    private val toolbarReflectionHelper: ToolbarReflectionHelper by lazy {
        ToolbarReflectionHelper(this)
    }

    private var inTitleAnimation: ValueAnimator? = null
    private var inSubtitleAnimation: ValueAnimator? = null
    private var outTitleAnimation: ValueAnimator? = null
    private var outSubtitleAnimation: ValueAnimator? = null

    private val backgroundAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            setBackgroundColor(it.animatedValue as Int)
        }
    }

    private val titleAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            setTitleTextColor(it.animatedValue as Int)
        }
    }

    private val subtitleAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            setSubtitleTextColor(it.animatedValue as Int)
        }
    }
    // end animation Variables

    companion object {
        private const val LIGHTNESS_LIMIT = 0.4f
        private const val STATUS_BAR_DARKEN_FACTOR = 0.8f
        private const val TOO_LIGHT_DARKEN_FACTOR = 0.75f
    }

    init {
        parseAttributes(attrs)
    }

    fun animateBackgroundToPalette(window: Window, palette: Palette?) {
        val toolbarBackgroundFallback = context.getAttrColor(R.attr.colorPrimary)
        val statusBarBackgroundFallback = context.getAttrColor(R.attr.colorPrimaryDark)

        val swatch = palette?.darkVibrantSwatch ?: palette?.darkMutedSwatch

        @ColorInt val toolbarBackground: Int
        @ColorInt val statusBarBackground: Int

        if (swatch == null) {
            toolbarBackground = toolbarBackgroundFallback
            statusBarBackground = statusBarBackgroundFallback
        } else {
            toolbarBackground = MiscUtils.brightenOrDarkenColorIfLightnessIs(swatch.rgb, TOO_LIGHT_DARKEN_FACTOR,
                LIGHTNESS_LIMIT)
            statusBarBackground = MiscUtils.brightenOrDarkenColor(toolbarBackground, STATUS_BAR_DARKEN_FACTOR)
        }

        val toolbarAnimator = AnimationUtils.createArgbValueAnimator(
                background?.colorCompat ?: toolbarBackgroundFallback, toolbarBackground)
        toolbarAnimator.addUpdateListener(backgroundAnimatorUpdateListener)

        val statusBarAnimator = AnimationUtils.createArgbValueAnimator(
                window.statusBarColorCompat ?: statusBarBackgroundFallback, statusBarBackground)
        statusBarAnimator.addUpdateListener {
            window.statusBarColorCompat = it.animatedValue as Int
        }

        val animatorSet = AnimatorSet()
        animatorSet.duration = resources.getLong(R.integer.color_animation_duration)
        animatorSet.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR
        animatorSet.playTogether(toolbarAnimator, statusBarAnimator)
        animatorSet.start()
    }

    private fun createSparseMenuItemsArray() {
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            sparseMenuItemsArray.put(menuItem.itemId, menuItem.isVisible)
        }
    }

    fun fadeInTitleAndSubtitle() {
        if (inTitleAnimation != null || inSubtitleAnimation != null) {
            return
        }

        outTitleAnimation?.cancel()
        outTitleAnimation = null
        outSubtitleAnimation?.cancel()
        outSubtitleAnimation = null

        val titleAnimation = AnimationUtils.createArgbValueAnimator(
                toolbarReflectionHelper.titleTextColor, textColorPrimary)
        titleAnimation.addUpdateListener(titleAnimatorUpdateListener)
        titleAnimation.duration = animationDuration
        titleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

        titleAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                inTitleAnimation = null
            }
        })

        val subtitleAnimation = AnimationUtils.createArgbValueAnimator(
                toolbarReflectionHelper.subtitleTextColor, textColorSecondary)
        subtitleAnimation.addUpdateListener(subtitleAnimatorUpdateListener)
        subtitleAnimation.duration = titleAnimation.duration
        subtitleAnimation.interpolator = titleAnimation.interpolator

        subtitleAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                inSubtitleAnimation = null
            }
        })

        inTitleAnimation = titleAnimation
        inSubtitleAnimation = subtitleAnimation

        titleAnimation.start()
        subtitleAnimation.start()
    }

    fun fadeOutTitleAndSubtitle() {
        if (outTitleAnimation != null || outSubtitleAnimation != null) {
            return
        }

        inTitleAnimation?.cancel()
        inTitleAnimation = null
        inSubtitleAnimation?.cancel()
        inSubtitleAnimation = null

        val titleAnimation = AnimationUtils.createArgbValueAnimator(
                toolbarReflectionHelper.titleTextColor, Color.TRANSPARENT)
        titleAnimation.addUpdateListener(titleAnimatorUpdateListener)
        titleAnimation.duration = animationDuration
        titleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

        titleAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                outTitleAnimation = null
            }
        })

        val subtitleAnimation = AnimationUtils.createArgbValueAnimator(
                toolbarReflectionHelper.subtitleTextColor, Color.TRANSPARENT)
        subtitleAnimation.addUpdateListener(subtitleAnimatorUpdateListener)
        subtitleAnimation.duration = titleAnimation.duration
        subtitleAnimation.interpolator = titleAnimation.interpolator

        subtitleAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                outSubtitleAnimation = null
            }
        })

        outTitleAnimation = titleAnimation
        outSubtitleAnimation = subtitleAnimation

        titleAnimation.start()
        subtitleAnimation.start()
    }

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    protected var isMenuCreated: Boolean = false
        private set

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        refresh()
    }

    open fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        createSparseMenuItemsArray()
        isMenuCreated = true
    }

    open fun onOptionsItemSelected(item: MenuItem): Boolean {
        // intentionally empty, children can override
        return false
    }

    protected open fun onRefreshMenu() {
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            menuItem.isVisible = sparseMenuItemsArray.get(menuItem.itemId)
        }
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MenuToolbar)

        if (ta.getBoolean(R.styleable.MenuToolbar_startWithTransparentTextColors, false)) {
            startWithTransparentTextColors = true
            setTitleTextColor(Color.TRANSPARENT)
            setSubtitleTextColor(Color.TRANSPARENT)
        }

        ta.recycle()
    }

    protected fun postRefresh() {
        if (isAlive) {
            post(refreshRunnable)
        }
    }

    final override fun refresh() {
        if (isMenuCreated) {
            onRefreshMenu()
        }
    }

    private val refreshRunnable = Runnable {
        if (isAlive) {
            refresh()
        }
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)

        if (startWithTransparentTextColors) {
            setTitleTextColor(toolbarReflectionHelper.titleTextColor)
        }
    }

    override fun setSubtitle(subtitle: CharSequence?) {
        super.setSubtitle(subtitle)

        if (startWithTransparentTextColors) {
            setSubtitleTextColor(toolbarReflectionHelper.subtitleTextColor)
        }
    }

}
