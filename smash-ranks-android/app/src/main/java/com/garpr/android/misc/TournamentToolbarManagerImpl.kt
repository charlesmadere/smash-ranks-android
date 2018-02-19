package com.garpr.android.misc

import com.garpr.android.misc.TournamentToolbarManager.Presentation
import com.garpr.android.models.FullTournament

class TournamentToolbarManagerImpl : TournamentToolbarManager {

    override fun getPresentation(tournament: FullTournament?): Presentation {
        var presentation = Presentation()

        if (tournament == null) {
            return presentation
        }

        presentation = presentation.copy(isShareVisible = true,
                isViewTournamentPageVisible = tournament.url?.isNotBlank() == true)

        return presentation
    }

}
