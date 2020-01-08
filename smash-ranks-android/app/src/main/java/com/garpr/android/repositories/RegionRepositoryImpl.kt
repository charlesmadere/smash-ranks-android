package com.garpr.android.repositories

import com.garpr.android.data.models.Region
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import io.reactivex.Observable

class RegionRepositoryImpl(
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val timber: Timber
) : RegionRepository {

    override var region: Region
        get() {
            return checkNotNull(generalPreferenceStore.currentRegion.get()) {
                "The user's currentRegion preference is null, this should be impossible."
            }
        }
        set(value) {
            timber.d(TAG, "region was \"$region\", is now being changed to \"$value\"")
            rankingsPollingPreferenceStore.lastPoll.delete()
            rankingsPollingPreferenceStore.rankingsId.delete()
            generalPreferenceStore.currentRegion.set(value)
        }

    override val observable: Observable<Region> = generalPreferenceStore.currentRegion
            .observable
            .map {
                checkNotNull(it.item) {
                    "A null region here is impossible and means we have a bug somewhere else."
                }
            }

    companion object {
        private const val TAG = "RegionRepositoryImpl"
    }

}
