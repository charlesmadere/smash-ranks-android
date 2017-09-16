package com.garpr.android.misc

import android.support.annotation.UiThread
import com.garpr.android.models.FullTournament

interface FullTournamentUtils {

    fun prepareFullTournament(fullTournament: FullTournament?, callback: Callback)

    interface Callback {
        @UiThread
        fun onComplete(fullTournament: FullTournament?)
    }

}
