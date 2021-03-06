package com.garpr.android.features.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
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

        homeBackground.alpha = 255
        homeImageView.setTintedImageColor(colorAccent)
        homeTextView.setTextColor(colorAccent)

        tournamentsBackground.alpha = 0
        tournamentsImageView.setTintedImageColor(textColorPrimary)
        tournamentsTextView.setTextColor(textColorPrimary)
    }

    private fun animateTo(
            @IntRange(from = 0, to = 255) newHomeBackgroundAlpha: Int,
            @ColorInt currentHomeImageTint: Int,
            @ColorInt newHomeImageTint: Int,
            @ColorInt newHomeTextColor: Int,
            @IntRange(from = 0, to = 255) newTournamentsBackgroundAlpha: Int,
            @ColorInt currentTournamentsImageTint: Int,
            @ColorInt newTournamentsImageTint: Int,
            @ColorInt newTournamentsTextColor: Int
    ) {
        homeBackgroundAnimation?.cancel()
        homeImageTintAnimation?.cancel()
        homeTextColorAnimation?.cancel()
        tournamentsBackgroundAnimation?.cancel()
        tournamentsImageTintAnimation?.cancel()
        tournamentsTextColorAnimation?.cancel()

        val homeBackgroundAnimation = ValueAnimator.ofInt(homeBackground.alpha, newHomeBackgroundAlpha)
        homeBackgroundAnimation.addListener(clearHomeBackgroundAnimation)
        homeBackgroundAnimation.addUpdateListener(homeBackgroundAlphaAnimatorUpdateListener)
        this.homeBackgroundAnimation = homeBackgroundAnimation

        val homeImageTintAnimation = ValueAnimator.ofArgb(currentHomeImageTint, newHomeImageTint)
        homeImageTintAnimation.addListener(clearHomeImageTintAnimation)
        homeImageTintAnimation.addUpdateListener(homeImageTintAnimatorUpdateListener)
        this.homeImageTintAnimation = homeImageTintAnimation

        val homeTextColorAnimation = ValueAnimator.ofArgb(homeTextView.currentTextColor, newHomeTextColor)
        homeTextColorAnimation.addListener(clearHomeTextColorAnimation)
        homeTextColorAnimation.addUpdateListener(homeTextColorAnimatorUpdateListener)
        this.homeTextColorAnimation = homeTextColorAnimation

        val tournamentsBackgroundAnimation = ValueAnimator.ofInt(tournamentsBackground.alpha, newTournamentsBackgroundAlpha)
        tournamentsBackgroundAnimation.addListener(clearTournamentsBackgroundAnimation)
        tournamentsBackgroundAnimation.addUpdateListener(tournamentsBackgroundAlphaAnimatorUpdateListener)
        this.tournamentsBackgroundAnimation = tournamentsBackgroundAnimation

        val tournamentsImageTintAnimation = ValueAnimator.ofArgb(currentTournamentsImageTint, newTournamentsImageTint)
        tournamentsImageTintAnimation.addListener(clearTournamentsImageTintAnimation)
        tournamentsImageTintAnimation.addUpdateListener(tournamentsImageTintAnimatorUpdateListener)
        this.tournamentsImageTintAnimation = tournamentsImageTintAnimation

        val tournamentsTextColorAnimation = ValueAnimator.ofArgb(tournamentsTextView.currentTextColor, newTournamentsTextColor)
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

    private fun animateToHome() {
        animateTo(
                newHomeBackgroundAlpha = 255,
                currentHomeImageTint = textColorPrimary,
                newHomeImageTint = colorAccent,
                newHomeTextColor = colorAccent,
                newTournamentsBackgroundAlpha = 0,
                currentTournamentsImageTint = colorAccent,
                newTournamentsImageTint = textColorPrimary,
                newTournamentsTextColor = textColorPrimary
        )
    }

    private fun animateToTournaments() {
        animateTo(
                newHomeBackgroundAlpha = 0,
                currentHomeImageTint = colorAccent,
                newHomeImageTint = textColorPrimary,
                newHomeTextColor = textColorPrimary,
                newTournamentsBackgroundAlpha = 255,
                currentTournamentsImageTint = textColorPrimary,
                newTournamentsImageTint = colorAccent,
                newTournamentsTextColor = colorAccent
        )
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
