package com.garpr.android.misc

import com.garpr.android.misc.TournamentToolbarManager.Presentation
import com.garpr.android.models.FullTournament

class TournamentToolbarManagerImpl : TournamentToolbarManager {

    override fun getPresentation(tournament: FullTournament?): Presentation {
        val presentation = Presentation()

        if (tournament == null) {
            return presentation
        }

        presentation.isShareVisible = true
        presentation.isViewTournamentPageVisible = tournament.url?.isNotBlank() == true

        return presentation
    }

}
