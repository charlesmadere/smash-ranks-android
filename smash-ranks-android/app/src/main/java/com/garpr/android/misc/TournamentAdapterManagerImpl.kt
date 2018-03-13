package com.garpr.android.misc

import android.app.Application
import com.garpr.android.R
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.AbsTournament
import com.garpr.android.models.FullTournament
import com.garpr.android.models.Match

class TournamentAdapterManagerImpl(
        private val application: Application
) : TournamentAdapterManager {

    companion object {
        private val TABS = Any()
    }

    override fun buildMatchesList(content: FullTournament): List<Any> {
        val list = mutableListOf<Any>()
        list.add(content)
        list.add(TABS)

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
        list.add(TABS)

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

            1 -> {
                Long.MIN_VALUE + 1L
            }

            else -> {
                item.hashCode().toLong()
            }
        }
    }

    override fun getItemViewType(position: Int, item: Any): TournamentAdapterManager.ViewType {
        return when {
            position == 0 -> {
                TournamentAdapterManager.ViewType.INFO
            }

            position == 1 -> {
                TournamentAdapterManager.ViewType.TABS
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
