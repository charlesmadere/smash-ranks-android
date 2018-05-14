package com.garpr.android.dagger;

import com.garpr.android.managers.AppUpgradeManagerTest;
import com.garpr.android.managers.FavoritePlayersManagerTest;
import com.garpr.android.managers.HomeToolbarManagerTest;
import com.garpr.android.managers.IdentityManagerTest;
import com.garpr.android.managers.PlayerProfileManagerTest;
import com.garpr.android.managers.PlayerToolbarManagerTest;
import com.garpr.android.managers.RegionManagerTest;
import com.garpr.android.managers.TournamentAdapterManagerTest;
import com.garpr.android.misc.DeepLinkUtilsTest;
import com.garpr.android.misc.FullTournamentUtilsTest;
import com.garpr.android.misc.ListUtilsTest;
import com.garpr.android.misc.PreviousRankUtilsTest;
import com.garpr.android.misc.RankingsNotificationsUtilsTest;
import com.garpr.android.misc.SmashRosterStorageTest;
import com.garpr.android.misc.TimberTest;
import com.garpr.android.models.AbsPlayerTest;
import com.garpr.android.models.AbsRegionTest;
import com.garpr.android.models.AbsTournamentTest;
import com.garpr.android.models.AvatarTest;
import com.garpr.android.models.MatchTest;
import com.garpr.android.models.RankingCriteriaTest;
import com.garpr.android.models.RankingsBundleTest;
import com.garpr.android.models.RegionsBundleTest;
import com.garpr.android.models.SimpleDateTest;
import com.garpr.android.models.SmashCharacterTest;
import com.garpr.android.networking.PlayerMatchesBundleApiCallTest;
import com.garpr.android.networking.RegionsBundleApiCallTest;
import com.garpr.android.preferences.KeyValueStoreProviderTest;
import com.garpr.android.preferences.KeyValueStoreTest;
import com.garpr.android.preferences.persistent.PersistentBooleanPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentGsonPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentIntegerPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentLongPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentStringPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentUriPreferenceTest;
import com.garpr.android.sync.RankingsPollingSyncManagerTest;
import com.garpr.android.sync.SmashRosterSyncManagerTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { TestAppModule.class })
public interface TestAppComponent {

    // managers
    void inject(AppUpgradeManagerTest test);
    void inject(FavoritePlayersManagerTest test);
    void inject(HomeToolbarManagerTest test);
    void inject(IdentityManagerTest test);
    void inject(PlayerProfileManagerTest test);
    void inject(PlayerToolbarManagerTest test);
    void inject(RegionManagerTest test);
    void inject(TournamentAdapterManagerTest test);

    // misc
    void inject(DeepLinkUtilsTest test);
    void inject(FullTournamentUtilsTest test);
    void inject(ListUtilsTest test);
    void inject(PreviousRankUtilsTest test);
    void inject(RankingsNotificationsUtilsTest test);
    void inject(SmashRosterStorageTest test);
    void inject(TimberTest test);

    // models
    void inject(AbsPlayerTest test);
    void inject(AbsRegionTest test);
    void inject(AbsTournamentTest test);
    void inject(AvatarTest test);
    void inject(MatchTest test);
    void inject(RankingCriteriaTest test);
    void inject(RankingsBundleTest test);
    void inject(RegionsBundleTest test);
    void inject(SimpleDateTest test);
    void inject(SmashCharacterTest test);

    // networking
    void inject(PlayerMatchesBundleApiCallTest test);
    void inject(RegionsBundleApiCallTest test);

    // preferences
    void inject(KeyValueStoreProviderTest test);
    void inject(KeyValueStoreTest test);
    void inject(PersistentBooleanPreferenceTest test);
    void inject(PersistentGsonPreferenceTest test);
    void inject(PersistentIntegerPreferenceTest test);
    void inject(PersistentLongPreferenceTest test);
    void inject(PersistentStringPreferenceTest test);
    void inject(PersistentUriPreferenceTest test);

    // sync
    void inject(RankingsPollingSyncManagerTest test);
    void inject(SmashRosterSyncManagerTest test);

}
