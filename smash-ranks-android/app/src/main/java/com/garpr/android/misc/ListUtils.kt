package com.garpr.android.misc

import android.content.Context
import com.garpr.android.R
import com.garpr.android.models.*
import java.util.*

object ListUtils {

    private interface MatchListItemCreator {
        fun createMatch(match: Match): Any
    }

    fun createHeadToHeadList(context: Context, headToHead: HeadToHead?): MutableList<Any>? {
        if (headToHead == null) {
            return null
        }

        val list = mutableListOf<Any>()
        list.add(WinsLosses(headToHead.player, headToHead.wins, headToHead.opponent,
                headToHead.losses))

        val matches = headToHead.matches
        if (matches.isNullOrEmpty()) {
            list.add(context.getString(R.string.these_two_competitors_have_never_played_in_tournament_before))
            return list
        }

        list.addAll(createSortedTournamentAndMatchList(matches, object : MatchListItemCreator {
            override fun createMatch(match: Match): Any {
                return HeadToHeadMatch(match.result, headToHead.player, match.opponent)
            }
        }))

        return list
    }

    fun createPlayerMatchesList(context: Context, fullPlayer: FullPlayer,
            bundle: MatchesBundle?): MutableList<Any>? {
        val newList = mutableListOf<Any>()
        newList.add(fullPlayer)

        val matches = bundle?.matches

        if (matches.isNullOrEmpty()) {
            newList.add(context.getString(R.string.no_matches))
        } else {
            newList.addAll(createSortedTournamentAndMatchList(matches, object : MatchListItemCreator {
                override fun createMatch(match: Match): Any {
                    return match
                }
            }))
        }

        return newList
    }

    fun createRegionsList(bundle: RegionsBundle?): List<Any>? {
        val regions = bundle?.regions

        if (regions.isNullOrEmpty()) {
            return null
        }

        val regionsCopy = mutableListOf<AbsRegion>()
        regionsCopy.addAll(regions)
        Collections.sort(regionsCopy, AbsRegion.ENDPOINT_ORDER)

        val list = mutableListOf<Any>()
        var endpoint: Endpoint? = null

        for (region in regionsCopy) {
            if (region is Region) {
                if (region.isActive) {
                    if (region.endpoint != endpoint) {
                        endpoint = region.endpoint
                        list.add(region.endpoint)
                    }

                    list.add(region)
                }
            } else {
                throw RuntimeException("$region is a ${LiteRegion::class.java.simpleName} " +
                        "when it must be a ${Region::class.java.simpleName}")
            }
        }

        return list
    }

    private fun createSortedTournamentAndMatchList(matches: List<Match>,
            matchListItemCreator: MatchListItemCreator): MutableList<Any> {
        val matchesCopy = mutableListOf<Match>()
        matchesCopy.addAll(matches)
        Collections.sort(matchesCopy, Match.REVERSE_CHRONOLOGICAL_ORDER)

        val list = mutableListOf<Any>()
        var tournamentId: String? = null

        for (match in matchesCopy) {
            if (match.tournament.id != tournamentId) {
                tournamentId = match.tournament.id
                list.add(LiteTournament(null, match.tournament.date, tournamentId,
                        match.tournament.name))
            }

            list.add(matchListItemCreator.createMatch(match))
        }

        return list
    }

    fun createTournamentList(bundle: TournamentsBundle?): List<AbsTournament>? {
        val tournaments = bundle?.tournaments

        return if (tournaments.isNullOrEmpty()) {
            null
        } else {
            val tournamentsCopy = mutableListOf<AbsTournament>()
            tournamentsCopy.addAll(tournaments)
            Collections.sort(tournamentsCopy, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)
            tournamentsCopy
        }
    }

    fun searchPlayerList(query: String?, list: List<AbsPlayer>?) =
        if (list.isNullOrEmpty()) {
            null
        } else {
            val newList = mutableListOf<AbsPlayer>()

            if (query.isNullOrBlank()) {
                newList.addAll(list)
            } else {
                val trimmedQuery = query.trim()
                newList.addAll(list.filter { it.name.contains(trimmedQuery, true) })
            }

            newList
        }

    fun searchPlayerMatchesList(query: String?, list: List<Any>?): MutableList<Any>? {
        if (list.isNullOrEmpty()) {
            return null
        }

        val newList = mutableListOf<Any>()

        if (query.isNullOrBlank()) {
            newList.addAll(list)
            return newList
        }

        val trimmedQuery = query.trim()
        var addedFullPlayer = false

        for (i in list.indices) {
            val objectI = list[i]

            if (objectI is FullPlayer) {
                if (!addedFullPlayer) {
                    addedFullPlayer = true
                    newList.add(objectI)
                }
            } else if (objectI is AbsTournament) {
                var addedTournament = false
                var j = i + 1

                while (j < list.size) {
                    val objectJ = list[j]

                    if (objectJ is Match) {
                        if (objectJ.opponent.name.contains(trimmedQuery, true)) {
                            if (!addedTournament) {
                                addedTournament = true
                                newList.add(objectI)
                            }

                            newList.add(objectJ)
                        }

                        ++j
                    } else {
                        j = list.size
                    }
                }
            }
        }

        return newList
    }

    fun searchRankingList(query: String?, list: List<RankedPlayer>?) =
        if (list.isNullOrEmpty()) {
            null
        } else {
            val newList = mutableListOf<RankedPlayer>()

            if (query.isNullOrBlank()) {
                newList.addAll(list)
            } else {
                val trimmedQuery = query.trim()
                newList.addAll(list.filter { it.name.contains(trimmedQuery, true) })
            }

            newList
        }

    fun searchTournamentList(query: String?, list: List<AbsTournament>?) =
        if (list.isNullOrEmpty()) {
            null
        } else {
            val newList = mutableListOf<AbsTournament>()

            if (query.isNullOrBlank()) {
                newList.addAll(list)
            } else {
                val trimmedQuery = query.trim()
                newList.addAll(list.filter { it.name.contains(trimmedQuery, true) })
            }

            Collections.sort(newList, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)
            newList
        }

    fun searchTournamentMatchesList(query: String?, list: List<FullTournament.Match>?) =
        if (list.isNullOrEmpty()) {
            null
        } else {
            val newList = mutableListOf<FullTournament.Match>()

            if (query.isNullOrBlank()) {
                newList.addAll(list)
            } else {
                val trimmedQuery = query.trim()

                newList.addAll(list.filter { it.winnerName.contains(trimmedQuery, true)
                        || it.loserName.contains(trimmedQuery, true) })
            }

            newList
        }

}
