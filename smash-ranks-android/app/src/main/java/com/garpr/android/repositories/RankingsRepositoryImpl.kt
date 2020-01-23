package com.garpr.android.repositories

import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.managers.NotificationsManager
import com.garpr.android.networking.ServerApi
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import io.reactivex.Single

class RankingsRepositoryImpl(
        private val notificationsManager: NotificationsManager,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val regionRepository: RegionRepository,
        private val serverApi: ServerApi
) : RankingsRepository {

    override fun getRankings(region: Region): Single<RankingsBundle> {
        return serverApi.getRankings(region)
                .doOnSubscribe {
                    if (region == regionRepository.region) {
                        notificationsManager.cancelRankingsUpdated()
                    }
                }
                .doOnSuccess { bundle ->
                    if (region == regionRepository.region) {
                        rankingsPollingPreferenceStore.rankingsId.set(bundle.id)
                    }
                }
    }

}
