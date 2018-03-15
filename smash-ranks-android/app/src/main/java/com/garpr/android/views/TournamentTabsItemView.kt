package com.garpr.android.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.TournamentModeListeners
import com.garpr.android.models.TournamentMode
import kotterknife.bindView

class TournamentTabsItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<Any?>, Refreshable {

    private var enableScrollListener = false

    private val matchesTab: TextView by bindView(R.id.tvMatchesTab)
    private val playersTab: TextView by bindView(R.id.tvPlayersTab)
    private var scrollListener: RecyclerView.OnScrollListener? = null


    init {
        parseAttributes(attrs)
    }

    private fun attachScrollListener() {
        val recyclerView = findRecyclerViewParent(parent as? View)
                ?: throw NullPointerException("couldn't find parent RecyclerView")

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

    private fun findRecyclerViewParent(view: View?): RecyclerView? {
        return view as? RecyclerView ?: findRecyclerViewParent(view?.parent as? View)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refresh()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

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
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TournamentTabsItemView)
        enableScrollListener = ta.getBoolean(R.styleable.TournamentTabsItemView_enableScrollListener, enableScrollListener)
        ta.recycle()
    }

    override fun refresh() {
        if (enableScrollListener) {
            attachScrollListener()
        }

        // TODO

        when (tournamentModeListeners?.tournamentMode) {
            TournamentMode.MATCHES -> {

            }

            TournamentMode.PLAYERS -> {

            }

            else -> {

            }
        }
    }

    override fun setContent(content: Any?) {
        refresh()
    }

    private val tournamentModeListeners: TournamentModeListeners?
        get() = context.optActivity() as? TournamentModeListeners

}
