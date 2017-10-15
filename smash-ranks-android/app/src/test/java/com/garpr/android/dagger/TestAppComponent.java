package com.garpr.android.dagger;

import com.garpr.android.misc.DeepLinkUtilsTest;
import com.garpr.android.misc.FavoritePlayersManagerTest;
import com.garpr.android.misc.FullTournamentUtilsTest;
import com.garpr.android.misc.HomeToolbarManagerTest;
import com.garpr.android.misc.IdentityManagerTest;
import com.garpr.android.misc.KeyValueStoreProviderTest;
import com.garpr.android.misc.ListUtilsTest;
import com.garpr.android.misc.PlayerToolbarManagerTest;
import com.garpr.android.misc.PreviousRankUtilsTest;
import com.garpr.android.misc.RankingsNotificationsUtilsTest;
import com.garpr.android.misc.RegionManagerTest;
import com.garpr.android.misc.SmashRosterStorageTest;
import com.garpr.android.misc.TimberTest;
import com.garpr.android.misc.TournamentToolbarManagerTest;
import com.garpr.android.models.AbsPlayerTest;
import com.garpr.android.models.AbsRegionTest;
import com.garpr.android.models.AbsTournamentTest;
import com.garpr.android.models.MatchTest;
import com.garpr.android.models.RegionsBundleTest;
import com.garpr.android.models.SimpleDateTest;
import com.garpr.android.networking.PlayerMatchesBundleApiCallTest;
import com.garpr.android.networking.RegionsBundleApiCallTest;
import com.garpr.android.preferences.KeyValueStoreTest;
import com.garpr.android.preferences.persistent.PersistentBooleanPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentGsonPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentIntegerPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentLongPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentStringPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentUriPreferenceTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { TestAppModule.class })
public interface TestAppComponent {

    // misc
    void inject(DeepLinkUtilsTest test);
    void inject(FavoritePlayersManagerTest test);
    void inject(FullTournamentUtilsTest test);
    void inject(HomeToolbarManagerTest test);
    void inject(IdentityManagerTest test);
    void inject(KeyValueStoreProviderTest test);
    void inject(ListUtilsTest test);
    void inject(PlayerToolbarManagerTest test);
    void inject(PreviousRankUtilsTest test);
    void inject(RankingsNotificationsUtilsTest test);
    void inject(RegionManagerTest test);
    void inject(SmashRosterStorageTest test);
    void inject(TimberTest test);
    void inject(TournamentToolbarManagerTest test);

    // models
    void inject(AbsPlayerTest test);
    void inject(AbsRegionTest test);
    void inject(AbsTournamentTest test);
    void inject(MatchTest test);
    void inject(RegionsBundleTest test);
    void inject(SimpleDateTest test);

    // networking
    void inject(PlayerMatchesBundleApiCallTest test);
    void inject(RegionsBundleApiCallTest test);

    // preferences
    void inject(KeyValueStoreTest test);
    void inject(PersistentBooleanPreferenceTest test);
    void inject(PersistentGsonPreferenceTest test);
    void inject(PersistentIntegerPreferenceTest test);
    void inject(PersistentLongPreferenceTest test);
    void inject(PersistentStringPreferenceTest test);
    void inject(PersistentUriPreferenceTest test);

}
