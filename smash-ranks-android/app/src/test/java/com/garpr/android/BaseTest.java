package com.garpr.android;

import com.garpr.android.dagger.DaggerTestAppComponent;
import com.garpr.android.dagger.TestAppComponent;
import com.garpr.android.dagger.TestAppModule;
import com.garpr.android.misc.Constants;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public abstract class BaseTest {

    private TestAppComponent mTestAppComponent;


    public TestAppComponent getTestAppComponent() {
        return mTestAppComponent;
    }

    @Before
    public void setUp() throws Exception {
        mTestAppComponent = DaggerTestAppComponent.builder()
                .testAppModule(new TestAppModule(RuntimeEnvironment.application,
                        Constants.INSTANCE.getDefaultRegion()))
                .build();
    }

}
