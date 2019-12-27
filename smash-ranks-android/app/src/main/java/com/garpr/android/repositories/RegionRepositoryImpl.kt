package com.garpr.android.repositories

import android.content.Context
import android.content.ContextWrapper
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

    override fun getRegion(context: Context?): Region {
        val region = (context as? RegionRepository.RegionHandle)?.currentRegion

        if (region != null) {
            return region
        } else if (context is ContextWrapper) {
            return getRegion(context.baseContext)
        }

        return checkNotNull(generalPreferenceStore.currentRegion.get()) {
            "The user's current region preference is null, this is impossible."
        }
    }

    override fun setRegion(region: Region) {
        timber.d(TAG, "old region is \"${generalPreferenceStore.currentRegion.get()}\", "
                + "new region is \"$region\"")
        rankingsPollingPreferenceStore.lastPoll.delete()
        rankingsPollingPreferenceStore.rankingsId.delete()
        generalPreferenceStore.currentRegion.set(region)
    }

}
