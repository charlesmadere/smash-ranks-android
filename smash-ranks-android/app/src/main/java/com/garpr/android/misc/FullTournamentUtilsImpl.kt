package com.garpr.android.misc

import com.garpr.android.misc.FullTournamentUtils.Callback
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FullTournament
import java.util.*

class FullTournamentUtilsImpl(
        private val threadUtils: ThreadUtils
) : FullTournamentUtils {

    override fun prepareFullTournament(fullTournament: FullTournament?, callback: Callback) {
        if (fullTournament?.players?.isNotEmpty() == true) {
            threadUtils.run(object : ThreadUtils.Task {
                private lateinit var newFullTournament: FullTournament

                override fun onBackground() {
                    val players = mutableListOf<AbsPlayer>()
                    players.addAll(fullTournament.players)
                    Collections.sort(players, AbsPlayer.ALPHABETICAL_ORDER)

                    newFullTournament = FullTournament(fullTournament.regions, fullTournament.date,
                            fullTournament.id, fullTournament.name, players, fullTournament.matches,
                            fullTournament.rawId, fullTournament.url)
                }

                override fun onUi() {
                    callback.onComplete(newFullTournament)
                }
            })
        } else {
            callback.onComplete(fullTournament)
        }
    }

}
