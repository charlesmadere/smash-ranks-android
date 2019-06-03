package com.garpr.android.features.tournament

import androidx.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.TournamentMode
import com.garpr.android.features.base.BaseAdapter

class TournamentAdapter(
        private val tournamentAdapterManager: TournamentAdapterManager
) : BaseAdapter<Any>() {

    init {
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
