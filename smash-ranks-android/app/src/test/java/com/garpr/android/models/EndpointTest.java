package com.garpr.android.models;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EndpointTest extends BaseTest {

    @Test
    public void testGetApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/", Endpoint.GAR_PR.getApiPath());
        assertEquals("https://www.notgarpr.com:3001/", Endpoint.NOT_GAR_PR.getApiPath());
    }

    @Test
    public void testGetWebPath() throws Exception {
        assertEquals("https://www.garpr.com/", Endpoint.GAR_PR.getWebPath());
        assertEquals("https://www.notgarpr.com/", Endpoint.NOT_GAR_PR.getWebPath());
    }

}
