package com.garpr.android.sync.roster

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor

interface SmashRosterStorage {

    @WorkerThread
    fun deleteFromStorage(endpoint: Endpoint)

    @AnyThread
    fun getSmashCompetitor(endpoint: Endpoint, playerId: String?): SmashCompetitor?

    @AnyThread
    fun getSmashCompetitor(region: Region, playerId: String?): SmashCompetitor?

    @WorkerThread
    fun writeToStorage(endpoint: Endpoint, smashRoster: Map<String, SmashCompetitor>?)

}
