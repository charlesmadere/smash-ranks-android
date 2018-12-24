package com.garpr.android.views.toolbars

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import com.garpr.android.R
import com.garpr.android.activities.BaseActivity
import com.garpr.android.extensions.colorCompat
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.getLong
import com.garpr.android.extensions.requireActivity
import com.garpr.android.extensions.statusBarColorCompat
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.Refreshable
import kotlinx.android.synthetic.main.gar_toolbar.view.*

private const val LIGHTNESS_LIMIT = 0.4f
private const val STATUS_BAR_DARKEN_FACTOR = 0.8f
private const val TOO_LIGHT_DARKEN_FACTOR = 0.75f

open class GarToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), Heartbeat, Refreshable {

    // begin animation variables
    private val animationDuration: Long by lazy {
        resources.getLong(android.R.integer.config_shortAnimTime)
    }

    private val colorPrimary: Int by lazy {
        context.getAttrColor(R.attr.colorPrimary)
    }

    private val colorPrimaryDark: Int by lazy {
        context.getAttrColor(R.attr.colorPrimaryDark)
    }

    private val textColorPrimary: Int by lazy {
        context.getAttrColor(android.R.attr.textColorPrimary)
    }

    private val textColorSecondary: Int by lazy {
        context.getAttrColor(android.R.attr.textColorSecondary)
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
            titleTextColor = it.animatedValue as Int
        }
    }

    private val subtitleAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            subtitleTextColor = it.animatedValue as Int
        }
    }
    // end animation variables

    protected var showTitleContainer: Boolean
        get() = titleContainer.visibility == View.VISIBLE
        set(value) {
            titleContainer.visibility = if (value) View.VISIBLE else View.GONE
        }

    var showUpNavigation: Boolean = false
        set(value) {
            field = value
            upNavigationButton.visibility = if (value) View.VISIBLE else View.GONE
        }

    var subtitleText: CharSequence? = null
        set(value) {
            field = value
            subtitleView.text = value
            refreshTitleContainerVisibility()
        }

    var subtitleTextColor: Int = textColorSecondary
        set(value) {
            field = value
            subtitleView.setTextColor(value)
        }

    var titleText: CharSequence? = null
        set(value) {
            field = value
            titleView.text = value
            refreshTitleContainerVisibility()
        }

    var titleTextColor: Int = textColorPrimary
        set(value) {
            field = value
            titleView.setTextColor(value)
        }

    init {
        @Suppress("LeakingThis")
        LayoutInflater.from(context).inflate(R.layout.gar_toolbar, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.GarToolbar)
        showUpNavigation = ta.getBoolean(R.styleable.GarToolbar_showUpNavigation, showUpNavigation)
        subtitleText = ta.getText(R.styleable.GarToolbar_toolbarSubtitleText)
        subtitleTextColor = ta.getColor(R.styleable.GarToolbar_toolbarSubtitleTextColor, subtitleTextColor)
        titleText = ta.getText(R.styleable.GarToolbar_toolbarTitleText)
        titleTextColor = ta.getColor(R.styleable.GarToolbar_toolbarTitleTextColor, titleTextColor)
        ta.recycle()
    }

    fun animateToPaletteColors(window: Window, palette: Palette?) {
        val toolbarBackgroundFallback = colorPrimary
        val statusBarBackgroundFallback = colorPrimaryDark

        val swatch = palette?.darkVibrantSwatch ?: palette?.darkMutedSwatch

        @ColorInt val toolbarBackground: Int
        @ColorInt val statusBarBackground: Int

        if (swatch == null) {
            toolbarBackground = toolbarBackgroundFallback
            statusBarBackground = statusBarBackgroundFallback
        } else {
            toolbarBackground = MiscUtils.brightenOrDarkenColorIfLightnessIs(swatch.rgb,
                    TOO_LIGHT_DARKEN_FACTOR, LIGHTNESS_LIMIT)
            statusBarBackground = MiscUtils.brightenOrDarkenColor(toolbarBackground,
                    STATUS_BAR_DARKEN_FACTOR)
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

    fun fadeInTitleAndSubtitle() {
        if (inTitleAnimation != null || inSubtitleAnimation != null) {
            return
        }

        outTitleAnimation?.cancel()
        outTitleAnimation = null
        outSubtitleAnimation?.cancel()
        outSubtitleAnimation = null

        val titleAnimation = AnimationUtils.createArgbValueAnimator(titleTextColor,
                textColorPrimary)
        titleAnimation.addUpdateListener(titleAnimatorUpdateListener)
        titleAnimation.duration = animationDuration
        titleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

        titleAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                inTitleAnimation = null
            }
        })

        val subtitleAnimation = AnimationUtils.createArgbValueAnimator(subtitleTextColor,
                textColorSecondary)
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

        val titleAnimation = AnimationUtils.createArgbValueAnimator(titleTextColor,
                Color.TRANSPARENT)
        titleAnimation.addUpdateListener(titleAnimatorUpdateListener)
        titleAnimation.duration = animationDuration
        titleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

        titleAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                outTitleAnimation = null
            }
        })

        val subtitleAnimation = AnimationUtils.createArgbValueAnimator(subtitleTextColor,
                Color.TRANSPARENT)
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

    val hasSubtitleText: Boolean
        get() = subtitleText?.isNotBlank() == true

    val hasTitleText: Boolean
        get() = titleText?.isNotBlank() == true

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        refresh()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        upNavigationButton.setOnClickListener {
            upNavigate()
        }
    }

    override fun refresh() {
        // intentionally empty, children can override
    }

    private fun refreshTitleContainerVisibility() {
        titleView.visibility = if (hasTitleText) View.VISIBLE else View.GONE
        subtitleView.visibility = if (hasSubtitleText) View.VISIBLE else View.GONE
        showTitleContainer = titleView.visibility == View.VISIBLE || subtitleView.visibility == View.VISIBLE
    }

    protected open fun upNavigate() {
        val activity = requireActivity()

        when (activity) {
            is BaseActivity -> {
                activity.navigateUp()
            }

            is AppCompatActivity -> {
                activity.supportFinishAfterTransition()
            }

            else -> {
                activity.finish()
            }
        }
    }

}
