package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.TournamentAdapterManager
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FullTournament
import com.garpr.android.models.TournamentMode
import javax.inject.Inject

class TournamentAdapter(context: Context) : BaseAdapter<Any>(context) {

    @Inject
    protected lateinit var tournamentAdapterManager: TournamentAdapterManager


    init {
        context.appComponent.inject(this)
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return tournamentAdapterManager.getItemId(position, getItem(position))
    }

    @LayoutRes
    override fun getItemViewType(position: Int): Int =
        when (tournamentAdapterManager.getItemViewType(position, getItem(position))) {
            TournamentAdapterManager.ViewType.MATCH -> {
                R.layout.item_tournament_match
            }

            TournamentAdapterManager.ViewType.MESSAGE -> {
                R.layout.item_string
            }

            TournamentAdapterManager.ViewType.PLAYER -> {
                R.layout.item_player
            }

            TournamentAdapterManager.ViewType.TOURNAMENT_INFO -> {
                R.layout.item_tournament_info
            }
        }

    fun set(mode: TournamentMode, content: FullTournament) {
        when (mode) {
            TournamentMode.MATCHES -> {
                set(tournamentAdapterManager.buildMatchesList(content))
            }

            TournamentMode.PLAYERS -> {
                set(tournamentAdapterManager.buildPlayersList(content))
            }
        }
    }

    fun setSearchedMatchesList(content: FullTournament, matches: List<FullTournament.Match>?) {
        set(tournamentAdapterManager.buildSearchedMatchesList(content, matches))
    }

    fun setSearchedPlayersList(content: FullTournament, players: List<AbsPlayer>?) {
        set(tournamentAdapterManager.buildSearchedPlayersList(content, players))
    }

}
