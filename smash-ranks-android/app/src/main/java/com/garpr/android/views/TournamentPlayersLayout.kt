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

    private var mContent: FullTournament? = null
    lateinit private var mAdapter: PlayersAdapter


    companion object {
        fun inflate(parent: ViewGroup): TournamentPlayersLayout {
            val inflater = LayoutInflater.from(parent.context)
            return inflater.inflate(R.layout.layout_tournament_players, parent, false)
                    as TournamentPlayersLayout
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

        mRecyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        mRecyclerView.setHasFixedSize(true)
        mAdapter = PlayersAdapter(context)
        mRecyclerView.adapter = mAdapter
    }

    override fun search(query: String?) {
        val players = mContent?.players

        if (players == null || players.isEmpty()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<AbsPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchPlayerList(query, players)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mAdapter.set(mList)
            }
        })
    }

    override fun setContent(content: FullTournament) {
        mContent = content
        mAdapter.set(content)

        if (mAdapter.isEmpty) {
            mRecyclerView.visibility = GONE
            mEmpty.visibility = VISIBLE
        } else {
            mEmpty.visibility = GONE
            mRecyclerView.visibility = VISIBLE
        }
    }

}
