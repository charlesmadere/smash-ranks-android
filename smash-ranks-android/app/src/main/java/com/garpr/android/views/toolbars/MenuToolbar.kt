package com.garpr.android.views.toolbars

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.Toolbar
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.garpr.android.R
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.Refreshable

abstract class MenuToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : Toolbar(context, attrs), Heartbeat, Refreshable {

    private val sparseMenuItemsArray = SparseBooleanArray()

    // Animation Variables
    private var alphaColorSpan: ForegroundColorSpan? = null

    private val animationDuration: Long by lazy {
        resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    }

    private var inTitleAnimation: ValueAnimator? = null
    private var inSubtitleAnimation: ValueAnimator? = null
    private var outTitleAnimation: ValueAnimator? = null
    private var outSubtitleAnimation: ValueAnimator? = null

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
    // end Animation Variables


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

        val titleAnimation = ValueAnimator.ofInt(ContextCompat.getColor(context, R.color.transparent),
                context.getAttrColor(android.R.attr.textColorPrimary))
        titleAnimation.addUpdateListener(titleAnimatorUpdateListener)
        titleAnimation.duration = animationDuration
        titleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

        titleAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                inTitleAnimation = null
            }
        })

        val subtitleAnimation = ValueAnimator.ofInt(ContextCompat.getColor(context, R.color.transparent),
                context.getAttrColor(android.R.attr.textColorSecondary))
        subtitleAnimation.addUpdateListener(subtitleAnimatorUpdateListener)
        subtitleAnimation.duration = animationDuration
        subtitleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

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

        val titleAnimation = ValueAnimator.ofInt(context.getAttrColor(android.R.attr.textColorPrimary),
                ContextCompat.getColor(context, R.color.transparent))
        titleAnimation.addUpdateListener(titleAnimatorUpdateListener)
        titleAnimation.duration = animationDuration
        titleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

        titleAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                outTitleAnimation = null
            }
        })

        val subtitleAnimation = ValueAnimator.ofInt(context.getAttrColor(android.R.attr.textColorSecondary),
                ContextCompat.getColor(context, R.color.transparent))
        subtitleAnimation.addUpdateListener(subtitleAnimatorUpdateListener)
        subtitleAnimation.duration = animationDuration
        subtitleAnimation.interpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR

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

}
