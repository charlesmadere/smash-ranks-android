package com.garpr.android.views

import android.content.Context
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.optActivity
import com.garpr.android.extensions.requireViewByIdFromRoot
import com.garpr.android.misc.Refreshable
import com.garpr.android.models.TournamentMode
import kotterknife.bindView

class TournamentTabsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<Any?>, Refreshable {

    @IdRes
    private var recyclerViewId: Int = View.NO_ID
    private var scrollListener: RecyclerView.OnScrollListener? = null

    private val matchesTab: TextView by bindView(R.id.tvMatchesTab)
    private val playersTab: TextView by bindView(R.id.tvPlayersTab)
    private val indicatorLine: View by bindView(R.id.indicatorLine)


    interface Listeners {
        fun onTournamentModeClick(v: TournamentTabsView, tournamentMode: TournamentMode)
        val tournamentMode: TournamentMode

    }

    init {
        parseAttributes(attrs)
    }

    private fun attachScrollListener() {
        val recyclerView: RecyclerView = requireViewByIdFromRoot(recyclerViewId)
        var scrollListener = this.scrollListener

        if (scrollListener == null) {
            scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    // TODO
                }
            }

            this.scrollListener = scrollListener
        } else {
            recyclerView.removeOnScrollListener(scrollListener)
        }

        recyclerView.addOnScrollListener(scrollListener)
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

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TournamentTabsView)
        recyclerViewId = ta.getResourceId(R.styleable.TournamentTabsView_recyclerViewId, View.NO_ID)
        ta.recycle()
    }

    override fun refresh() {
        if (recyclerViewId != View.NO_ID) {
            attachScrollListener()
        }

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
