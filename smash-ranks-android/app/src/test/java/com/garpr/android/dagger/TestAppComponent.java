package com.garpr.android.dagger;

import com.garpr.android.misc.DeepLinkUtilsTest;
import com.garpr.android.misc.IdentityManagerTest;
import com.garpr.android.misc.ListUtilsTest;
import com.garpr.android.misc.RegionManagerTest;
import com.garpr.android.models.AbsPlayerTest;
import com.garpr.android.models.AbsTournamentTest;
import com.garpr.android.models.RatingsTest;
import com.garpr.android.models.SimpleDateTest;
import com.garpr.android.preferences.persistent.PersistentBooleanPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentGsonPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentIntegerPreferenceTest;
import com.garpr.android.preferences.persistent.PersistentStringPreferenceTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { TestAppModule.class })
public interface TestAppComponent {

    // misc
    void inject(DeepLinkUtilsTest test);
    void inject(IdentityManagerTest test);
    void inject(ListUtilsTest test);
    void inject(RegionManagerTest test);

    // models
    void inject(AbsPlayerTest test);
    void inject(AbsTournamentTest test);
    void inject(RatingsTest test);
    void inject(SimpleDateTest test);

    // preferences
    void inject(PersistentBooleanPreferenceTest test);
    void inject(PersistentGsonPreferenceTest test);
    void inject(PersistentIntegerPreferenceTest test);
    void inject(PersistentStringPreferenceTest test);

}
