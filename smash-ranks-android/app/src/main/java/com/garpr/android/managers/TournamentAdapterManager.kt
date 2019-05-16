package com.garpr.android.managers

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullTournament

interface TournamentAdapterManager {

    enum class ViewType {
        MATCH, MESSAGE, PLAYER, TOURNAMENT_INFO
    }

    fun buildMatchesList(content: FullTournament): List<Any>

    fun buildPlayersList(content: FullTournament): List<Any>

    fun buildSearchedMatchesList(content: FullTournament, matches: List<FullTournament.Match>?): List<Any>

    fun buildSearchedPlayersList(content: FullTournament, players: List<AbsPlayer>?): List<Any>

    fun getItemId(position: Int, item: Any): Long

    fun getItemViewType(position: Int, item: Any): ViewType

}
