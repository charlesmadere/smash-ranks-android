package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.widget.DividerItemDecoration
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.adapters.PlayersAdapter
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FullTournament

class TournamentPlayersLayout : TournamentPageLayout {

    private var content: FullTournament? = null
    private lateinit var adapter: PlayersAdapter


    companion object {
        fun inflate(parent: ViewGroup): TournamentPlayersLayout = LayoutInflater.from(
                parent.context).inflate(R.layout.layout_tournament_players, parent,
                false) as TournamentPlayersLayout
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

        recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        adapter = PlayersAdapter(context)
        recyclerView.adapter = adapter
    }

    override fun search(query: String?) {
        val players = content?.players

        if (players == null || players.isEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<AbsPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                list = ListUtils.searchPlayerList(query, players)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                adapter.set(list)
            }
        })
    }

    override fun setContent(content: FullTournament) {
        this.content = content
        adapter.set(content)

        if (adapter.isEmpty) {
            recyclerView.visibility = GONE
            empty.visibility = VISIBLE
        } else {
            empty.visibility = GONE
            recyclerView.visibility = VISIBLE
        }
    }

}
