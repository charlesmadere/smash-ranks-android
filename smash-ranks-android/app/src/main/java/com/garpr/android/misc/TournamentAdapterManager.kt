package com.garpr.android.misc

import com.garpr.android.models.FullTournament

interface TournamentAdapterManager {

    enum class ViewType {
        MATCH, MESSAGE, PLAYER, TOURNAMENT_INFO
    }

    fun buildMatchesList(content: FullTournament): List<Any>

    fun buildPlayersList(content: FullTournament): List<Any>

    fun getItemId(position: Int, item: Any): Long

    fun getItemViewType(position: Int, item: Any): ViewType

}
