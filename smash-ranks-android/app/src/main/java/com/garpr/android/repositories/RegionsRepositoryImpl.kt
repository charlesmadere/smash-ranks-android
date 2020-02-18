package com.garpr.android.repositories

import com.garpr.android.data.exceptions.FailedToFetchRegionsException
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.misc.Timber
import com.garpr.android.networking.ServerApi
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.Collections

class RegionsRepositoryImpl(
        private val serverApi: ServerApi,
        private val timber: Timber
) : RegionsRepository {

    override fun getRegions(): Single<RegionsBundle> {
        return Single.zip(
                getRegions(Endpoint.GAR_PR),
                getRegions(Endpoint.NOT_GAR_PR),
                BiFunction<Optional<RegionsBundle>, Optional<RegionsBundle>, RegionsBundle> { garPr, notGarPr ->
                    mergeResponses(garPr.orNull(), notGarPr.orNull())
                })
    }

    private fun getRegions(endpoint: Endpoint): Single<Optional<RegionsBundle>> {
        return serverApi.getRegions(endpoint)
                .map { bundle ->
                    Optional.of(bundle)
                }
                .onErrorReturn { throwable ->
                    timber.e(TAG, "Error fetching \"$endpoint\" regions", throwable)
                    Optional.empty()
                }
    }

    @Throws(FailedToFetchRegionsException::class)
    private fun mergeResponses(garPr: RegionsBundle?, notGarPr: RegionsBundle?): RegionsBundle {
        val regionsSet = mutableSetOf<Region>()

        garPr?.regions?.mapTo(regionsSet) { region ->
            Region(region = region, endpoint = Endpoint.GAR_PR)
        }

        notGarPr?.regions?.mapTo(regionsSet) { region ->
            Region(region = region, endpoint = Endpoint.NOT_GAR_PR)
        }

        val regionsList = regionsSet.toList()
        Collections.sort(regionsList, AbsRegion.ENDPOINT_ORDER)

        if (regionsList.isEmpty()) {
            timber.e(TAG, "Failed to fetch any regions")
            throw FailedToFetchRegionsException()
        }

        return RegionsBundle(regions = regionsList)
    }

    companion object {
        private const val TAG = "RegionsRepositoryImpl"
    }

}
