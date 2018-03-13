package com.garpr.android.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.models.TournamentMode
import kotterknife.bindView

class TournamentTabsItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<Any?>, Refreshable {

    private var enableScrollListener = false

    private val matches: TextView by bindView(R.id.tvMatches)
    private val players: TextView by bindView(R.id.tvPlayers)


    interface Listeners {
        fun onTournamentModeClick(v: TournamentTabsItemView, tournamentMode: TournamentMode)
        val tournamentMode: TournamentMode
    }

    init {
        parseAttributes(attrs)
    }

    private fun attachScrollListener() {
        // TODO
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TournamentTabsItemView)
        enableScrollListener = ta.getBoolean(R.styleable.TournamentTabsItemView_enableScrollListener, enableScrollListener)
        ta.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refresh()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        matches.setOnClickListener {
            (context.optActivity() as? Listeners)?.onTournamentModeClick(this,
                    TournamentMode.MATCHES)
            refresh()
        }

        players.setOnClickListener {
            (context.optActivity() as? Listeners)?.onTournamentModeClick(this,
                    TournamentMode.PLAYERS)
            refresh()
        }

        refresh()
    }

    override fun refresh() {
        if (enableScrollListener) {
            attachScrollListener()
        }

        // TODO

        when ((context.optActivity() as? Listeners)?.tournamentMode) {
            TournamentMode.MATCHES -> {

            }

            TournamentMode.PLAYERS -> {

            }

            else -> {

            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // TODO
        }
    }

    override fun setContent(content: Any?) {
        refresh()
    }

}
