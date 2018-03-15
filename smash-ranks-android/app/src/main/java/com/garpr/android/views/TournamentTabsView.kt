package com.garpr.android.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.TournamentModeListeners
import com.garpr.android.models.TournamentMode
import kotterknife.bindView

class TournamentTabsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<Any?>, Refreshable {

    private var enableScrollListener = false
    private var scrollListener: RecyclerView.OnScrollListener? = null

    private val matchesTab: TextView by bindView(R.id.tvMatchesTab)
    private val playersTab: TextView by bindView(R.id.tvPlayersTab)
    private val indicatorLine: View by bindView(R.id.indicatorLine)


    init {
        parseAttributes(attrs)
    }

    private fun attachScrollListener() {
        val recyclerView = findRecyclerViewParent(parent as? View)
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

    private fun findRecyclerViewParent(view: View?): RecyclerView {
        return view as? RecyclerView ?: findRecyclerViewParent(view?.parent as? View)
                ?: throw NullPointerException("couldn't find parent RecyclerView")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refresh()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        LayoutInflater.from(context).inflate(R.layout.view_tournament_tabs, this)

        matchesTab.setOnClickListener {
            tournamentModeListeners?.onTournamentModeClick(this, TournamentMode.MATCHES)
            refresh()
        }

        playersTab.setOnClickListener {
            tournamentModeListeners?.onTournamentModeClick(this, TournamentMode.PLAYERS)
            refresh()
        }

        refresh()
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TournamentTabsView)
        enableScrollListener = ta.getBoolean(R.styleable.TournamentTabsView_enableScrollListener,
                enableScrollListener)
        ta.recycle()
    }

    override fun refresh() {
        if (enableScrollListener) {
            attachScrollListener()
        }

        val layoutParams = indicatorLine.layoutParams as? ConstraintLayout.LayoutParams ?: return

        when (tournamentModeListeners?.tournamentMode) {
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

    private val tournamentModeListeners: TournamentModeListeners?
        get() = context.optActivity() as? TournamentModeListeners

}
