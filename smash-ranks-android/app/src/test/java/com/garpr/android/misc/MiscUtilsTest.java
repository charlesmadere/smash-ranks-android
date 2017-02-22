package com.garpr.android.misc;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MiscUtilsTest extends BaseTest {

    @Test
    public void testTruncateFloatImytRanking() throws Exception {
        final String value = MiscUtils.truncateFloat(31.384343063802955f);
        assertEquals(value, "31.384");
    }

    @Test
    public void testTruncateFloatMaxValue() throws Exception {
        final String value = MiscUtils.truncateFloat(Float.MAX_VALUE);
        assertEquals(value, "340282346638528860000000000000000000000.000");
    }

    @Test
    public void testTruncateFloatMinValue() throws Exception {
        final String value = MiscUtils.truncateFloat(Float.MIN_VALUE);
        assertEquals(value, "0.000");
    }

    @Test
    public void testTruncateFloatPi() throws Exception {
        final String value = MiscUtils.truncateFloat((float) Math.PI);
        assertEquals(value, "3.142");
    }

    @Test
    public void testTruncateFloatSfatRanking() throws Exception {
        final String value = MiscUtils.truncateFloat(41.906930141097256f);
        assertEquals(value, "41.907");
    }

}
