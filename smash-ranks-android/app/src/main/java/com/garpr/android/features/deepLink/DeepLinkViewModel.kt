package com.garpr.android.features.deepLink

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.features.home.HomeTab
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.repositories.RegionsRepository
import okhttp3.internal.toImmutableList
import java.util.Collections

class DeepLinkViewModel(
        private val regionRepository: RegionRepository,
        private val regionsRepository: RegionsRepository,
        private val schedulers: Schedulers,
        private val timber: Timber
) : BaseViewModel() {

    private var initialized: Boolean = false
    private var url: String? = null

    private val _breadcrumbsLiveData = MutableLiveData<List<Breadcrumb>>()
    val breadcrumbsLiveData: LiveData<List<Breadcrumb>> = _breadcrumbsLiveData

    private val _networkErrorLiveData = MutableLiveData<Unit>()
    val networkErrorLiveData: LiveData<Unit> = _networkErrorLiveData

    private val _urlParseErrorLiveData = MutableLiveData<Unit>()
    val urlParseErrorLiveData: LiveData<Unit> = _urlParseErrorLiveData

    companion object {
        private const val TAG = "DeepLinkViewModel"

        private const val PLAYERS = "players"
        private const val RANKINGS = "rankings"
        private const val TOURNAMENTS = "tournaments"

        // Players
        // https://www.garpr.com/#/norcal/players
        // https://www.notgarpr.com/#/newjersey/players

        // Rankings
        // https://www.garpr.com/#/norcal/rankings
        // https://www.notgarpr.com/#/nyc/rankings

        // apollo iii
        // https://www.notgarpr.com/#/nyc/tournaments/58c72c801d41c8259fa1f8bf

        // Norcal Validated 2
        // https://www.garpr.com/#/norcal/tournaments/58a00514d2994e4d0f2e25a6

        // rubicon 12
        // https://www.notgarpr.com/#/chicago/tournaments/579839b0e592573cf1845f46

        // SFAT
        // https://www.garpr.com/#/norcal/players/588852e8d2994e3bbfa52d88

        // Swedish Delight
        // https://www.notgarpr.com/#/nyc/players/545b240b8ab65f7a95f74940
    }

    @WorkerThread
    private fun createPlayersBreadcrumbs(breadcrumbs: MutableList<Breadcrumb>, region: Region,
            sameRegion: Boolean, splits: List<String>) {
        breadcrumbs.add(Breadcrumb.Home())

        breadcrumbs.add(Breadcrumb.Players(
                region = if (sameRegion) null else region
        ))

        if (splits.size < 3) {
            return
        }

        val playerId = splits[2]

        if (playerId.isBlank()) {
            return
        }

        breadcrumbs.add(Breadcrumb.Player(
                region = if (sameRegion) null else region,
                playerId = playerId
        ))
    }

    @WorkerThread
    private fun createRankingsBreadcrumbs(breadcrumbs: MutableList<Breadcrumb>, region: Region,
            sameRegion: Boolean) {
        if (sameRegion) {
            breadcrumbs.add(Breadcrumb.Home(initialPosition = HomeTab.RANKINGS))
        } else {
            breadcrumbs.add(Breadcrumb.Home())
            breadcrumbs.add(Breadcrumb.Rankings(region = region))
        }
    }

    @WorkerThread
    private fun createTournamentsBreadcrumbs(breadcrumbs: MutableList<Breadcrumb>, region: Region,
            sameRegion: Boolean, splits: List<String>) {
        if (sameRegion) {
            breadcrumbs.add(Breadcrumb.Home(initialPosition = HomeTab.TOURNAMENTS))
        } else {
            breadcrumbs.add(Breadcrumb.Home())
            breadcrumbs.add(Breadcrumb.Tournaments(region = region))
        }

        if (splits.size < 3) {
            return
        }

        val tournamentId = splits[2]

        if (tournamentId.isBlank()) {
            return
        }

        breadcrumbs.add(Breadcrumb.Tournament(
                region = if (sameRegion) null else region,
                tournamentId = tournamentId
        ))
    }

    @WorkerThread
    private fun createBreadcrumbs(regions: List<Region>): List<Breadcrumb> {
        val currentRegion = regionRepository.region
        val url = this.url

        if (url.isNullOrBlank()) {
            return emptyList()
        }

        require(regions.isNotEmpty()) { "regions can't be empty" }

        val endpoint = Endpoint.values()
                .firstOrNull { endpoint -> url.startsWith(endpoint.basePath) }
                ?: return emptyList()

        val path = url.substring(endpoint.getWebPath().length, url.length)

        if (path.isBlank()) {
            return emptyList()
        }

        val splits = path.split("/")

        if (splits.isEmpty()) {
            return emptyList()
        }

        val regionId = splits[0]

        if (regionId.isBlank()) {
            return emptyList()
        }

        val sameRegion = regionId.equals(currentRegion.id, ignoreCase = true)
        val region = regions.find { it.id.equals(regionId, ignoreCase = true) }

        if (sameRegion && splits.size == 1) {
            return emptyList()
        } else if (region == null) {
            timber.w(TAG, "unable to find a region with ID \"$regionId\".")
            return emptyList()
        }

        val breadcrumbs = mutableListOf<Breadcrumb>()
        val page = splits[1]

        if (PLAYERS.equals(page, ignoreCase = true)) {
            createPlayersBreadcrumbs(breadcrumbs, region, sameRegion, splits)
        } else if (RANKINGS.equals(page, ignoreCase = true)) {
            createRankingsBreadcrumbs(breadcrumbs, region, sameRegion)
        } else if (TOURNAMENTS.equals(page, ignoreCase = true)) {
            createTournamentsBreadcrumbs(breadcrumbs, region, sameRegion, splits)
        }

        return Collections.unmodifiableList(breadcrumbs)
    }

    fun fetchBreadcrumbs() {
        check(initialized) { "initialize() hasn't been called!" }

        if (url.isNullOrBlank()) {
            _urlParseErrorLiveData.postValue(Unit)
            return
        }

        disposables.add(regionsRepository.getRegions()
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({
                    val regions = it.regions
                            ?.filterIsInstance<Region>()
                            ?.toImmutableList()

                    if (regions.isNullOrEmpty()) {
                        _networkErrorLiveData.postValue(Unit)
                    } else {
                        val breadcrumbs = createBreadcrumbs(regions)
                        _breadcrumbsLiveData.postValue(breadcrumbs)
                    }
                }, {
                    timber.e(TAG, "Error fetching regions", it)
                    _networkErrorLiveData.postValue(Unit)
                }))
    }

    fun initialize(url: String?) {
        this.url = url?.trim()
        initialized = true
    }

    sealed class Breadcrumb {

        class Home(
                val initialPosition: HomeTab? = null
        ) : Breadcrumb()

        class Player(
                val region: Region? = null,
                val playerId: String
        ) : Breadcrumb()

        class Players(
                val region: Region? = null
        ) : Breadcrumb()

        class Rankings(
                val region: Region? = null
        ) : Breadcrumb()

        class Tournament(
                val region: Region? = null,
                val tournamentId: String
        ) : Breadcrumb()

        class Tournaments(
                val region: Region? = null
        ) : Breadcrumb()

    }

}
