package com.garpr.android.misc

import android.support.annotation.AnyThread
import android.support.annotation.WorkerThread
import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region
import com.garpr.android.models.SmashCompetitor
import com.garpr.android.models.SmashRoster

interface SmashRosterStorage {

    @WorkerThread
    fun deleteFromStorage(endpoint: Endpoint)

    @AnyThread
    fun getSmashCompetitor(endpoint: Endpoint, playerId: String?): SmashCompetitor?

    @AnyThread
    fun getSmashCompetitor(region: Region, playerId: String?): SmashCompetitor?

    @WorkerThread
    fun writeToStorage(endpoint: Endpoint, smashRoster: SmashRoster?)

}
