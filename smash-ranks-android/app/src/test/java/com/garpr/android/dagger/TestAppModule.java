package com.garpr.android.dagger;

import android.app.Application;

import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.TestDeviceUtilsImpl;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.Region;
import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.KeyValueStoreImpl;
import com.garpr.android.wrappers.ImageLibraryWrapper;
import com.garpr.android.wrappers.WorkManagerWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.work.WorkRequest;
import dagger.Module;
import dagger.Provides;

@Module
public class TestAppModule extends BaseAppModule {

    public TestAppModule(@NonNull final Application application,
            @NonNull final Region defaultRegion, @NonNull final String smashRosterBasePath) {
        super(application, defaultRegion, smashRosterBasePath);
    }

    @NonNull
    @Provides
    @Singleton
    CrashlyticsWrapper providesCrashlyticsWrapper() {
        return new CrashlyticsWrapper() {
            @Override
            public void initialize(final boolean disabled) {
                // intentionally empty
            }

            @Override
            public void log(final int priority, @NotNull final String tag, @NotNull final String msg) {
                // intentionally empty
            }

            @Override
            public void logException(@NotNull final Throwable tr) {
                // intentionally empty
            }

            @Override
            public void setBool(@NotNull final String key, final boolean value) {
                // intentionally empty
            }

            @Override
            public void setInt(@NotNull final String key, final int value) {
                // intentionally empty
            }

            @Override
            public void setString(@NotNull final String key, @NotNull final String value) {
                // intentionally empty
            }
        };
    }

    @NonNull
    @Provides
    @Singleton
    DeviceUtils providesDeviceUtils(@NonNull final Application application) {
        return new TestDeviceUtilsImpl(application);
    }

    @NonNull
    @Provides
    @Singleton
    ImageLibraryWrapper providesImageLibraryWrapper() {
        return new ImageLibraryWrapper() {
            @Override
            public void initialize() {
                // intentionally empty
            }
        };
    }

    @NonNull
    @Provides
    @Singleton
    KeyValueStore providesKeyValueStore(@NonNull final Application application) {
        return new KeyValueStoreImpl(application, "TEST");
    }

    @NonNull
    @Provides
    @Singleton
    ThreadUtils providesThreadUtils() {
        return new ThreadUtils() {
            private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

            @NotNull
            @Override
            public ExecutorService getExecutorService() {
                return mExecutorService;
            }

            @Override
            public boolean isUiThread() {
                return false;
            }

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

    @NonNull
    @Provides
    @Singleton
    WorkManagerWrapper providesWorkManagerWrapper() {
        return new WorkManagerWrapper() {
            @Override
            public void cancelAllWorkByTag(@NotNull final String tag) {
                // intentionally empty
            }

            @Override
            public void enqueue(@NotNull final WorkRequest workRequest) {
                // intentionally empty
            }
        };
    }

}
