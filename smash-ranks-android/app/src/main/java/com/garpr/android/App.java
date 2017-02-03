package com.garpr.android;

import android.app.Application;

import com.garpr.android.dagger.AppComponent;
import com.garpr.android.dagger.AppModule;
import com.garpr.android.dagger.DaggerAppComponent;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.Timber;

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
    Timber mTimber;


    public static App get() {
        return sInstance;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this, Constants.GAR_PR_URL, Constants.DEFAULT_REGION))
                .build();
        mAppComponent.inject(this);

        mCrashlyticsWrapper.initialize(BuildConfig.DEBUG);
        mCrashlyticsWrapper.setBool("low_ram_device", mDeviceUtils.hasLowRam());

        mTimber.d(TAG, "App created");
    }

}
