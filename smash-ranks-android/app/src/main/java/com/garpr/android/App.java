package com.garpr.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.garpr.android.dagger.AppComponent;
import com.garpr.android.dagger.AppModule;
import com.garpr.android.dagger.DaggerAppComponent;
import com.garpr.android.misc.Constants;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    private static App sInstance;

    private AppComponent mAppComponent;


    public static App getInstance() {
        return sInstance;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Fabric.with(sInstance, new Crashlytics());

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(sInstance, Constants.GAR_PR_URL))
                .build();
    }

}
