package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.getLong
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.models.TournamentMode
import kotterknife.bindView

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
    private var indicatorLineColor: Int = Color.TRANSPARENT

    private var inAnimation: ViewPropertyAnimator? = null
    private var outAnimation: ViewPropertyAnimator? = null

    private val matchesTab: TextView by bindView(R.id.tvMatchesTab)
    private val playersTab: TextView by bindView(R.id.tvPlayersTab)
    private val indicatorLine: View by bindView(R.id.indicatorLine)


    init {
        parseAttributes(attrs)
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

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TournamentTabsView)
        indicatorLineColor = ta.getColor(R.styleable.TournamentTabsView_indicatorLineColor,
                context.getAttrColor(R.attr.colorAccent))
        ta.recycle()
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
