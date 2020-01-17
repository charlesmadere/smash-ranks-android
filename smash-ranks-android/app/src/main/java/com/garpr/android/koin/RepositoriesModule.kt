package com.garpr.android.koin

import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.FavoritePlayersRepositoryImpl
import com.garpr.android.repositories.HeadToHeadRepository
import com.garpr.android.repositories.HeadToHeadRepositoryImpl
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.IdentityRepositoryImpl
import com.garpr.android.repositories.MatchesRepository
import com.garpr.android.repositories.MatchesRepositoryImpl
import com.garpr.android.repositories.NightModeRepository
import com.garpr.android.repositories.NightModeRepositoryImpl
import com.garpr.android.repositories.PlayerMatchesRepository
import com.garpr.android.repositories.PlayerMatchesRepositoryImpl
import com.garpr.android.repositories.PlayersRepository
import com.garpr.android.repositories.PlayersRepositoryImpl
import com.garpr.android.repositories.RankingsRepository
import com.garpr.android.repositories.RankingsRepositoryImpl
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.repositories.RegionRepositoryImpl
import com.garpr.android.repositories.RegionsRepository
import com.garpr.android.repositories.RegionsRepositoryImpl
import com.garpr.android.repositories.SmashRosterRepository
import com.garpr.android.repositories.SmashRosterRepositoryImpl
import com.garpr.android.repositories.TournamentsRepository
import com.garpr.android.repositories.TournamentsRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoriesModule = module {

    single<FavoritePlayersRepository> {
        val keyValueStore: KeyValueStore = get(named(FAVORITE_PLAYERS_KEY_VALUE_STORE))
        FavoritePlayersRepositoryImpl(get(), keyValueStore, get(), get(), get(), get())
    }

    single<HeadToHeadRepository> { HeadToHeadRepositoryImpl(get()) }
    single<IdentityRepository> { IdentityRepositoryImpl(get(), get(), get(), get()) }
    single<MatchesRepository> { MatchesRepositoryImpl(get()) }
    single<NightModeRepository> { NightModeRepositoryImpl(get(), get(), get()) }
    single<PlayerMatchesRepository> { PlayerMatchesRepositoryImpl(get(), get()) }
    single<PlayersRepository> { PlayersRepositoryImpl(get()) }
    single<RankingsRepository> { RankingsRepositoryImpl(get(), get(), get(), get()) }
    single<RegionRepository> { RegionRepositoryImpl(get(), get(), get(), get()) }
    single<RegionsRepository> { RegionsRepositoryImpl(get()) }
    single<SmashRosterRepository> { SmashRosterRepositoryImpl(get()) }
    single<TournamentsRepository> { TournamentsRepositoryImpl(get()) }

}
