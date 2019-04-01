package com.garpr.android.dagger;

import com.garpr.android.activities.HomeActivityTest;
import com.garpr.android.activities.PlayerActivityTest;
import com.garpr.android.activities.TournamentsActivityTest;
import com.garpr.android.data.converters.AbsPlayerConverterTest;
import com.garpr.android.data.converters.AbsRegionConverterTest;
import com.garpr.android.data.converters.AbsTournamentConverterTest;
import com.garpr.android.data.converters.MatchConverterTest;
import com.garpr.android.data.converters.RankedPlayerConverterTest;
import com.garpr.android.data.converters.SimpleDateConverterTest;
import com.garpr.android.data.models.AbsTournamentTest;
import com.garpr.android.data.models.AvatarTest;
import com.garpr.android.data.models.MatchTest;
import com.garpr.android.data.models.RankingsBundleTest;
import com.garpr.android.data.models.RegionsBundleTest;
import com.garpr.android.data.models.SimpleDateTest;
import com.garpr.android.data.models.SmashCharacterTest;
import com.garpr.android.managers.AppUpgradeManagerTest;
import com.garpr.android.managers.FavoritePlayersManagerTest;
import com.garpr.android.managers.HomeToolbarManagerTest;
import com.garpr.android.managers.IdentityManagerTest;
import com.garpr.android.managers.NightModeManagerTest;
import com.garpr.android.managers.PlayerProfileManagerTest;
import com.garpr.android.managers.RegionManagerTest;
import com.garpr.android.managers.SmashRosterAvatarUrlHelperTest;
import com.garpr.android.managers.SplashScreenManagerTest;
import com.garpr.android.managers.TournamentAdapterManagerTest;
import com.garpr.android.misc.DeepLinkUtilsTest;
import com.garpr.android.misc.FullTournamentUtilsTest;
import com.garpr.android.misc.ListUtilsTest;
import com.garpr.android.misc.PreviousRankUtilsTest;
import com.garpr.android.misc.RankingsNotificationsUtilsTest;
import com.garpr.android.misc.SmashRosterStorageTest;
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
import com.garpr.android.sync.rankings.RankingsPollingManagerTest;
import com.garpr.android.sync.roster.SmashRosterSyncManagerTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { TestAppModule.class })
public interface TestAppComponent {

    // activities
    void inject(HomeActivityTest test);
    void inject(PlayerActivityTest test);
    void inject(TournamentsActivityTest test);

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
    void inject(MatchTest test);
    void inject(RankingsBundleTest test);
    void inject(RegionsBundleTest test);
    void inject(SimpleDateTest test);
    void inject(SmashCharacterTest test);

    // managers
    void inject(AppUpgradeManagerTest test);
    void inject(FavoritePlayersManagerTest test);
    void inject(HomeToolbarManagerTest test);
    void inject(IdentityManagerTest test);
    void inject(NightModeManagerTest test);
    void inject(PlayerProfileManagerTest test);
    void inject(RegionManagerTest test);
    void inject(SmashRosterAvatarUrlHelperTest test);
    void inject(SplashScreenManagerTest test);
    void inject(TournamentAdapterManagerTest test);

    // misc
    void inject(DeepLinkUtilsTest test);
    void inject(FullTournamentUtilsTest test);
    void inject(ListUtilsTest test);
    void inject(PreviousRankUtilsTest test);
    void inject(RankingsNotificationsUtilsTest test);
    void inject(SmashRosterStorageTest test);
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

    // sync
    void inject(RankingsPollingManagerTest test);
    void inject(SmashRosterSyncManagerTest test);

}
