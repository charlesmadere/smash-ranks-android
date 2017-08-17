package com.garpr.android.misc

import android.app.Activity
import android.content.Context

import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.AbsTournament

interface ShareUtils {

    fun openUrl(context: Context, url: String?)

    fun sharePlayer(activity: Activity, player: AbsPlayer)

    fun shareRankings(activity: Activity)

    fun shareTournament(activity: Activity, tournament: AbsTournament)

    fun shareTournaments(activity: Activity)

}
