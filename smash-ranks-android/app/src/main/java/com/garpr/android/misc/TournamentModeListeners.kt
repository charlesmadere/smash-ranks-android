package com.garpr.android.misc

import android.view.View
import com.garpr.android.models.TournamentMode

interface TournamentModeListeners {

    fun onTournamentModeClick(v: View, tournamentMode: TournamentMode)

    val tournamentMode: TournamentMode

}
