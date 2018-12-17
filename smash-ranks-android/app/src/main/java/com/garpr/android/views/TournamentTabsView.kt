package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.getLong
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.models.TournamentMode
import kotlinx.android.synthetic.main.view_tournament_tabs.view.*

class TournamentTabsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), Refreshable {

    interface Listeners {
        fun onTournamentModeClick(v: TournamentTabsView, tournamentMode: TournamentMode)
        val tournamentMode: TournamentMode
    }

    private val animationDuration: Long by lazy {
        resources.getLong(android.R.integer.config_shortAnimTime)
    }

    @ColorInt
    private val indicatorLineColor: Int

    private var inAnimation: ViewPropertyAnimator? = null
    private var outAnimation: ViewPropertyAnimator? = null


    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TournamentTabsView)
        indicatorLineColor = ta.getColor(R.styleable.TournamentTabsView_indicatorLineColor,
                context.getAttrColor(R.attr.colorAccent))
        ta.recycle()
    }

    fun animateIn() {
        if (inAnimation != null) {
            return
        }

        outAnimation?.cancel()
        outAnimation = null

        val animation = animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR)
                .withEndAction {
                    inAnimation = null
                }

        inAnimation = animation
        animation.start()
    }

    fun animateOut() {
        if (outAnimation != null) {
            return
        }

        inAnimation?.cancel()
        inAnimation = null

        val animation = animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR)
                .withEndAction {
                    outAnimation = null
                }

        outAnimation = animation
        animation.start()
    }

    private val canBeTouched: Boolean
        get() = alpha == 1f && visibility == View.VISIBLE

    private val listeners: Listeners?
        get() = activity as? Listeners

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refresh()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        LayoutInflater.from(context).inflate(R.layout.view_tournament_tabs, this)

        matchesTab.setOnClickListener {
            listeners?.onTournamentModeClick(this, TournamentMode.MATCHES)
            refresh()
        }

        playersTab.setOnClickListener {
            listeners?.onTournamentModeClick(this, TournamentMode.PLAYERS)
            refresh()
        }

        indicatorLine.setBackgroundColor(indicatorLineColor)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (canBeTouched) super.onInterceptTouchEvent(ev) else true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (canBeTouched) super.onTouchEvent(event) else false
    }

    override fun refresh() {
        val layoutParams = indicatorLine.layoutParams as? ConstraintLayout.LayoutParams ?: return

        when (listeners?.tournamentMode) {
            TournamentMode.MATCHES -> {
                layoutParams.endToEnd = matchesTab.id
                layoutParams.startToStart = matchesTab.id
                indicatorLine.visibility = View.VISIBLE
            }

            TournamentMode.PLAYERS -> {
                layoutParams.endToEnd = playersTab.id
                layoutParams.startToStart = playersTab.id
                indicatorLine.visibility = View.VISIBLE
            }

            else -> {
                layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                indicatorLine.visibility = View.INVISIBLE
            }
        }

        indicatorLine.layoutParams = layoutParams
    }

}
