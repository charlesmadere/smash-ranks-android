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
import com.garpr.android.adapters.TournamentMatchesAdapter
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.FullTournament

class TournamentMatchesLayout : TournamentPageLayout {

    private var content: FullTournament? = null
    private lateinit var adapter: TournamentMatchesAdapter


    companion object {
        fun inflate(parent: ViewGroup): TournamentMatchesLayout {
            val inflater = LayoutInflater.from(parent.context)
            return inflater.inflate(R.layout.layout_tournament_matches, parent, false)
                    as TournamentMatchesLayout
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

        recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        adapter = TournamentMatchesAdapter(context)
        recyclerView.adapter = adapter
    }

    override fun search(query: String?) {
        val matches = content?.matches

        if (matches == null || matches.isEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<FullTournament.Match>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                list = ListUtils.searchTournamentMatchesList(query, matches)
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
