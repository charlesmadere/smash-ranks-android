package com.garpr.android;

import com.garpr.android.dagger.DaggerTestAppComponent;
import com.garpr.android.dagger.TestAppComponent;
import com.garpr.android.dagger.TestAppModule;
import com.garpr.android.misc.Constants;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public abstract class BaseTest {

    private TestAppComponent mTestAppComponent;


    public TestAppComponent getTestAppComponent() {
        return mTestAppComponent;
    }

    @Before
    public void setUp() throws Exception {
        mTestAppComponent = DaggerTestAppComponent.builder()
                .testAppModule(new TestAppModule(RuntimeEnvironment.application,
                        Constants.INSTANCE.getDEFAULT_REGION()))
                .build();
    }

}
