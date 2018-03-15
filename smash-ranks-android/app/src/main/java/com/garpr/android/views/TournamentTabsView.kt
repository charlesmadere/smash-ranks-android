package com.garpr.android.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.models.TournamentMode
import kotterknife.bindView

class TournamentTabsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<Any?>, Refreshable {

    private val matchesTab: TextView by bindView(R.id.tvMatchesTab)
    private val playersTab: TextView by bindView(R.id.tvPlayersTab)
    private val indicatorLine: View by bindView(R.id.indicatorLine)


    interface Listeners {
        fun onTournamentModeClick(v: TournamentTabsView, tournamentMode: TournamentMode)
        val tournamentMode: TournamentMode

    }

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

    override fun setContent(content: Any?) {
        refresh()
    }

    private val listeners: Listeners?
        get() = context.optActivity() as? Listeners

}
