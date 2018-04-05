package com.garpr.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.garpr.android.dagger.AppComponent;
import com.garpr.android.dagger.AppModule;
import com.garpr.android.dagger.DaggerAppComponent;
import com.garpr.android.managers.AppUpgradeManager;
import com.garpr.android.managers.ImageLibraryManager;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.Timber;
import com.garpr.android.models.NightMode;
import com.garpr.android.preferences.GeneralPreferenceStore;

import javax.inject.Inject;

public class App extends Application {

    private static final String TAG = "App";

    private static App sInstance;

    private AppComponent mAppComponent;

    @Inject
    AppUpgradeManager mAppUpgradeManager;

    @Inject
    CrashlyticsWrapper mCrashlyticsWrapper;

    @Inject
    DeviceUtils mDeviceUtils;

    @Inject
    GeneralPreferenceStore mGeneralPreferenceStore;

    @Inject
    ImageLibraryManager mImageLibraryManager;

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

    @NonNull
    public AppComponent getAppComponent() {
        final AppComponent appComponent = mAppComponent;

        if (appComponent == null) {
            throw new IllegalStateException("mAppComponent is null");
        }

        return appComponent;
    }

    private void initializeAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this, Constants.INSTANCE.getDefaultRegion()))
                .build();

        mAppComponent.inject(this);
    }

    private void initializeCrashlytics() {
        mCrashlyticsWrapper.initialize(BuildConfig.DEBUG);
        mCrashlyticsWrapper.setBool("low_ram_device", mDeviceUtils.getHasLowRam());
    }

    private void initializeImageLibrary() {
        mImageLibraryManager.initialize();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // the following order is important
        initializeAppComponent();
        initializeCrashlytics();
        initializeImageLibrary();

        mTimber.d(TAG, "App created", null);

        applyNightMode();
        mAppUpgradeManager.upgradeApp();
    }

}
