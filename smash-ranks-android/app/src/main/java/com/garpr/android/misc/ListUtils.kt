package com.garpr.android.misc

import android.content.Context
import android.util.Log
import com.garpr.android.R
import com.garpr.android.models.*
import java.util.*

object ListUtils {

    fun createHeadToHeadList(context: Context, headToHead: HeadToHead?): MutableList<Any> {
        val list = mutableListOf<Any>()

        if (headToHead == null) {
            list.add(WinsLosses(0, 0))
            list.add(context.getString(R.string.no_matches))
            return list
        }

        list.add(WinsLosses(headToHead.wins, headToHead.losses))
        val matches = headToHead.matches

        if (matches == null || matches.isEmpty()) {
            list.add(context.getString(R.string.no_matches))
            return list
        }

        list.addAll(createSortedTournamentAndMatchList(matches))
        return list
    }

    fun createPlayerMatchesList(context: Context, regionManager: RegionManager,
            fullPlayer: FullPlayer, bundle: MatchesBundle?): MutableList<Any>? {
        val region = regionManager.getRegion(context)
        Log.d("blah", "" + fullPlayer.ratings)
        val rating = fullPlayer.ratings?.get(region.id)
        Log.d("blah", "" + rating)
        val matches = bundle?.matches

        if (rating == null && (matches == null || matches.isEmpty())) {
            return null
        }

        val newList = mutableListOf<Any>()

        if (rating != null) {
            newList.add(rating)
        }

        if (matches == null || matches.isEmpty()) {
            if (newList.isEmpty()) {
                return null
            } else {
                newList.add(context.getString(R.string.no_matches))
            }
        } else {
            newList.addAll(createSortedTournamentAndMatchList(matches))
        }

        return newList
    }

    private fun createSortedTournamentAndMatchList(matches: List<Match>): MutableList<Any> {
        val matchesCopy = mutableListOf<Match>()
        matchesCopy.addAll(matches)
        Collections.sort(matchesCopy, Match.REVERSE_CHRONOLOGICAL_ORDER)

        val list = mutableListOf<Any>()
        var tournamentId: String? = null

        for (match in matchesCopy) {
            if (tournamentId == null || match.tournament.id != tournamentId) {
                tournamentId = match.tournament.id
                list.add(LiteTournament(null, match.tournament.date, tournamentId,
                        match.tournament.name))
            }

            list.add(match)
        }

        return list
    }

    fun createTournamentList(bundle: TournamentsBundle?): List<AbsTournament>? {
        val tournaments = bundle?.tournaments

        if (tournaments?.isNotEmpty() == true) {
            val tournamentsCopy = mutableListOf<AbsTournament>()
            tournamentsCopy.addAll(tournaments)
            Collections.sort(tournamentsCopy, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)
            return tournamentsCopy
        } else {
            return null
        }
    }

    fun filterPlayerMatchesList(result: Match.Result?, list: List<Any>?): MutableList<Any>? {
        if (list == null || list.isEmpty()) {
            return null
        }

        val newList = mutableListOf<Any>()

        if (result == null) {
            newList.addAll(list)
            return newList
        }

        var addedCharSequence = false
        var addedRating = false
        var addedWinsLosses = false

        for (i in list.indices) {
            val objectI = list[i]

            if (objectI is CharSequence) {
                if (!addedCharSequence) {
                    addedCharSequence = true
                    newList.add(objectI)
                }
            } else if (objectI is Rating) {
                if (!addedRating) {
                    addedRating = true
                    newList.add(objectI)
                }
            } else if (objectI is WinsLosses) {
                if (!addedWinsLosses) {
                    addedWinsLosses = true
                    newList.add(objectI)
                }
            } else if (objectI is AbsTournament) {
                var addedTournament = false
                var j = i + 1

                while (j < list.size) {
                    val objectJ = list[j]

                    if (objectJ is Match) {
                        val match = objectJ

                        if (match.result == result) {
                            if (!addedTournament) {
                                addedTournament = true
                                newList.add(objectI)
                            }

                            newList.add(match)
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

    fun searchPlayerList(query: String?, list: List<AbsPlayer>?) =
        if (list == null || list.isEmpty()) {
            null
        } else {
            val newList = mutableListOf<AbsPlayer>()

            if (query?.isNotBlank() == true) {
                val trimmedQuery = query.trim().toLowerCase()

                newList.addAll(list.filter {
                    it.name.toLowerCase().contains(trimmedQuery)
                })
            } else {
                newList.addAll(list)
            }

            newList
        }

    fun searchPlayerMatchesList(query: String?, list: List<Any>?): MutableList<Any>? {
        if (list == null || list.isEmpty()) {
            return null
        }

        val newList = mutableListOf<Any>()

        if (query == null || query.isBlank()) {
            newList.addAll(list)
            return newList
        }

        val trimmedQuery = query.trim().toLowerCase()
        var addedRating = false

        for (i in list.indices) {
            val objectI = list[i]

            if (objectI is Rating) {
                if (!addedRating) {
                    addedRating = true
                    newList.add(objectI)
                }
            } else if (objectI is AbsTournament) {
                var addedTournament = false
                var j = i + 1

                while (j < list.size) {
                    val objectJ = list[j]

                    if (objectJ is Match) {
                        val match = objectJ

                        if (match.opponent.name.toLowerCase().contains(trimmedQuery)) {
                            if (!addedTournament) {
                                addedTournament = true
                                newList.add(objectI)
                            }

                            newList.add(match)
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
        if (list == null || list.isEmpty()) {
            null
        } else {
            val newList = mutableListOf<RankedPlayer>()

            if (query?.isNotBlank() == true) {
                val trimmedQuery = query.trim().toLowerCase()
                newList.addAll(list.filter { it.name.toLowerCase().contains(trimmedQuery) })
            } else {
                newList.addAll(list)
            }

            newList
        }

    fun searchTournamentList(query: String?, list: List<AbsTournament>?) =
        if (list == null || list.isEmpty()) {
            null
        } else {
            val newList = mutableListOf<AbsTournament>()

            if (query?.isNotBlank() == true) {
                val trimmedQuery = query.trim().toLowerCase()
                newList.addAll(list.filter { it.name.toLowerCase().contains(trimmedQuery) })
            } else {
                newList.addAll(list)
            }

            Collections.sort(newList, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)
            newList
        }

    fun searchTournamentMatchesList(query: String?, list: List<FullTournament.Match>?) =
        if (list == null || list.isEmpty()) {
            null
        } else {
            val newList = mutableListOf<FullTournament.Match>()

            if (query?.isNotBlank() == true) {
                val trimmedQuery = query.trim().toLowerCase()

                newList.addAll(list.filter { it.winnerName.toLowerCase().contains(trimmedQuery)
                        || it.loserName.toLowerCase().contains(trimmedQuery) })
            } else {
                newList.addAll(list)
            }

            newList
        }

}
