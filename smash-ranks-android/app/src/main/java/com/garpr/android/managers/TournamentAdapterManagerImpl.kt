package com.garpr.android.managers

import android.app.Application
import com.garpr.android.R
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FullTournament
import com.garpr.android.models.FullTournament.Match

class TournamentAdapterManagerImpl(
        private val application: Application
) : TournamentAdapterManager {

    override fun buildMatchesList(content: FullTournament): List<Any> {
        val list = mutableListOf<Any>()
        list.add(content)

        if (content.matches?.isNotEmpty() == true) {
            list.addAll(content.matches)
        } else {
            list.add(application.getString(R.string.this_tournament_has_no_matches))
        }

        return list
    }

    override fun buildPlayersList(content: FullTournament): List<Any> {
        val list = mutableListOf<Any>()
        list.add(content)

        if (content.players?.isNotEmpty() == true) {
            list.addAll(content.players)
        } else {
            list.add(application.getString(R.string.this_tournament_has_no_players))
        }

        return list
    }

    override fun getItemId(position: Int, item: Any): Long {
        return when (position) {
            0 -> {
                Long.MIN_VALUE
            }

            else -> {
                item.hashCode().toLong()
            }
        }
    }

    override fun getItemViewType(position: Int, item: Any): TournamentAdapterManager.ViewType {
        return when {
            position == 0 -> {
                TournamentAdapterManager.ViewType.TOURNAMENT_INFO
            }

            item is AbsPlayer -> {
                TournamentAdapterManager.ViewType.PLAYER
            }

            item is Match -> {
                TournamentAdapterManager.ViewType.MATCH
            }

            item is String -> {
                TournamentAdapterManager.ViewType.MESSAGE
            }

            else -> {
                throw IllegalArgumentException("position $position has an unknown item: $item")
            }
        }
    }

}
