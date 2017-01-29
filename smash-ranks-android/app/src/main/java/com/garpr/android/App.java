package com.garpr.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.garpr.android.dagger.AppComponent;
import com.garpr.android.dagger.AppModule;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    private static App sInstance;

    private AppComponent mAppComponent;


    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Fabric.with(sInstance, new Crashlytics());

        mAppComponent = DaggerAppComponent.Builder()
                .daggerModule(new AppModule(sInstance))
                .build();
    }

}
