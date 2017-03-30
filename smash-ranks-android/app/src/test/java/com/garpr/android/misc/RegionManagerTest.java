package com.garpr.android.misc;

import android.app.Application;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.garpr.android.models.Region;
import com.google.gson.Gson;

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

    private static final String JSON_REGION_ALABAMA = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Alabama\",\"id\":\"alabama\",\"tournament_qualified_day_limit\":999}";
    private static final String JSON_REGION_GEORGIA = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180}";
    private static final String JSON_REGION_NYC = "{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":999}";

    private Region mAlabama;
    private Region mGeorgia;
    private Region mNyc;

    @Inject
    Application mApplication;

    @Inject
    Gson mGson;

    @Inject
    RegionManager mRegionManager;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);

        mAlabama = mGson.fromJson(JSON_REGION_ALABAMA, Region.class);
        mGeorgia = mGson.fromJson(JSON_REGION_GEORGIA, Region.class);
        mNyc = mGson.fromJson(JSON_REGION_NYC, Region.class);
    }

    @Test
    public void testAddListener() throws Exception {
        final Region[] array = new Region[1];

        final RegionManager.OnRegionChangeListener listener =
                new RegionManager.OnRegionChangeListener() {
            @Override
            public void onRegionChange(final RegionManager regionManager) {
                array[0] = regionManager.getRegion();
            }
        };

        mRegionManager.addListener(listener);
        assertNull(array[0]);

        mRegionManager.setRegion(mAlabama);
        assertEquals(mAlabama, array[0]);
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
        final Region[] array = new Region[1];

        final RegionManager.OnRegionChangeListener listener =
                new RegionManager.OnRegionChangeListener() {
            @Override
            public void onRegionChange(final RegionManager regionManager) {
                array[0] = regionManager.getRegion();
            }
        };

        mRegionManager.addListener(listener);
        mRegionManager.setRegion(mNyc);
        assertEquals(mNyc, array[0]);

        mRegionManager.removeListener(listener);
        mRegionManager.setRegion(mGeorgia);
        assertEquals(mNyc, array[0]);
    }

    @Test
    public void testSetRegion() throws Exception {
        assertNotNull(mRegionManager.getRegion());

        mRegionManager.setRegion(mGeorgia);
        assertNotNull(mRegionManager.getRegion());
    }

}
