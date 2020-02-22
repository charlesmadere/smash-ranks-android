package com.garpr.android.features.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.getLong
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.requireDrawable
import com.garpr.android.extensions.setTintedImageColor
import com.garpr.android.misc.AnimationUtils
import kotlinx.android.synthetic.main.view_bottom_navigation.view.*

class BottomNavigationView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val homeBackground = context.requireDrawable(R.drawable.bottom_navigation_selection)
            .mutate()

    private val tournamentsBackground = context.requireDrawable(R.drawable.bottom_navigation_selection)
            .mutate()

    var selection: HomeTab = HomeTab.HOME
        set(value) {
            if (field == value) {
                return
            }

            field = value

            when (value) {
                HomeTab.HOME -> animateToHome()
                HomeTab.TOURNAMENTS -> animateToTournaments()
            }
        }

    @ColorInt
    private val colorAccent: Int = context.getAttrColor(R.attr.colorAccent)

    @ColorInt
    private val textColorPrimary: Int = context.getAttrColor(android.R.attr.textColorPrimary)

    var listeners: Listeners? = null

    private val homeTabClickListener = OnClickListener {
        if (selection == HomeTab.HOME) {
            listeners?.onHomeTabReselected(this, HomeTab.HOME)
        } else {
            selection = HomeTab.HOME
            listeners?.onHomeTabClick(this, HomeTab.HOME)
        }
    }

    private val tournamentsTabClickListener = OnClickListener {
        if (selection == HomeTab.TOURNAMENTS) {
            listeners?.onHomeTabReselected(this, HomeTab.TOURNAMENTS)
        } else {
            selection = HomeTab.TOURNAMENTS
            listeners?.onHomeTabClick(this, HomeTab.TOURNAMENTS)
        }
    }

    init {
        layoutInflater.inflate(R.layout.view_bottom_navigation, this)

        ViewCompat.setBackground(homeCell, homeBackground)
        homeClickArea.setOnClickListener(homeTabClickListener)
        ViewCompat.setBackground(tournamentsCell, tournamentsBackground)
        tournamentsClickArea.setOnClickListener(tournamentsTabClickListener)
    }

    private fun animateToHome() {
        homeBackgroundAnimation?.cancel()
        homeTextColorAnimation?.cancel()
        tournamentsBackgroundAnimation?.cancel()
        tournamentsTextColorAnimation?.cancel()

        val homeBackgroundAnimation = ValueAnimator.ofInt(homeBackground.alpha, 255)
        homeBackgroundAnimation.addListener(clearHomeBackgroundAnimation)
        homeBackgroundAnimation.addUpdateListener(homeBackgroundAlphaAnimatorUpdateListener)
        this.homeBackgroundAnimation = homeBackgroundAnimation

        val homeImageTintAnimation = ValueAnimator.ofArgb(textColorPrimary, colorAccent)
        homeImageTintAnimation.addListener(clearHomeImageTintAnimation)
        homeImageTintAnimation.addUpdateListener(homeImageTintAnimatorUpdateListener)
        this.homeImageTintAnimation = homeImageTintAnimation

        val homeTextColorAnimation = ValueAnimator.ofArgb(homeTextView.currentTextColor,
                colorAccent)
        homeTextColorAnimation.addListener(clearHomeTextColorAnimation)
        homeTextColorAnimation.addUpdateListener(homeTextColorAnimatorUpdateListener)
        this.homeTextColorAnimation = homeTextColorAnimation

        val tournamentsBackgroundAnimation = ValueAnimator.ofInt(tournamentsBackground.alpha, 0)
        tournamentsBackgroundAnimation.addListener(clearTournamentsBackgroundAnimation)
        tournamentsBackgroundAnimation.addUpdateListener(tournamentsBackgroundAlphaAnimatorUpdateListener)
        this.tournamentsBackgroundAnimation = tournamentsBackgroundAnimation

        val tournamentsImageTintAnimation = ValueAnimator.ofArgb(colorAccent, textColorPrimary)
        tournamentsImageTintAnimation.addListener(clearTournamentsImageTintAnimation)
        tournamentsImageTintAnimation.addUpdateListener(tournamentsImageTintAnimatorUpdateListener)
        this.tournamentsImageTintAnimation = tournamentsImageTintAnimation

        val tournamentsTextColorAnimation = ValueAnimator.ofArgb(
                tournamentsTextView.currentTextColor, textColorPrimary)
        tournamentsTextColorAnimation.addListener(clearTournamentsTextColorAnimation)
        tournamentsTextColorAnimation.addUpdateListener(tournamentsTextColorAnimatorUpdateListener)
        this.tournamentsTextColorAnimation = tournamentsTextColorAnimation

        val animatorSet = AnimatorSet()
        animatorSet.duration = animationDuration
        animatorSet.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR
        animatorSet.playTogether(homeBackgroundAnimation, homeImageTintAnimation,
                homeTextColorAnimation, tournamentsBackgroundAnimation,
                tournamentsImageTintAnimation, tournamentsTextColorAnimation)
        animatorSet.start()
    }

    private fun animateToTournaments() {
        homeBackgroundAnimation?.cancel()
        homeTextColorAnimation?.cancel()
        tournamentsBackgroundAnimation?.cancel()
        tournamentsTextColorAnimation?.cancel()

        val homeBackgroundAnimation = ValueAnimator.ofInt(homeBackground.alpha, 0)
        homeBackgroundAnimation.addListener(clearHomeBackgroundAnimation)
        homeBackgroundAnimation.addUpdateListener(homeBackgroundAlphaAnimatorUpdateListener)
        this.homeBackgroundAnimation = homeBackgroundAnimation

        val homeImageTintAnimation = ValueAnimator.ofArgb(colorAccent, textColorPrimary)
        homeImageTintAnimation.addListener(clearHomeImageTintAnimation)
        homeImageTintAnimation.addUpdateListener(homeImageTintAnimatorUpdateListener)
        this.homeImageTintAnimation = homeImageTintAnimation

        val homeTextColorAnimation = ValueAnimator.ofArgb(homeTextView.currentTextColor,
                textColorPrimary)
        homeTextColorAnimation.addListener(clearHomeTextColorAnimation)
        homeTextColorAnimation.addUpdateListener(homeTextColorAnimatorUpdateListener)
        this.homeTextColorAnimation = homeTextColorAnimation

        val tournamentsBackgroundAnimation = ValueAnimator.ofInt(tournamentsBackground.alpha, 255)
        tournamentsBackgroundAnimation.addListener(clearTournamentsBackgroundAnimation)
        tournamentsBackgroundAnimation.addUpdateListener(tournamentsBackgroundAlphaAnimatorUpdateListener)
        this.tournamentsBackgroundAnimation = tournamentsBackgroundAnimation

        val tournamentsImageTintAnimation = ValueAnimator.ofArgb(textColorPrimary, colorAccent)
        tournamentsImageTintAnimation.addListener(clearTournamentsImageTintAnimation)
        tournamentsImageTintAnimation.addUpdateListener(tournamentsImageTintAnimatorUpdateListener)
        this.tournamentsImageTintAnimation = tournamentsImageTintAnimation

        val tournamentsTextColorAnimation = ValueAnimator.ofArgb(
                tournamentsTextView.currentTextColor, colorAccent)
        tournamentsTextColorAnimation.addListener(clearTournamentsTextColorAnimation)
        tournamentsTextColorAnimation.addUpdateListener(tournamentsTextColorAnimatorUpdateListener)
        this.tournamentsTextColorAnimation = tournamentsTextColorAnimation

        val animatorSet = AnimatorSet()
        animatorSet.duration = animationDuration
        animatorSet.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR
        animatorSet.playTogether(homeBackgroundAnimation, homeImageTintAnimation,
                homeTextColorAnimation, tournamentsBackgroundAnimation,
                tournamentsImageTintAnimation, tournamentsTextColorAnimation)
        animatorSet.start()
    }

    interface Listeners {
        fun onHomeTabClick(v: BottomNavigationView, homeTab: HomeTab)
        fun onHomeTabReselected(v: BottomNavigationView, homeTab: HomeTab)
    }

    ///////////////////////////////
    // BEGIN ANIMATION VARIABLES //
    ///////////////////////////////

    private val animationDuration: Long by lazy {
        resources.getLong(R.integer.bottom_navigation_animation_duration)
    }

    private val clearHomeBackgroundAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                homeBackgroundAnimation = null
            }
        }
    }

    private val clearHomeImageTintAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                homeImageTintAnimation = null
            }
        }
    }

    private val clearHomeTextColorAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                homeTextColorAnimation = null
            }
        }
    }

    private val clearTournamentsBackgroundAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                tournamentsBackgroundAnimation = null
            }
        }
    }

    private val clearTournamentsImageTintAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                tournamentsImageTintAnimation = null
            }
        }
    }

    private val clearTournamentsTextColorAnimation: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                tournamentsTextColorAnimation = null
            }
        }
    }

    private val homeBackgroundAlphaAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            homeBackground.alpha = it.animatedValue as Int
        }
    }

    private val homeImageTintAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            homeImageView.setTintedImageColor(it.animatedValue as Int)
        }
    }

    private val homeTextColorAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            homeTextView.setTextColor(it.animatedValue as Int)
        }
    }

    private val tournamentsBackgroundAlphaAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            tournamentsBackground.alpha = it.animatedValue as Int
        }
    }

    private val tournamentsImageTintAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            tournamentsImageView.setTintedImageColor(it.animatedValue as Int)
        }
    }

    private val tournamentsTextColorAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            tournamentsTextView.setTextColor(it.animatedValue as Int)
        }
    }

    private var homeBackgroundAnimation: ValueAnimator? = null
    private var homeImageTintAnimation: ValueAnimator? = null
    private var homeTextColorAnimation: ValueAnimator? = null
    private var tournamentsBackgroundAnimation: ValueAnimator? = null
    private var tournamentsImageTintAnimation: ValueAnimator? = null
    private var tournamentsTextColorAnimation: ValueAnimator? = null

    /////////////////////////////
    // END ANIMATION VARIABLES //
    /////////////////////////////

}
