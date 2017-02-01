package com.garpr.android;

import android.app.Application;

import com.garpr.android.dagger.AppComponent;
import com.garpr.android.dagger.AppModule;
import com.garpr.android.dagger.DaggerAppComponent;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsWrapper;

import javax.inject.Inject;

public class App extends Application {

    private static App sInstance;

    private AppComponent mAppComponent;

    @Inject
    CrashlyticsWrapper mCrashlyticsWrapper;


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
                .appModule(new AppModule(this, Constants.GAR_PR_URL))
                .build();
        mAppComponent.inject(this);

        mCrashlyticsWrapper.initialize(BuildConfig.DEBUG);
    }

}
