package com.garpr.android.dagger

import com.garpr.android.data.converters.AbsPlayerConverterTest
import com.garpr.android.data.converters.AbsRegionConverterTest
import com.garpr.android.data.converters.AbsTournamentConverterTest
import com.garpr.android.data.converters.MatchConverterTest
import com.garpr.android.data.converters.RankedPlayerConverterTest
import com.garpr.android.data.converters.SimpleDateConverterTest
import com.garpr.android.data.models.AbsTournamentTest
import com.garpr.android.data.models.AvatarTest
import com.garpr.android.data.models.RankingsBundleTest
import com.garpr.android.data.models.RegionsBundleTest
import com.garpr.android.data.models.SimpleDateTest
import com.garpr.android.data.models.SmashCharacterTest
import com.garpr.android.features.deepLink.DeepLinkViewModelTest
import com.garpr.android.features.home.HomeActivityTest
import com.garpr.android.features.home.HomeToolbarManagerTest
import com.garpr.android.features.player.PlayerActivityTest
import com.garpr.android.features.player.PlayerProfileManagerTest
import com.garpr.android.features.player.SmashRosterAvatarUrlHelperTest
import com.garpr.android.features.rankings.PreviousRankUtilsTest
import com.garpr.android.features.splash.AppUpgradeManagerTest
import com.garpr.android.features.splash.SplashScreenManagerTest
import com.garpr.android.features.tournaments.TournamentsActivityTest
import com.garpr.android.misc.TimberTest
import com.garpr.android.preferences.KeyValueStoreProviderTest
import com.garpr.android.preferences.KeyValueStoreTest
import com.garpr.android.preferences.persistent.PersistentBooleanPreferenceTest
import com.garpr.android.preferences.persistent.PersistentIntegerPreferenceTest
import com.garpr.android.preferences.persistent.PersistentLongPreferenceTest
import com.garpr.android.preferences.persistent.PersistentMoshiPreferenceTest
import com.garpr.android.preferences.persistent.PersistentStringPreferenceTest
import com.garpr.android.preferences.persistent.PersistentUriPreferenceTest
import com.garpr.android.repositories.FavoritePlayersRepositoryTest
import com.garpr.android.repositories.IdentityRepositoryTest
import com.garpr.android.repositories.NightModeRepositoryTest
import com.garpr.android.repositories.RegionRepositoryTest
import com.garpr.android.sync.rankings.RankingsNotificationsUtilsTest
import com.garpr.android.sync.rankings.RankingsPollingManagerTest
import com.garpr.android.sync.roster.SmashRosterStorageTest
import com.garpr.android.sync.roster.SmashRosterSyncManagerTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, TestConfigModule::class])
interface TestAppComponent {

    // data/converters
    fun inject(test: AbsPlayerConverterTest)
    fun inject(test: AbsRegionConverterTest)
    fun inject(test: AbsTournamentConverterTest)
    fun inject(test: MatchConverterTest)
    fun inject(test: RankedPlayerConverterTest)
    fun inject(test: SimpleDateConverterTest)

    // data/models
    fun inject(test: AbsTournamentTest)
    fun inject(test: AvatarTest)
    fun inject(test: RankingsBundleTest)
    fun inject(test: RegionsBundleTest)
    fun inject(test: SimpleDateTest)
    fun inject(test: SmashCharacterTest)

    // features/deepLink
    fun inject(test: DeepLinkViewModelTest)

    // features/home
    fun inject(test: HomeActivityTest)
    fun inject(test: HomeToolbarManagerTest)

    // features/player
    fun inject(test: PlayerActivityTest)
    fun inject(test: PlayerProfileManagerTest)
    fun inject(test: SmashRosterAvatarUrlHelperTest)

    // features/splash
    fun inject(test: AppUpgradeManagerTest)
    fun inject(test: SplashScreenManagerTest)

    // features/tournaments
    fun inject(test: TournamentsActivityTest)

    // misc
    fun inject(test: PreviousRankUtilsTest)
    fun inject(test: TimberTest)

    // preferences
    fun inject(test: KeyValueStoreProviderTest)
    fun inject(test: KeyValueStoreTest)
    fun inject(test: PersistentBooleanPreferenceTest)
    fun inject(test: PersistentIntegerPreferenceTest)
    fun inject(test: PersistentLongPreferenceTest)
    fun inject(test: PersistentMoshiPreferenceTest)
    fun inject(test: PersistentStringPreferenceTest)
    fun inject(test: PersistentUriPreferenceTest)

    // repositories
    fun inject(test: FavoritePlayersRepositoryTest)
    fun inject(test: IdentityRepositoryTest)
    fun inject(test: NightModeRepositoryTest)
    fun inject(test: RegionRepositoryTest)

    // sync/rankings
    fun inject(test: RankingsNotificationsUtilsTest)
    fun inject(test: RankingsPollingManagerTest)

    // sync/roster
    fun inject(test: SmashRosterStorageTest)
    fun inject(test: SmashRosterSyncManagerTest)

}
