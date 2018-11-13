package com.garpr.android;

import com.garpr.android.dagger.AppComponent;
import com.garpr.android.dagger.AppComponentHandle;
import com.garpr.android.dagger.AppModule;
import com.garpr.android.dagger.DaggerAppComponent;
import com.garpr.android.managers.AppUpgradeManager;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.Timber;
import com.garpr.android.models.NightMode;
import com.garpr.android.preferences.GeneralPreferenceStore;
import com.garpr.android.wrappers.ImageLibraryWrapper;
import com.garpr.android.wrappers.WorkManagerWrapper;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

public class App extends BaseApp implements AppComponentHandle {

    private static final String TAG = "App";

    @Nullable
    private static App sInstance;

    @Nullable
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
    ImageLibraryWrapper mImageLibraryWrapper;

    @Inject
    Timber mTimber;

    @Inject
    WorkManagerWrapper mWorkManagerWrapper;


    @NonNull
    public static App get() throws IllegalStateException {
        final App instance = sInstance;

        if (instance == null) {
            throw new IllegalStateException("sInstance is null");
        }

        return instance;
    }

    private void applyNightMode() {
        final NightMode nightMode = mGeneralPreferenceStore.getNightMode().get();
        AppCompatDelegate.setDefaultNightMode(nightMode != null ? nightMode.getThemeValue() :
                NightMode.SYSTEM.getThemeValue());
    }

    @NotNull
    @Override
    public AppComponent getAppComponent() throws IllegalStateException {
        final AppComponent appComponent = mAppComponent;

        if (appComponent == null) {
            throw new IllegalStateException("mAppComponent is null");
        }

        return appComponent;
    }

    private void initializeAppComponent() {
        final AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(
                        this,
                        Constants.INSTANCE.getDefaultRegion(),
                        Constants.SMASH_ROSTER_BASE_PATH)
                )
                .build();

        appComponent.inject(this);
        mAppComponent = appComponent;
    }

    private void initializeCrashlytics() {
        mCrashlyticsWrapper.initialize(BuildConfig.DEBUG);
        mCrashlyticsWrapper.setBool("low_ram_device", mDeviceUtils.getHasLowRam());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // The order of the following lines is important!

        initializeAppComponent();
        initializeCrashlytics();

        mTimber.d(TAG, "App created", null);

        applyNightMode();

        mImageLibraryWrapper.initialize();
        mWorkManagerWrapper.initialize();
        mAppUpgradeManager.upgradeApp();
    }

}
