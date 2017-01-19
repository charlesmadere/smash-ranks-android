package com.garpr.android;

import android.app.Application;

public class App extends Application {

    private static App sInstance;


    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

}
