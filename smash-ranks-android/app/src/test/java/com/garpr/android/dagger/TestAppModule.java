package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.TestDeviceUtilsImpl;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.Region;
import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.KeyValueStoreImpl;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TestAppModule extends BaseAppModule {

    public TestAppModule(@NonNull final Application application,
            @NonNull final Region defaultRegion) {
        super(application, defaultRegion);
    }

    @Provides
    @Singleton
    CrashlyticsWrapper providesCrashlyticsWrapper() {
        return new CrashlyticsWrapper() {
            @Override
            public void initialize(final boolean disabled) {
                // intentionally empty
            }

            @Override
            public void log(final int priority, @NonNull final String tag, @NonNull final String msg) {
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
            public void setString(@NonNull final String key, @NonNull final String value) {
                // intentionally empty
            }
        };
    }

    @Provides
    @Singleton
    DeviceUtils providesDeviceUtils() {
        return new TestDeviceUtilsImpl();
    }

    @Provides
    @Singleton
    KeyValueStore providesKeyValueStore(final Application application) {
        return new KeyValueStoreImpl(application, "TEST");
    }

    @Provides
    @Singleton
    ThreadUtils providesThreadUtils() {
        return new ThreadUtils() {
            @Override
            public void run(@NotNull final Task task) {
                task.onBackground();
                task.onUi();
            }

            @Override
            public void runOnBackground(@NotNull final Runnable task) {
                task.run();
            }

            @Override
            public void runOnUi(@NotNull final Runnable task) {
                task.run();
            }
        };
    }

}
