package com.garpr.android;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.garpr.android.dagger.AppComponent;
import com.garpr.android.dagger.AppModule;
import com.garpr.android.dagger.DaggerAppComponent;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.Timber;
import com.garpr.android.models.NightMode;
import com.garpr.android.preferences.GeneralPreferenceStore;
import com.garpr.android.preferences.Preference;

import javax.inject.Inject;

public class App extends Application {

    private static final String TAG = "App";

    private static App sInstance;

    private AppComponent mAppComponent;

    @Inject
    CrashlyticsWrapper mCrashlyticsWrapper;

    @Inject
    DeviceUtils mDeviceUtils;

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;

    @Inject
    GeneralPreferenceStore mGeneralPreferenceStore;

    @Inject
    Timber mTimber;


    public static App get() {
        return sInstance;
    }

    private void applyNightMode() {
        final NightMode nightMode = mGeneralPreferenceStore.getNightMode().get();
        AppCompatDelegate.setDefaultNightMode(nightMode != null ? nightMode.getThemeValue() :
                NightMode.SYSTEM.getThemeValue());
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private void initializeAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this, Constants.INSTANCE.getDefaultRegion()))
                .build();

        mAppComponent.inject(this);
    }

    private void initializeCrashlytics() {
        mCrashlyticsWrapper.initialize(BuildConfig.DEBUG);
        mCrashlyticsWrapper.setBool("low_ram_device", mDeviceUtils.hasLowRam());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        initializeAppComponent();
        initializeCrashlytics();

        mTimber.d(TAG, "App created");

        applyNightMode();
        upgradeApp();
    }

    private void upgradeApp() {
        final Preference<Integer> lastVersionPref = mGeneralPreferenceStore.getLastVersion();
        final Integer lastVersion = lastVersionPref.get();

        if (lastVersion == null) {
            mTimber.d(TAG, "App has no previous version, is now " + BuildConfig.VERSION_CODE);
        } else if (lastVersion < BuildConfig.VERSION_CODE) {
            mTimber.d(TAG, "App's previous version was " + lastVersion + ", is now " +
                    BuildConfig.VERSION_CODE);
        } else {
            mTimber.d(TAG, "App doesn't need to upgrade (on version " + lastVersion + ")");
            return;
        }

        lastVersionPref.set(BuildConfig.VERSION_CODE);

        if (lastVersion == null || lastVersion < 1011) {
            // this preference used to be a String but was changed to an AbsRegion
            mGeneralPreferenceStore.getCurrentRegion().delete();

            // it used to be an AbsPlayer, is now a FavoritePlayer
            mGeneralPreferenceStore.getIdentity().delete();

            // same as above :(
            mFavoritePlayersManager.clear();
        }
    }

}
