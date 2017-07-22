package com.garpr.android.misc

import android.content.Context
import com.garpr.android.R
import com.garpr.android.models.*
import java.util.*
import kotlin.collections.ArrayList

object ListUtils {

    fun createHeadToHeadList(context: Context, headToHead: HeadToHead?): ArrayList<Any> {
        val list = ArrayList<Any>()

        if (headToHead == null) {
            list.add(WinsLosses(0, 0))
            list.add(context.getString(R.string.no_matches))
            list.trimToSize()
            return list
        }

        list.add(WinsLosses(headToHead.wins, headToHead.losses))
        val matches = headToHead.matches

        if (matches == null || matches.isEmpty()) {
            list.add(context.getString(R.string.no_matches))
            list.trimToSize()
            return list
        }

        list.addAll(createSortedTournamentAndMatchList(matches))
        list.trimToSize()

        return list
    }

    fun createPlayerMatchesList(context: Context, regionManager: RegionManager,
            fullPlayer: FullPlayer, bundle: MatchesBundle?): ArrayList<Any>? {
        val region = regionManager.getRegion(context).id
        val rating = fullPlayer.ratings?.getRegion(region)
        val matches = bundle?.matches

        if (rating == null && (matches == null || matches.isEmpty())) {
            return null
        }

        val newList = ArrayList<Any>()

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

        newList.trimToSize()
        return newList
    }

    private fun createSortedTournamentAndMatchList(matches: ArrayList<Match>): ArrayList<Any> {
        val matchesCopy = ArrayList(matches)
        Collections.sort(matchesCopy, Match.REVERSE_CHRONOLOGICAL_ORDER)

        val list = ArrayList<Any>()
        var tournamentId: String? = null

        for (match in matchesCopy) {
            if (tournamentId == null || match.tournament.id != tournamentId) {
                tournamentId = match.tournament.id
                list.add(LiteTournament(tournamentId!!, match.tournament.name,
                        match.tournament.date))
            }

            list.add(match)
        }

        return list
    }

    fun createTournamentList(bundle: TournamentsBundle?): ArrayList<AbsTournament>? {
        bundle?.tournaments?.let {
            val tournaments = ArrayList(it)
            Collections.sort(tournaments, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)

            return tournaments
        } ?: return null
    }

    fun filterPlayerMatchesList(result: Match.Result?, list: List<Any>?): ArrayList<Any>? {
        if (list == null || list.isEmpty()) {
            return null
        }

        val newList = ArrayList<Any>(list.size)

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
            val newList: ArrayList<AbsPlayer>

            if (query != null && query.isNotBlank()) {
                val trimmedQuery = query.trim().toLowerCase()

                newList = ArrayList<AbsPlayer>(list.filter {
                    it.name.toLowerCase().contains(trimmedQuery)
                })
            } else {
                newList = ArrayList<AbsPlayer>(list)
            }

            newList
        }

    fun searchPlayerMatchesList(query: String?, list: List<Any>?): ArrayList<Any>? {
        if (list == null || list.isEmpty()) {
            return null
        }

        val newList = ArrayList<Any>(list.size)

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

    fun searchRankingList(query: String?, list: List<Ranking>?) =
        if (list == null || list.isEmpty()) {
            null
        } else if (query != null && query.isNotBlank()) {
            val trimmedQuery = query.trim().toLowerCase()

            ArrayList<Ranking>(list.filter {
                it.name.toLowerCase().contains(trimmedQuery)
            })
        } else {
            ArrayList<Ranking>(list)
        }

    fun searchTournamentList(query: String?, list: List<AbsTournament>?) =
        if (list == null || list.isEmpty()) {
            null
        } else {
            val newList: ArrayList<AbsTournament>

            if (query != null && query.isNotBlank()) {
                val trimmedQuery = query.trim().toLowerCase()

                newList = ArrayList<AbsTournament>(list.filter {
                    it.name.toLowerCase().contains(trimmedQuery)
                })
            } else {
                newList = ArrayList<AbsTournament>(list)
            }

            Collections.sort(newList, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)
            newList
        }

    fun searchTournamentMatchesList(query: String?, list: List<FullTournament.Match>?) =
        if (list == null || list.isEmpty()) {
            null
        } else if (query != null && query.isNotBlank()) {
            val trimmedQuery = query.trim().toLowerCase()

            ArrayList<FullTournament.Match>(list.filter {
                it.winnerName.toLowerCase().contains(trimmedQuery) ||
                        it.loserName.toLowerCase().contains(trimmedQuery)
            })
        } else {
            ArrayList<FullTournament.Match>(list)
        }

}
