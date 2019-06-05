package com.garpr.android;

import android.app.Application;

import com.garpr.android.dagger.AppModule;
import com.garpr.android.dagger.TestConfigModule;
import com.garpr.android.dagger.DaggerTestAppComponent;
import com.garpr.android.dagger.TestAppComponent;
import com.garpr.android.dagger.TestAppComponentHandle;
import com.garpr.android.misc.Constants;

import org.jetbrains.annotations.Nullable;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

public class TestApp extends BaseApp implements TestAppComponentHandle {

    @Nullable
    private TestAppComponent mTestAppComponent;


    @NonNull
    @Override
    public TestAppComponent getTestAppComponent() throws IllegalStateException {
        final TestAppComponent testAppComponent = mTestAppComponent;

        if (testAppComponent == null) {
            throw new IllegalStateException("mTestAppComponent is null");
        }

        return testAppComponent;
    }

    private void initializeAppComponent() {
        final Application application = ApplicationProvider.getApplicationContext();
        mTestAppComponent = DaggerTestAppComponent.builder()
                .appModule(new AppModule(
                        application,
                        Constants.INSTANCE.getDefaultRegion(),
                        Constants.SMASH_ROSTER_BASE_PATH
                ))
                .testConfigModule(new TestConfigModule())
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeAppComponent();
    }

}
