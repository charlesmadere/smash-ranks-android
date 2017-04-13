package com.garpr.android.models;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RegionsBundleTest extends BaseTest {

    private static final String JSON_GAR_PR_REGIONS_BUNDLE = "{\"regions\":[{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":60,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":30,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000}]}";
    private static final String JSON_NOT_GAR_PR_REGIONS_BUNDLE = "{\"regions\":[{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Alabama\",\"id\":\"alabama\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Central Florida\",\"id\":\"cfl\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180},{\"ranking_num_tourneys_attended\":3,\"ranking_activity_day_limit\":999,\"display_name\":\"Georgia Smash 4\",\"id\":\"georgia smash 4\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Georgia Teams\",\"id\":\"georgia teams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"GeorgiaTeams\",\"id\":\"georgiateams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":91,\"display_name\":\"Long Island\",\"id\":\"li\",\"tournament_qualified_day_limit\":366},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":99999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"NYC Smash 64\",\"id\":\"nyc64\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":99999,\"display_name\":\"New England\",\"id\":\"newengland\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":120,\"display_name\":\"North Carolina\",\"id\":\"northcarolina\",\"tournament_qualified_day_limit\":365},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Philadelphia\",\"id\":\"philadelphia\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Pittsburgh\",\"id\":\"pittsburgh\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"South Carolina\",\"id\":\"southcarolina\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Westchester\",\"id\":\"westchester\",\"tournament_qualified_day_limit\":999}]}";

    private RegionsBundle mGarPr;
    private RegionsBundle mNotGarPr;

    @Inject
    Gson mGson;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);

        mGarPr = mGson.fromJson(JSON_GAR_PR_REGIONS_BUNDLE, RegionsBundle.class);
        mNotGarPr = mGson.fromJson(JSON_NOT_GAR_PR_REGIONS_BUNDLE, RegionsBundle.class);
    }

    @Test
    public void testGarPrMergeWithItself() throws Exception {
        assertNotNull(mGarPr.getRegions());
        assertEquals(mGarPr.getRegions().size(), 2);

        mGarPr.merge(mGarPr);
        assertNotNull(mGarPr.getRegions());
        assertEquals(mGarPr.getRegions().size(), 2);
    }

    @Test
    public void testGarPrMergeWithNotGarPr() throws Exception {
        assertNotNull(mGarPr.getRegions());
        assertEquals(mGarPr.getRegions().size(), 2);

        mGarPr.merge(mNotGarPr);
        assertNotNull(mGarPr.getRegions());
        assertEquals(mGarPr.getRegions().size(), 19);
    }

    @Test
    public void testGarPrMergeWithNull() throws Exception {
        assertNotNull(mGarPr.getRegions());
        assertEquals(mGarPr.getRegions().size(), 2);

        mGarPr.merge(null);
        assertNotNull(mGarPr.getRegions());
        assertEquals(mGarPr.getRegions().size(), 2);
    }

    @Test
    public void testNotGarPrMergeWithGarPr() throws Exception {
        assertNotNull(mNotGarPr.getRegions());
        assertEquals(mNotGarPr.getRegions().size(), 17);

        mNotGarPr.merge(mGarPr);
        assertNotNull(mNotGarPr.getRegions());
        assertEquals(mNotGarPr.getRegions().size(), 19);
    }

    @Test
    public void testNotGarPrMergeWithItself() throws Exception {
        assertNotNull(mNotGarPr.getRegions());
        assertEquals(mNotGarPr.getRegions().size(), 17);

        mNotGarPr.merge(mNotGarPr);
        assertNotNull(mNotGarPr.getRegions());
        assertEquals(mNotGarPr.getRegions().size(), 17);
    }

    @Test
    public void testNotGarPrMergeWithNull() throws Exception {
        assertNotNull(mNotGarPr.getRegions());
        assertEquals(mNotGarPr.getRegions().size(), 17);

        mNotGarPr.merge(null);
        assertNotNull(mNotGarPr.getRegions());
        assertEquals(mNotGarPr.getRegions().size(), 17);
    }

}
