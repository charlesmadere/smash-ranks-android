package com.garpr.android.features.tournament

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.R
import com.garpr.android.data.models.TournamentMode
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.misc.Refreshable
import kotlinx.android.synthetic.main.view_tournament_tabs.view.*

class TournamentTabsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), Refreshable {

    private val matchesTabClickListener = OnClickListener {
        tournamentMode = TournamentMode.MATCHES
        onTabClickListener?.onTabClick(this)
    }

    private val playersTabClickListener = OnClickListener {
        tournamentMode = TournamentMode.PLAYERS
        onTabClickListener?.onTabClick(this)
    }

    var onTabClickListener: OnTabClickListener? = null

    var tournamentMode: TournamentMode = TournamentMode.MATCHES
        set(value) {
            field = value
            refresh()
        }

    interface OnTabClickListener {
        fun onTabClick(v: TournamentTabsView)
    }

    init {
        @Suppress("LeakingThis")
        layoutInflater.inflate(R.layout.view_tournament_tabs, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.TournamentTabsView)
        val indicatorLineColor = ta.getColor(R.styleable.TournamentTabsView_indicatorLineColor,
                context.getAttrColor(R.attr.colorAccent))
        ta.recycle()

        matchesTab.setOnClickListener(matchesTabClickListener)
        playersTab.setOnClickListener(playersTabClickListener)
        indicatorLine.setBackgroundColor(indicatorLineColor)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refresh()
    }

    override fun refresh() {
        val layoutParams = indicatorLine.layoutParams as? LayoutParams? ?: return

        when (tournamentMode) {
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
        }

        indicatorLine.layoutParams = layoutParams
    }

}
