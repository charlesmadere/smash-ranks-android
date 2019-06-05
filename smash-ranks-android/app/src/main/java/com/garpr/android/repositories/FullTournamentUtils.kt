package com.garpr.android.repositories

import androidx.annotation.UiThread
import com.garpr.android.data.models.FullTournament

interface FullTournamentUtils {

    fun prepareFullTournament(fullTournament: FullTournament?, callback: Callback)

    interface Callback {
        @UiThread
        fun onComplete(fullTournament: FullTournament?)
    }

}
