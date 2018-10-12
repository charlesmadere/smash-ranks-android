package com.garpr.android;

import com.garpr.android.dagger.TestAppComponent;
import com.garpr.android.dagger.TestAppComponentHandle;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@Config(application = TestApp.class)
@RunWith(RobolectricTestRunner.class)
public abstract class BaseTest implements TestAppComponentHandle {

    @NotNull
    @Override
    public TestAppComponent getTestAppComponent() {
        return ((TestAppComponentHandle) RuntimeEnvironment.application).getTestAppComponent();
    }

    @Before
    public void setUp() {
        // intentionally empty
    }

}
