package com.garpr.android.repositories

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.SmashCompetitor
import io.reactivex.Single

interface SmashRosterRepository {

    fun getSmashRoster(endpoint: Endpoint): Single<Map<String, SmashCompetitor>>

}
