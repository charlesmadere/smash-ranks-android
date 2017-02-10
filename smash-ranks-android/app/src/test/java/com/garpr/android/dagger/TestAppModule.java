package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import dagger.Module;

@Module
public class TestAppModule extends AppModule {

    public TestAppModule(@NonNull final Application application, @NonNull final String garPrApiUrl,
            @NonNull final String garPrWebUrl, @NonNull final String defaultRegion) {
        super(application, garPrApiUrl, garPrWebUrl, defaultRegion);
    }

}
