package com.garpr.android.misc

import com.garpr.android.misc.TournamentToolbarManager.Presentation
import com.garpr.android.models.FullTournament

class TournamentToolbarManagerImpl : TournamentToolbarManager {

    override fun getPresentation(tournament: FullTournament?): Presentation {
        return if (tournament == null) {
            Presentation()
        } else {
            Presentation(true,
                    tournament.url?.isNotBlank() == true)
        }
    }

}
