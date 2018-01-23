package com.garpr.android.misc

import com.garpr.android.models.FullTournament

interface TournamentToolbarManager {

    data class Presentation(
            val isShareVisible: Boolean = false,
            val isViewTournamentPageVisible: Boolean = false
    )

    fun getPresentation(tournament: FullTournament?): Presentation

}
