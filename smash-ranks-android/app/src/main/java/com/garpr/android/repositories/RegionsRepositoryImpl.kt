package com.garpr.android.repositories

import com.garpr.android.data.exceptions.FailedToFetchRegionsException
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.misc.Schedulers
import com.garpr.android.networking.ServerApi2
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.Collections

class RegionsRepositoryImpl(
        private val schedulers: Schedulers,
        private val serverApi: ServerApi2
) : RegionsRepository {

    override fun getRegions(): Single<RegionsBundle> {
        return Single.zip(
                serverApi.getRegions(Endpoint.GAR_PR),
                serverApi.getRegions(Endpoint.NOT_GAR_PR),
                BiFunction<RegionsBundle, RegionsBundle, RegionsBundle> { t1, t2 ->
                    mergeResponses(t1, t2)
                })
                .subscribeOn(schedulers.background)
    }

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
