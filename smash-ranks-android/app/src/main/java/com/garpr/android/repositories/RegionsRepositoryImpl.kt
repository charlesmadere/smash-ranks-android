package com.garpr.android.repositories

import com.garpr.android.data.exceptions.FailedToFetchRegionsException
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.networking.ServerApi
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.Collections

class RegionsRepositoryImpl(
        private val serverApi: ServerApi
) : RegionsRepository {

    override fun getRegions(): Single<RegionsBundle> {
        return Single.zip(
                serverApi.getRegions(Endpoint.GAR_PR),
                serverApi.getRegions(Endpoint.NOT_GAR_PR),
                BiFunction<RegionsBundle, RegionsBundle, RegionsBundle> { t1, t2 ->
                    mergeResponses(garPr = t1, notGarPr = t2)
                })
    }

    @Throws(FailedToFetchRegionsException::class)
    private fun mergeResponses(garPr: RegionsBundle, notGarPr: RegionsBundle): RegionsBundle {
        val regionsSet = mutableSetOf<Region>()
        garPr.regions
                ?.filter { region -> region.isActive }
                ?.mapTo(regionsSet) { region -> Region(region, Endpoint.GAR_PR) }

        notGarPr.regions
                ?.filter { region -> region.isActive }
                ?.mapTo(regionsSet) { region -> Region(region, Endpoint.NOT_GAR_PR) }

        val regionsList = regionsSet.toList()
        Collections.sort(regionsList, AbsRegion.ENDPOINT_ORDER)

        if (regionsList.isEmpty()) {
            throw FailedToFetchRegionsException()
        } else {
            return RegionsBundle(
                    regions = regionsList
            )
        }
    }

}
