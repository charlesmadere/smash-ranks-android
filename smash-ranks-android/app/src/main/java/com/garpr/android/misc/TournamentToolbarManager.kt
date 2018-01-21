package com.garpr.android.misc

import com.garpr.android.models.FullTournament

interface TournamentToolbarManager {

    class Presentation {
        var isShareVisible: Boolean = false
            internal set

        var isViewTournamentPageVisible: Boolean = false
            internal set
    }

    fun getPresentation(tournament: FullTournament?): Presentation

}
