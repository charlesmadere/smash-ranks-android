package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.garpr.android.models.Region;
import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.KeyValueStoreImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TestAppModule extends AppModule {

    public TestAppModule(@NonNull final Application application, @NonNull final Region defaultRegion) {
        super(application, defaultRegion);
    }

    /*
    @Provides
    @Override
    @Singleton
    CrashlyticsWrapper providesCrashlyticsWrapper() {
        return new CrashlyticsWrapper() {
            @Override
            public void initialize(final boolean disabled) {
                // intentionally empty
            }

            @Override
            public void log(final int priority, final String tag, final String msg) {
                // intentionally empty
            }

            @Override
            public void logException(@NonNull final Throwable tr) {
                // intentionally empty
            }

            @Override
            public void setBool(@NonNull final String key, final boolean value) {
                // intentionally empty
            }

            @Override
            public void setInt(@NonNull final String key, final int value) {
                // intentionally empty
            }

            @Override
            public void setString(@NonNull final String key, final String value) {
                // intentionally empty
            }
        };
    }
    */

    @Provides
    @Singleton
    KeyValueStore providesKeyValueStore(final Application application) {
        return new KeyValueStoreImpl(application, "TEST");
    }

}
