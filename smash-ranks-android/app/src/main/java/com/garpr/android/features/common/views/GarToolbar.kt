package com.garpr.android.features.common.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnTouchListener
import android.view.Window
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import com.garpr.android.R
import com.garpr.android.extensions.colorCompat
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.getLong
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.requireActivity
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.MiscUtils
import kotlinx.android.synthetic.main.gar_toolbar.view.*

open class GarToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    @ColorInt
    private val colorPrimary = context.getAttrColor(R.attr.colorPrimary)

    @ColorInt
    private val colorPrimaryDark = context.getAttrColor(R.attr.colorPrimaryDark)

    @ColorInt
    private val textColorPrimary = context.getAttrColor(android.R.attr.textColorPrimary)

    @ColorInt
    private val textColorSecondary = context.getAttrColor(android.R.attr.textColorSecondary)

    val hasSubtitleText: Boolean
        get() = !subtitleText.isNullOrBlank()

    val hasTitleText: Boolean
        get() = !titleText.isNullOrBlank()

    protected var showTitleContainer: Boolean
        get() = titleContainer.visibility == View.VISIBLE
        set(value) {
            titleContainer.visibility = if (value) View.VISIBLE else View.GONE
        }

    var showUpNavigation: Boolean
        get() = upNavigationButton.visibility == View.VISIBLE
        set(value) {
            upNavigationButton.visibility = if (value) View.VISIBLE else View.GONE
        }

    var subtitleText: CharSequence? = null
        get() = subtitleView.text
        set(value) {
            field = value
            subtitleView.text = value
            refreshTitleContainerVisibility()
        }

    var titleText: CharSequence? = null
        get() = titleView.text
        set(value) {
            field = value
            titleView.text = value
            refreshTitleContainerVisibility()
        }

    var subtitleTextColor: Int = textColorSecondary
        set(value) {
            field = value
            subtitleView.setTextColor(value)
        }

    var titleTextColor: Int = textColorPrimary
        set(value) {
            field = value
            titleView.setTextColor(value)
        }

    private val onUpNavigationClick = OnClickListener {
        upNavigate()
    }

    private val onTouchListener = OnTouchListener { v, event ->
        // disable clicking through the background of the view
        true
    }

    companion object {
        private const val LIGHTNESS_LIMIT = 0.4f
        private const val STATUS_BAR_DARKEN_FACTOR = 0.8f
        private const val TOO_LIGHT_DARKEN_FACTOR = 0.75f
    }

    init {
        @Suppress("LeakingThis")
        layoutInflater.inflate(R.layout.gar_toolbar, this)

        var ta = context.obtainStyledAttributes(attrs, R.styleable.GarToolbar)
        showUpNavigation = ta.getBoolean(R.styleable.GarToolbar_showUpNavigation, showUpNavigation)
        subtitleTextColor = ta.getColor(R.styleable.GarToolbar_descriptionTextColor, subtitleTextColor)
        titleTextColor = ta.getColor(R.styleable.GarToolbar_titleTextColor, titleTextColor)
        ta.recycle()

        @SuppressLint("CustomViewStyleable")
        ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        subtitleText = ta.getText(R.styleable.View_descriptionText)
        titleText = ta.getText(R.styleable.View_titleText)
        ta.recycle()

        @Suppress("LeakingThis")
        setOnTouchListener(onTouchListener)

        upNavigationButton.setOnClickListener(onUpNavigationClick)
    }

    fun animateToPaletteColors(window: Window, palette: Palette?) {
        val toolbarBackgroundFallback = colorPrimary
        val statusBarBackgroundFallback = colorPrimaryDark

        val swatch = palette?.darkVibrantSwatch ?: palette?.darkMutedSwatch

        @ColorInt val toolbarBackground: Int
        @ColorInt val systemWindowBackground: Int

        if (swatch == null) {
            toolbarBackground = toolbarBackgroundFallback
            systemWindowBackground = statusBarBackgroundFallback
        } else {
            toolbarBackground = MiscUtils.brightenOrDarkenColorIfLightnessIs(swatch.rgb,
                    TOO_LIGHT_DARKEN_FACTOR, LIGHTNESS_LIMIT)
            systemWindowBackground = MiscUtils.brightenOrDarkenColor(toolbarBackground,
                    STATUS_BAR_DARKEN_FACTOR)
        }

        val toolbarAnimator = ValueAnimator.ofArgb(
                background?.colorCompat ?: toolbarBackgroundFallback, toolbarBackground)
        toolbarAnimator.addUpdateListener(backgroundAnimatorUpdateListener)

        val statusBarAnimator = ValueAnimator.ofArgb(window.statusBarColor,
                systemWindowBackground)
        statusBarAnimator.addUpdateListener {
            window.statusBarColor = it.animatedValue as Int
        }

        val navigationBarAnimator = ValueAnimator.ofArgb(window.navigationBarColor,
                systemWindowBackground)
        navigationBarAnimator.addUpdateListener {
            window.navigationBarColor = it.animatedValue as Int
        }

        val animatorSet = AnimatorSet()
        animatorSet.duration = titleColorAnimationDuration
        animatorSet.interpolator = AnimationUtils.DECELERATE_INTERPOLATOR
        animatorSet.playTogether(toolbarAnimator, statusBarAnimator, navigationBarAnimator)
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

        val titleAnimation = ValueAnimator.ofArgb(titleTextColor, textColorPrimary)
        titleAnimation.addListener(clearInTitleAnimation)
        titleAnimation.addUpdateListener(titleAnimatorUpdateListener)
        titleAnimation.duration = titleAlphaAnimationDuration
        titleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

        val subtitleAnimation = ValueAnimator.ofArgb(subtitleTextColor, textColorSecondary)
        subtitleAnimation.addListener(clearInSubtitleAnimation)
        subtitleAnimation.addUpdateListener(subtitleAnimatorUpdateListener)
        subtitleAnimation.duration = titleAnimation.duration
        subtitleAnimation.interpolator = titleAnimation.interpolator

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

        val titleAnimation = ValueAnimator.ofArgb(titleTextColor, Color.TRANSPARENT)
        titleAnimation.addListener(clearOutTitleAnimation)
        titleAnimation.addUpdateListener(titleAnimatorUpdateListener)
        titleAnimation.duration = titleAlphaAnimationDuration
        titleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

        val subtitleAnimation = ValueAnimator.ofArgb(subtitleTextColor, Color.TRANSPARENT)
        subtitleAnimation.addListener(clearOutSubtitleAnimation)
        subtitleAnimation.addUpdateListener(subtitleAnimatorUpdateListener)
        subtitleAnimation.duration = titleAnimation.duration
        subtitleAnimation.interpolator = titleAnimation.interpolator

        outTitleAnimation = titleAnimation
        outSubtitleAnimation = subtitleAnimation

        titleAnimation.start()
        subtitleAnimation.start()
    }

    private fun refreshTitleContainerVisibility() {
        titleView.visibility = if (hasTitleText) View.VISIBLE else View.GONE
        subtitleView.visibility = if (hasSubtitleText) View.VISIBLE else View.GONE
        showTitleContainer = titleView.visibility == View.VISIBLE || subtitleView.visibility == View.VISIBLE
    }

    protected open fun upNavigate() {
        when (val activity = requireActivity()) {
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


    ///////////////////////////////
    // BEGIN ANIMATION VARIABLES //
    ///////////////////////////////

    private val titleAlphaAnimationDuration: Long by lazy {
        resources.getLong(R.integer.toolbar_title_animation_duration)
    }

    private val titleColorAnimationDuration: Long by lazy {
        resources.getLong(R.integer.toolbar_color_animation_duration)
    }

    private var inTitleAnimation: ValueAnimator? = null
    private var inSubtitleAnimation: ValueAnimator? = null
    private var outTitleAnimation: ValueAnimator? = null
    private var outSubtitleAnimation: ValueAnimator? = null

    private val clearInTitleAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                inTitleAnimation = null
            }
        }
    }

    private val clearInSubtitleAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                inSubtitleAnimation = null
            }
        }
    }

    private val clearOutTitleAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                outTitleAnimation = null
            }
        }
    }

    private val clearOutSubtitleAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                outSubtitleAnimation = null
            }
        }
    }

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

    /////////////////////////////
    // END ANIMATION VARIABLES //
    /////////////////////////////

}
