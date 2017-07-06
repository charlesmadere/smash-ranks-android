package com.garpr.android.misc

import android.content.Context
import android.text.TextUtils
import com.garpr.android.R
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.AbsTournament
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.FullTournament
import com.garpr.android.models.HeadToHead
import com.garpr.android.models.LiteTournament
import com.garpr.android.models.Match
import com.garpr.android.models.MatchesBundle
import com.garpr.android.models.Ranking
import com.garpr.android.models.Rating
import com.garpr.android.models.TournamentsBundle
import com.garpr.android.models.WinsLosses
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

        if (!headToHead.hasMatches()) {
            list.add(context.getString(R.string.no_matches))
            list.trimToSize()
            return list
        }

        list.addAll(createSortedTournamentAndMatchList(headToHead.matches!!))
        list.trimToSize()

        return list
    }

    fun createPlayerMatchesList(context: Context, regionManager: RegionManager,
            fullPlayer: FullPlayer, bundle: MatchesBundle?): ArrayList<Any>? {
        val region = regionManager.getRegion(context).id
        val rating = fullPlayer.ratings?.getRegion(region)

        if (rating == null && (bundle == null || !bundle.hasMatches())) {
            return null
        }

        val list = ArrayList<Any>()

        if (rating != null) {
            list.add(rating)
        }

        if (bundle == null || !bundle.hasMatches()) {
            if (list.isEmpty()) {
                return null
            } else {
                list.add(context.getString(R.string.no_matches))
                list.trimToSize()
                return list
            }
        }

        list.addAll(createSortedTournamentAndMatchList(bundle.matches!!))
        list.trimToSize()

        return list
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

    fun searchPlayerList(query: String?, list: List<AbsPlayer>?): ArrayList<AbsPlayer>? {
        if (list == null || list.isEmpty()) {
            return null
        }

        val newList = ArrayList<AbsPlayer>(list.size)

        if (query == null || TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list)
            return newList
        }

        val query = query.trim { it <= ' ' }.toLowerCase()

        for (player in list) {
            if (player.name.toLowerCase().contains(query)) {
                newList.add(player)
            }
        }

        return newList
    }

    fun searchPlayerMatchesList(query: String?, list: List<Any>?): ArrayList<Any>? {
        if (list == null || list.isEmpty()) {
            return null
        }

        val newList = ArrayList<Any>(list.size)

        if (query == null || TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list)
            return newList
        }

        val query = query.trim { it <= ' ' }.toLowerCase()
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

                        if (match.opponent.name.toLowerCase().contains(query)) {
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

    fun searchRankingList(query: String?, list: List<Ranking>?): ArrayList<Ranking>? {
        if (list == null || list.isEmpty()) {
            return null
        }

        val newList = ArrayList<Ranking>(list.size)

        if (query == null || TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list)
            return newList
        }

        val query = query.trim { it <= ' ' }.toLowerCase()

        for (ranking in list) {
            if (ranking.name.toLowerCase().contains(query)) {
                newList.add(ranking)
            }
        }

        return newList
    }

    fun searchTournamentList(query: String?, list: List<AbsTournament>?): ArrayList<AbsTournament>? {
        if (list == null || list.isEmpty()) {
            return null
        }

        val newList = ArrayList<AbsTournament>(list.size)

        if (query == null || TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list)
            Collections.sort(newList, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)
            return newList
        }

        val query = query.trim { it <= ' ' }.toLowerCase()

        for (tournament in list) {
            if (tournament.name.toLowerCase().contains(query)) {
                newList.add(tournament)
            }
        }

        Collections.sort(newList, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)
        return newList
    }

    fun searchTournamentMatchesList(query: String?, list: List<FullTournament.Match>?):
            ArrayList<FullTournament.Match>? {
        if (list == null || list.isEmpty()) {
            return null
        }

        val newList = ArrayList<FullTournament.Match>(list.size)

        if (query == null || TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list)
            return newList
        }

        val query = query.trim { it <= ' ' }.toLowerCase()

        for (match in list) {
            if (match.winnerName.toLowerCase().contains(query) ||
                    match.loserName.toLowerCase().contains(query)) {
                newList.add(match)
            }
        }

        return newList
    }

}
