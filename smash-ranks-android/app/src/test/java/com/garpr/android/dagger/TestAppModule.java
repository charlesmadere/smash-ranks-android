package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.KeyValueStoreImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TestAppModule extends AppModule {

    public TestAppModule(@NonNull final Application application, @NonNull final String garPrApiUrl,
            @NonNull final String garPrWebUrl, @NonNull final String defaultRegion) {
        super(application, garPrApiUrl, garPrWebUrl, defaultRegion);
    }

    @Provides
    @Singleton
    KeyValueStore providesKeyValueStore(final Application application) {
        return new KeyValueStoreImpl(application, "TEST");
    }

}
