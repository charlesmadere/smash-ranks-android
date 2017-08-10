package com.garpr.android.misc

import com.garpr.android.models.FullTournament

interface TournamentToolbarManager {

    class Presentation {
        var mIsShareVisible: Boolean = false
            internal set

        var mIsViewTournamentPageVisible: Boolean = false
            internal set
    }

    fun getPresentation(tournament: FullTournament?): Presentation

}
