package com.garpr.android;

import com.garpr.android.dagger.DaggerTestAppComponent;
import com.garpr.android.dagger.TestAppComponent;
import com.garpr.android.dagger.TestAppComponentHandle;
import com.garpr.android.dagger.TestAppModule;
import com.garpr.android.misc.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.robolectric.RuntimeEnvironment;

public class TestApp extends BaseApp implements TestAppComponentHandle {

    @Nullable
    private TestAppComponent mTestAppComponent;


    @NotNull
    @Override
    public TestAppComponent getTestAppComponent() {
        final TestAppComponent testAppComponent = mTestAppComponent;

        if (testAppComponent == null) {
            throw new IllegalStateException("mTestAppComponent is null");
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
