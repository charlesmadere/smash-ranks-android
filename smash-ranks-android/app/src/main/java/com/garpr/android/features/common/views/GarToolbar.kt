package com.garpr.android.features.common.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.view.View.OnTouchListener
import android.view.ViewPropertyAnimator
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.colorCompat
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.getLong
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.requireActivity
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.ColorUtils
import com.garpr.android.misc.Heartbeat
import kotlinx.android.synthetic.main.gar_toolbar.view.*

open class GarToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), ColorListener, Heartbeat {

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

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    protected var showTitleContainer: Boolean
        get() = titleContainer.visibility == VISIBLE
        set(value) {
            titleContainer.visibility = if (value) VISIBLE else GONE
        }

    var showUpNavigation: Boolean
        get() = upNavigationButton.visibility == VISIBLE
        set(value) {
            upNavigationButton.visibility = if (value) VISIBLE else GONE
        }

    var subtitleText: CharSequence? = null
        set(value) {
            field = value
            subtitleView.text = value
            refreshTitleContainerVisibility()
        }

    var titleText: CharSequence? = null
        set(value) {
            field = value
            titleView.text = value
            refreshTitleContainerVisibility()
        }

    private val onUpNavigationClick = OnClickListener {
        upNavigate()
    }

    private val onTouchListener = OnTouchListener { v, event ->
        // disable clicking through the background of the view
        true
    }

    init {
        @Suppress("LeakingThis")
        layoutInflater.inflate(R.layout.gar_toolbar, this)

        var ta = context.obtainStyledAttributes(attrs, R.styleable.GarToolbar)

        if (ta.getBoolean(R.styleable.GarToolbar_startWithTransparentTitle, false)) {
            titleView.alpha = 0f
            subtitleView.alpha = 0f
        }

        showUpNavigation = ta.getBoolean(R.styleable.GarToolbar_showUpNavigation, showUpNavigation)
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

    fun fadeInTitleAndSubtitle() {
        if (inTitleAnimation != null || inSubtitleAnimation != null) {
            return
        }

        outTitleAnimation?.cancel()
        outTitleAnimation = null
        outSubtitleAnimation?.cancel()
        outSubtitleAnimation = null

        val titleAnimation = titleView.animate()
                .alpha(1f)
                .setDuration(titleAlphaAnimationDuration)
                .setInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR)
                .setListener(clearInTitleAnimation)

        val subtitleAnimation = subtitleView.animate()
                .alpha(1f)
                .setDuration(titleAlphaAnimationDuration)
                .setInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR)
                .setListener(clearInSubtitleAnimation)

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

        val titleAnimation = titleView.animate()
                .alpha(0f)
                .setDuration(titleAlphaAnimationDuration)
                .setInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR)
                .setListener(clearOutTitleAnimation)

        val subtitleAnimation = subtitleView.animate()
                .alpha(0f)
                .setDuration(titleAlphaAnimationDuration)
                .setInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR)
                .setListener(clearOutSubtitleAnimation)

        outTitleAnimation = titleAnimation
        outSubtitleAnimation = subtitleAnimation

        titleAnimation.start()
        subtitleAnimation.start()
    }

    override fun onPaletteBuilt(palette: Palette?) {
        if (!isAlive) {
            return
        }

        val window = activity?.window ?: return

        val toolbarBackgroundFallback = colorPrimary
        val statusBarBackgroundFallback = colorPrimaryDark
        val swatch = palette?.darkVibrantSwatch ?: palette?.darkMutedSwatch

        @ColorInt val toolbarBackground: Int
        @ColorInt val systemWindowBackground: Int

        if (swatch == null) {
            toolbarBackground = toolbarBackgroundFallback
            systemWindowBackground = statusBarBackgroundFallback
        } else {
            toolbarBackground = ColorUtils.brightenOrDarkenColorIfLightnessIs(swatch.rgb,
                    TOO_LIGHT_DARKEN_FACTOR, LIGHTNESS_LIMIT)
            systemWindowBackground = ColorUtils.brightenOrDarkenColor(toolbarBackground,
                    STATUS_BAR_DARKEN_FACTOR)
        }

        val toolbarAnimator = ValueAnimator.ofArgb(
                background?.colorCompat ?: toolbarBackgroundFallback, toolbarBackground)
        toolbarAnimator.addUpdateListener(backgroundColorAnimatorUpdateListener)

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

    private fun refreshTitleContainerVisibility() {
        titleView.visibility = if (hasTitleText) VISIBLE else GONE
        subtitleView.visibility = if (hasSubtitleText) VISIBLE else GONE
        showTitleContainer = titleView.visibility == VISIBLE || subtitleView.visibility == VISIBLE
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

    private val backgroundColorAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            setBackgroundColor(it.animatedValue as Int)
        }
    }

    private var inTitleAnimation: ViewPropertyAnimator? = null
    private var inSubtitleAnimation: ViewPropertyAnimator? = null
    private var outTitleAnimation: ViewPropertyAnimator? = null
    private var outSubtitleAnimation: ViewPropertyAnimator? = null

    /////////////////////////////
    // END ANIMATION VARIABLES //
    /////////////////////////////

    companion object {
        private const val LIGHTNESS_LIMIT = 0.4f
        private const val STATUS_BAR_DARKEN_FACTOR = 0.8f
        private const val TOO_LIGHT_DARKEN_FACTOR = 0.75f
    }

}
