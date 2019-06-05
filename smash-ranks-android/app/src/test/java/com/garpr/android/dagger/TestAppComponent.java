package com.garpr.android.dagger;

import com.garpr.android.data.converters.AbsPlayerConverterTest;
import com.garpr.android.data.converters.AbsRegionConverterTest;
import com.garpr.android.data.converters.AbsTournamentConverterTest;
import com.garpr.android.data.converters.MatchConverterTest;
import com.garpr.android.data.converters.RankedPlayerConverterTest;
import com.garpr.android.data.converters.SimpleDateConverterTest;
import com.garpr.android.data.models.AbsTournamentTest;
import com.garpr.android.data.models.AvatarTest;
import com.garpr.android.data.models.RankingsBundleTest;
import com.garpr.android.data.models.RegionsBundleTest;
import com.garpr.android.data.models.SimpleDateTest;
import com.garpr.android.data.models.SmashCharacterTest;
import com.garpr.android.features.deepLink.DeepLinkUtilsTest;
import com.garpr.android.features.home.HomeActivityTest;
import com.garpr.android.features.home.HomeToolbarManagerTest;
import com.garpr.android.features.player.PlayerActivityTest;
import com.garpr.android.features.player.PlayerProfileManagerTest;
import com.garpr.android.features.player.SmashRosterAvatarUrlHelperTest;
import com.garpr.android.features.rankings.PreviousRankUtilsTest;
import com.garpr.android.features.splash.AppUpgradeManagerTest;
import com.garpr.android.features.splash.SplashScreenManagerTest;
import com.garpr.android.features.tournament.TournamentAdapterManagerTest;
import com.garpr.android.features.tournaments.TournamentsActivityTest;
import com.garpr.android.misc.FullTournamentUtilsTest;
import com.garpr.android.misc.ListUtilsTest;
import com.garpr.android.misc.TimberTest;
import com.garpr.android.networking.PlayerMatchesBundleApiCallTest;
import com.garpr.android.networking.RegionsBundleApiCallTest;
import com.garpr.android.preferences.KeyValueStoreProviderTest;
import com.garpr.android.preferences.KeyValueStoreTest;
import com.garpr.android.preferences.persistent.PersistentBooleanPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentIntegerPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentLongPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentMoshiPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentStringPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentUriPreferenceTest;
import com.garpr.android.repositories.FavoritePlayersRepositoryTest;
import com.garpr.android.repositories.IdentityRepositoryTest;
import com.garpr.android.repositories.NightModeRepositoryTest;
import com.garpr.android.repositories.RegionRepositoryTest;
import com.garpr.android.sync.rankings.RankingsNotificationsUtilsTest;
import com.garpr.android.sync.rankings.RankingsPollingManagerTest;
import com.garpr.android.sync.roster.SmashRosterStorageTest;
import com.garpr.android.sync.roster.SmashRosterSyncManagerTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { TestAppModule.class })
public interface TestAppComponent {

    // data/converters
    void inject(AbsPlayerConverterTest test);
    void inject(AbsRegionConverterTest test);
    void inject(AbsTournamentConverterTest test);
    void inject(MatchConverterTest test);
    void inject(RankedPlayerConverterTest test);
    void inject(SimpleDateConverterTest test);

    // data/models
    void inject(AbsTournamentTest test);
    void inject(AvatarTest test);
    void inject(RankingsBundleTest test);
    void inject(RegionsBundleTest test);
    void inject(SimpleDateTest test);
    void inject(SmashCharacterTest test);

    // features/deepLink
    void inject(DeepLinkUtilsTest test);

    // features/home
    void inject(HomeActivityTest test);
    void inject(HomeToolbarManagerTest test);

    // features/player
    void inject(PlayerActivityTest test);
    void inject(PlayerProfileManagerTest test);
    void inject(SmashRosterAvatarUrlHelperTest test);

    // features/splash
    void inject(AppUpgradeManagerTest test);
    void inject(SplashScreenManagerTest test);

    // features/tournaments
    void inject(TournamentAdapterManagerTest test);
    void inject(TournamentsActivityTest test);

    // misc
    void inject(FullTournamentUtilsTest test);
    void inject(ListUtilsTest test);
    void inject(PreviousRankUtilsTest test);
    void inject(TimberTest test);

    // networking
    void inject(PlayerMatchesBundleApiCallTest test);
    void inject(RegionsBundleApiCallTest test);

    // preferences
    void inject(KeyValueStoreProviderTest test);
    void inject(KeyValueStoreTest test);
    void inject(PersistentBooleanPreferenceTest test);
    void inject(PersistentIntegerPreferenceTest test);
    void inject(PersistentLongPreferenceTest test);
    void inject(PersistentMoshiPreferenceTest test);
    void inject(PersistentStringPreferenceTest test);
    void inject(PersistentUriPreferenceTest test);

    // repositories
    void inject(FavoritePlayersRepositoryTest test);
    void inject(IdentityRepositoryTest test);
    void inject(NightModeRepositoryTest test);
    void inject(RegionRepositoryTest test);

    // sync/rankings
    void inject(RankingsNotificationsUtilsTest test);
    void inject(RankingsPollingManagerTest test);

    // sync/roster
    void inject(SmashRosterStorageTest test);
    void inject(SmashRosterSyncManagerTest test);

}
