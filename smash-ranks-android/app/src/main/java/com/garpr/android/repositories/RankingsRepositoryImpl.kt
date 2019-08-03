package com.garpr.android.repositories

import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.features.notifications.NotificationsManager
import com.garpr.android.misc.Schedulers
import com.garpr.android.networking.ServerApi
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import io.reactivex.Single

class RankingsRepositoryImpl(
        private val notificationsManager: NotificationsManager,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val regionRepository: RegionRepository,
        private val schedulers: Schedulers,
        private val serverApi: ServerApi
) : RankingsRepository {

    override fun getRankings(region: Region): Single<RankingsBundle> {
        return serverApi.getRankings(region)
                .subscribeOn(schedulers.background)
                .doOnSubscribe {
                    if (region == regionRepository.getRegion()) {
                        notificationsManager.cancelRankingsUpdated()
                    }
                }
                .doOnSuccess {
                    if (region == regionRepository.getRegion()) {
                        rankingsPollingPreferenceStore.rankingsId.set(it.id)
                    }
                }
    }

}
