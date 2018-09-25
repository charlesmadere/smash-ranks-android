package com.garpr.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.garpr.android.dagger.DaggerTestAppComponent;
import com.garpr.android.dagger.TestAppComponent;
import com.garpr.android.dagger.TestAppComponentHandle;
import com.garpr.android.dagger.TestAppModule;
import com.garpr.android.misc.Constants;

import org.robolectric.RuntimeEnvironment;

public class TestApp extends BaseApp implements TestAppComponentHandle {

    @Nullable
    private TestAppComponent mTestAppComponent;


    @NonNull
    @Override
    public TestAppComponent getTestAppComponent() {
        final TestAppComponent testAppComponent = mTestAppComponent;

        if (testAppComponent == null) {
            throw new IllegalArgumentException("mTestAppComponent is null");
        }

        return testAppComponent;
    }

    private void initializeAppComponent() {
        mTestAppComponent = DaggerTestAppComponent.builder()
                .testAppModule(new TestAppModule(RuntimeEnvironment.application,
                        Constants.INSTANCE.getDefaultRegion(), Constants.SMASH_ROSTER_BASE_PATH))
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeAppComponent();
    }

}
