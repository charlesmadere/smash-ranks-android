package com.garpr.android.misc

import com.garpr.android.misc.TournamentToolbarManager.Presentation
import com.garpr.android.models.FullTournament

class TournamentToolbarManagerImpl : TournamentToolbarManager {

    override fun getPresentation(tournament: FullTournament?): Presentation {
        val presentation = Presentation()

        if (tournament == null) {
            return presentation
        }

        presentation.mIsShareVisible = true
        presentation.mIsViewTournamentPageVisible = tournament.url?.isNotBlank() == true

        return presentation
    }

}
