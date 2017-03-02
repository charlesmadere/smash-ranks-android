package com.garpr.android.misc;

import android.app.Application;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RegionManagerTest extends BaseTest {

    @Inject
    Application mApplication;

    @Inject
    RegionManager mRegionManager;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testAddListener() throws Exception {
        final String[] array = new String[1];

        final RegionManager.OnRegionChangeListener listener =
                new RegionManager.OnRegionChangeListener() {
            @Override
            public void onRegionChange(final RegionManager regionManager) {
                array[0] = regionManager.getRegion();
            }
        };

        mRegionManager.addListener(listener);
        assertNull(array[0]);

        mRegionManager.setRegion("louisiana");
        assertEquals("louisiana", array[0]);
    }

    @Test
    public void testGetRegion() throws Exception {
        assertNotNull(mRegionManager.getRegion());
    }

    @Test
    public void testGetRegionWithContext() throws Exception {
        assertNotNull(mRegionManager.getRegion(mApplication));
    }

    @Test
    public void testRemoveListener() throws Exception {
        final String[] array = new String[1];

        final RegionManager.OnRegionChangeListener listener =
                new RegionManager.OnRegionChangeListener() {
            @Override
            public void onRegionChange(final RegionManager regionManager) {
                array[0] = regionManager.getRegion();
            }
        };

        mRegionManager.addListener(listener);
        mRegionManager.setRegion("texas");
        assertEquals("texas", array[0]);

        mRegionManager.removeListener(listener);
        mRegionManager.setRegion("newyork");
        assertEquals("texas", array[0]);
    }

    @Test
    public void testSetRegion() throws Exception {
        assertNotNull(mRegionManager.getRegion());

        mRegionManager.setRegion("hello");
        assertNotNull(mRegionManager.getRegion());
    }

}
