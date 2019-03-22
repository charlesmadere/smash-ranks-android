package com.garpr.android;

import com.garpr.android.dagger.AppComponent;
import com.garpr.android.dagger.AppComponentHandle;
import com.garpr.android.dagger.AppModule;
import com.garpr.android.dagger.DaggerAppComponent;
import com.garpr.android.managers.AppUpgradeManager;
import com.garpr.android.managers.NightModeManager;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.Timber;
import com.garpr.android.wrappers.ImageLibraryWrapper;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

public class App extends BaseApp implements AppComponentHandle,
        NightModeManager.OnNightModeChangeListener {

    private static final String TAG = "App";

    @Nullable
    private AppComponent mAppComponent;

    @Inject
    AppUpgradeManager mAppUpgradeManager;

    @Inject
    CrashlyticsWrapper mCrashlyticsWrapper;

    @Inject
    DeviceUtils mDeviceUtils;

    @Inject
    ImageLibraryWrapper mImageLibraryWrapper;

    @Inject
    NightModeManager mNightModeManager;

    @Inject
    Timber mTimber;


    private void applyNightMode() {
        AppCompatDelegate.setDefaultNightMode(mNightModeManager.getNightMode().getThemeValue());
        mNightModeManager.addListener(this);
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

        // The order of the following lines is important!

        initializeAppComponent();
        initializeCrashlytics();

        mTimber.d(TAG, "App created", null);

        applyNightMode();

        mImageLibraryWrapper.initialize();
        mAppUpgradeManager.upgradeApp();
    }

    @Override
    public void onNightModeChange(@NonNull final NightModeManager nightModeManager) {
        AppCompatDelegate.setDefaultNightMode(nightModeManager.getNightMode().getThemeValue());
    }

}
