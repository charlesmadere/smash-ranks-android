package com.garpr.android.misc

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.misc.FullTournamentUtils.Callback
import java.util.Collections

class FullTournamentUtilsImpl(
        private val threadUtils: ThreadUtils
) : FullTournamentUtils {

    override fun prepareFullTournament(fullTournament: FullTournament?, callback: Callback) {
        if (fullTournament?.players == null || fullTournament.players.isEmpty()) {
            callback.onComplete(fullTournament)
            return
        }

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
    }

}
