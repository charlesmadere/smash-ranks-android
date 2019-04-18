package com.garpr.android;

import android.app.Application;

import com.garpr.android.dagger.TestAppComponent;
import com.garpr.android.dagger.TestAppComponentHandle;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

@Config(application = TestApp.class)
@RunWith(RobolectricTestRunner.class)
public abstract class BaseTest implements TestAppComponentHandle {

    @NonNull
    @Override
    public TestAppComponent getTestAppComponent() {
        final Application application = ApplicationProvider.getApplicationContext();
        return ((TestAppComponentHandle) application).getTestAppComponent();
    }

    @Before
    public void setUp() {
        // intentionally empty
    }

}
