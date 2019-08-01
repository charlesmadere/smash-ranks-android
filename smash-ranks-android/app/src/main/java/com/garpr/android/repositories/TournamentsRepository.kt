package com.garpr.android.repositories

import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentsBundle
import io.reactivex.Single

interface TournamentsRepository {

    fun getTournament(region: Region, tournamentId: String): Single<FullTournament>

    fun getTournaments(region: Region): Single<TournamentsBundle>

}
