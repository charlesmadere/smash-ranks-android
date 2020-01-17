package com.garpr.android.repositories

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.networking.ServerApi
import io.reactivex.Single

class SmashRosterRepositoryImpl(
        private val serverApi: ServerApi
) : SmashRosterRepository {

    override fun getSmashRoster(endpoint: Endpoint): Single<Map<String, SmashCompetitor>> {
        return serverApi.getSmashRoster(endpoint)
    }

}
