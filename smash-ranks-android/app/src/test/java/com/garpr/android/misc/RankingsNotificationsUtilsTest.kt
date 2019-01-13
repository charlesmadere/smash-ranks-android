package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.misc.RankingsNotificationsUtils.NotificationInfo.CANCEL
import com.garpr.android.misc.RankingsNotificationsUtils.NotificationInfo.NO_CHANGE
import com.garpr.android.misc.RankingsNotificationsUtils.NotificationInfo.SHOW
import com.garpr.android.misc.RankingsNotificationsUtils.PollStatus
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RankingsNotificationsUtilsTest : BaseTest() {

    @Inject
    protected lateinit var deviceUtils: DeviceUtils

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var rankingsNotificationsUtils: RankingsNotificationsUtils

    @Inject
    protected lateinit var rankingsPollingPreferenceStore: RankingsPollingPreferenceStore

    private lateinit var rankingsBundle: RankingsBundle


    companion object {
        private const val RANKINGS_ID_1 = "5ae78567d2994e288f49cdc8"
        private const val RANKINGS_ID_2 = "5ae2c2e7d2994e288f49cdaa"
        private const val RANKINGS_ID_3 = "5ae8abbb1d41c852c724113c"

        private const val JSON_RANKINGS_BUNDLE = "{\"ranking\":[{\"rating\":44.001720505358705,\"name\":\"CLG. | PewPewU\",\"rank\":1,\"previous_rank\":null,\"id\":\"588852e8d2994e3bbfa52da7\"},{\"rating\":42.466830438279636,\"name\":\"Azusa\",\"rank\":2,\"previous_rank\":2,\"id\":\"588852e8d2994e3bbfa52d8c\"},{\"rating\":41.526856291909986,\"name\":\"NMW\",\"rank\":3,\"previous_rank\":3,\"id\":\"583a4a15d2994e0577b05c8a\"},{\"rating\":41.27324699193703,\"name\":\"Spark\",\"rank\":4,\"previous_rank\":5,\"id\":\"5877eb55d2994e15c7dea97e\"},{\"rating\":40.42641903016027,\"name\":\"Rocky\",\"rank\":5,\"previous_rank\":6,\"id\":\"5877eb55d2994e15c7dea982\"},{\"rating\":40.1687987646224,\"name\":\"SPY | Nintendude\",\"rank\":6,\"previous_rank\":null,\"id\":\"588852e8d2994e3bbfa52da6\"},{\"rating\":39.81979555148656,\"name\":\"Darkatma\",\"rank\":7,\"previous_rank\":7,\"id\":\"587894e9d2994e15c7dea9cd\"},{\"rating\":39.113975744418255,\"name\":\"SAB | Ralph\",\"rank\":8,\"previous_rank\":8,\"id\":\"588852e8d2994e3bbfa52dcf\"}],\"region\":\"norcal\",\"ranking_criteria\":{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":45,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000},\"time\":\"10/31/15\",\"tournaments\":[\"588827bad2994e0d53b14556\",\"58885dced2994e3d5659410e\",\"588828ced2994e0d53b1455b\",\"58ad40a1d2994e756952adc7\",\"58882955d2994e0d53b1455d\",\"5888282dd2994e0d53b14559\",\"58882a16d2994e0d53b1455f\",\"58885e04d2994e3d56594118\",\"5888502cd2994e3bbfa52d56\",\"588850d5d2994e3bbfa52d67\",\"58882a7dd2994e0d53b14574\",\"58882affd2994e0d53b14589\",\"58ad4135d2994e756952adde\",\"58885305d2994e3bbfa52ddb\",\"58882c34d2994e0d53b14593\",\"58898d7bd2994e6f7981b1c6\",\"588a45b2d2994e713ad63cfd\",\"588c5f2fd2994e713ad63d2a\",\"588d0857d2994e713ad63d2e\",\"588ec08fd2994e6485b3d54a\",\"58902ffed2994e04409f19fd\",\"58ad416dd2994e756952ade6\",\"5896d3c2d2994e73ee0b171e\",\"5896deb3d2994e73ee0b172e\",\"5896d44dd2994e73ee0b1720\",\"58ad41e5d2994e756952adf1\",\"589cb48fd2994e4d0f2e2574\",\"58a002c7d2994e4d0f2e258f\",\"589ebbd3d2994e4d0f2e2583\",\"58a00342d2994e4d0f2e2592\",\"58a0047dd2994e4d0f2e2597\",\"58a00514d2994e4d0f2e25a6\",\"58a007f2d2994e4d0f2e25b8\",\"58a2b66ed2994e688c68c9df\",\"58a4cfafd2994e756952ad78\",\"58ad4236d2994e756952adfa\",\"58b34432d2994e05bdebcf76\",\"58a7ee7fd2994e756952ad81\",\"58a7f661d2994e756952ad89\",\"58a9139cd2994e756952ad94\",\"58aa7e8ed2994e756952ada5\",\"58ab7fecd2994e756952adaa\",\"58ad4274d2994e756952adfd\",\"58ad3b1cd2994e756952adba\",\"58b130d8d2994e7265472758\",\"58b13120d2994e7265472760\",\"58b13684d2994e726547277a\",\"58b135efd2994e7265472777\",\"58b13232d2994e726547276e\",\"58b273cfd2994e726547277e\",\"58b3161bd2994e05bdebcf72\",\"58b3cb0ad2994e05bdebcf7b\",\"58ba51d5d2994e5dfee3a946\",\"58ba5259d2994e5dfee3a94d\",\"58bb8fa9d2994e5dfee3a957\",\"58bb90d5d2994e7ff2c4b207\",\"58bfac17d2994e057e91f714\",\"58bfaed4d2994e057e91f71b\",\"58c5d79ad2994e057e91f740\",\"58c5d6bfd2994e057e91f728\",\"58c5d6ffd2994e057e91f735\",\"58c5d742d2994e057e91f73c\",\"58d8bcdfd2994e057e91f7be\",\"58d8be67d2994e057e91f7cc\",\"58d8bea8d2994e057e91f7ce\",\"58c616d9d2994e057e91f747\",\"58ced80fd2994e057e91f763\",\"591014cdd2994e342a462f4e\",\"58ced65bd2994e057e91f751\",\"58d8bf92d2994e057e91f7d0\",\"58d8bffad2994e057e91f7d4\",\"58d8c038d2994e057e91f7d8\",\"58d8c631d2994e057e91f810\",\"58d8c08ad2994e057e91f7de\",\"58d8c0f8d2994e057e91f7e0\",\"58d8c164d2994e057e91f7e9\",\"58d8c1c6d2994e057e91f7f3\",\"58d8ce9dd2994e057e91f815\",\"58d8c55fd2994e057e91f80e\",\"58d8c3e8d2994e057e91f7fd\",\"58e08f2fd2994e39c28082c5\",\"58e08e9dd2994e39c28082bb\",\"58e08fbed2994e39c28082cc\",\"58e1a108d2994e4c48900141\",\"58ea97dbd2994e5bfa64b5c5\",\"58ea985bd2994e5bfa64b5cc\",\"58ea992cd2994e5bfa64b5d3\",\"58ea996ed2994e5bfa64b5d5\",\"58ea99c2d2994e5bfa64b5de\",\"58ea99fad2994e5bfa64b5e0\",\"58ea9a5fd2994e5bfa64b5e5\",\"58f1b3cdd2994e5bfa64b60b\",\"59101466d2994e342a462f46\",\"58f1b356d2994e5bfa64b5f9\",\"58f1b9e6d2994e5bfa64b60d\",\"58f2a72ad2994e5bfa64b610\",\"58f315c1d2994e5bfa64b615\",\"58f70975d2994e5bfa64b622\",\"591013c7d2994e342a462f40\",\"58f709dbd2994e5bfa64b628\",\"58fb52ecd2994e5bfa64b639\",\"58fb5354d2994e5bfa64b63b\",\"590947ded2994e342a462eea\",\"59094be1d2994e342a462efd\",\"59101354d2994e342a462f3c\",\"59094c0ad2994e342a462eff\",\"59094d0dd2994e342a462f09\",\"59094d71d2994e342a462f0d\",\"59094e91d2994e342a462f17\",\"59094fa0d2994e342a462f27\",\"59094fe5d2994e342a462f2c\",\"59101569d2994e342a462f52\",\"59097bd8d2994e342a462f30\",\"591015ebd2994e342a462f60\",\"591016d0d2994e342a462f64\",\"59101772d2994e342a462f6f\",\"59180ee2d2994e47e461afc1\",\"59180f2bd2994e47e461afc6\",\"59180f7cd2994e47e461afcd\",\"59181043d2994e47e461afd7\",\"591810a6d2994e47e461afdf\",\"59213b8dd2994e1d79144936\",\"59213b9fd2994e1d79144938\",\"59213ba9d2994e1d7914493a\",\"59213bb3d2994e1d7914493c\",\"59213bbcd2994e1d7914493e\",\"59213bc5d2994e1d79144940\",\"59213bd5d2994e1d79144942\",\"59251d4ed2994e1d7914496d\",\"59251d5ad2994e1d7914496f\",\"59251d61d2994e1d79144971\",\"59252fa5d2994e1d7914497a\",\"592f97d7d2994e1d7914498c\",\"592f9813d2994e1d7914498e\",\"592f9831d2994e1d79144990\",\"593b206ed2994e34028b4b75\",\"593b214ad2994e34028b4b7e\",\"593b21fad2994e34028b4b84\",\"593b227bd2994e34028b4b93\",\"593b231cd2994e34028b4b9c\",\"593b2363d2994e34028b4ba2\",\"593b23c2d2994e34028b4ba7\",\"59420f9dd2994e34028b4bbe\",\"59420fe3d2994e34028b4bc4\",\"59422f6ed2994e34028b4bdd\",\"5942101cd2994e34028b4bc9\",\"59421065d2994e34028b4bcd\",\"594f4694d2994e34028b4bf6\",\"594f46ddd2994e34028b4bfc\",\"594f4a4fd2994e34028b4c25\",\"594f473fd2994e34028b4c00\",\"594f47c2d2994e34028b4c03\",\"594f49afd2994e34028b4c1c\",\"594f49f7d2994e34028b4c20\",\"594f4a99d2994e34028b4c2a\",\"594f4b2bd2994e34028b4c2e\",\"594f575bd2994e34028b4c3a\",\"597d21d8d2994e34028b4c65\",\"597d220dd2994e34028b4c69\",\"597d23eed2994e34028b4c72\",\"597d2468d2994e34028b4c88\",\"597d24e7d2994e34028b4c9a\",\"597d253ed2994e34028b4c9d\",\"597d2ccad2994e34028b4d00\",\"597d2ee4d2994e34028b4d07\",\"597d2797d2994e34028b4ca8\",\"597d2903d2994e34028b4cc4\",\"597d26b9d2994e34028b4ca6\",\"597d27dad2994e34028b4cad\",\"597d2c69d2994e34028b4cfa\",\"597d2f3dd2994e34028b4d0d\",\"597d2817d2994e34028b4cb7\",\"597d2851d2994e34028b4cbb\",\"597d2c08d2994e34028b4cf5\",\"597d2bdbd2994e34028b4cf2\",\"597d4955d2994e34028b4d13\",\"597d5045d2994e34028b4d1c\",\"5989465cd2994e6b491a3a94\",\"598946c3d2994e6b491a3a9e\",\"59894733d2994e6b491a3aa3\",\"59894752d2994e6b491a3aa6\",\"598947e5d2994e6b491a3aaf\",\"59a1b7a9d2994e6b491a3ad3\",\"59a1b82fd2994e6b491a3ad8\",\"59a1b882d2994e6b491a3adb\",\"59a1b8c5d2994e6b491a3adf\",\"59a1be93d2994e6b491a3b03\",\"59a1bad5d2994e6b491a3ae4\",\"59a1bfa0d2994e6b491a3b0a\",\"59a1bd13d2994e6b491a3afa\",\"59a1bb83d2994e6b491a3aea\",\"59a1bcb8d2994e6b491a3af6\",\"59a1c01dd2994e6b491a3b0c\",\"59a1bc26d2994e6b491a3aef\",\"59a1c06ed2994e6b491a3b0e\",\"59bcbd5dd2994e6b491a3b2f\",\"59bcc9fad2994e6b491a3b75\",\"59bcc09bd2994e6b491a3b33\",\"59bcc101d2994e6b491a3b35\",\"59bcc1f6d2994e6b491a3b3a\",\"59bcc350d2994e6b491a3b40\",\"59bcc3ccd2994e6b491a3b43\",\"59bcc488d2994e6b491a3b4f\",\"59bcc517d2994e6b491a3b56\",\"59bcc601d2994e6b491a3b62\",\"59bcc7e9d2994e6b491a3b67\"],\"id\":\"$RANKINGS_ID_2\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        rankingsBundle = gson.fromJson(JSON_RANKINGS_BUNDLE, RankingsBundle::class.java)
    }

    @Test
    fun testGetNotificationInfo() {
        setHasNetworkConnection(true)
        rankingsPollingPreferenceStore.enabled.set(true)
        rankingsPollingPreferenceStore.rankingsId.delete()

        var pollStatus = rankingsNotificationsUtils.getPollStatus()
        assertEquals(CANCEL, rankingsNotificationsUtils.getNotificationInfo(pollStatus, rankingsBundle))

        rankingsPollingPreferenceStore.rankingsId.set(RANKINGS_ID_1)

        pollStatus = rankingsNotificationsUtils.getPollStatus()
        assertEquals(SHOW, rankingsNotificationsUtils.getNotificationInfo(pollStatus, rankingsBundle))

        rankingsPollingPreferenceStore.rankingsId.set(RANKINGS_ID_2)

        pollStatus = rankingsNotificationsUtils.getPollStatus()
        assertEquals(NO_CHANGE, rankingsNotificationsUtils.getNotificationInfo(pollStatus, rankingsBundle))

        rankingsPollingPreferenceStore.rankingsId.set(RANKINGS_ID_3)

        pollStatus = rankingsNotificationsUtils.getPollStatus()
        assertEquals(SHOW, rankingsNotificationsUtils.getNotificationInfo(pollStatus, rankingsBundle))
    }

    @Test
    fun testGetNotificationInfoWithNulls() {
        assertEquals(CANCEL, rankingsNotificationsUtils.getNotificationInfo(null,
                null))

        assertEquals(CANCEL, rankingsNotificationsUtils.getNotificationInfo(
                PollStatus(null, false, false), null))

        assertEquals(CANCEL, rankingsNotificationsUtils.getNotificationInfo(
                PollStatus(RANKINGS_ID_1, false, false), null))

        assertEquals(CANCEL, rankingsNotificationsUtils.getNotificationInfo(
                PollStatus(null, false, false), null))
    }

    @Test
    fun testGetPollStatus() {
        setHasNetworkConnection(false)
        rankingsPollingPreferenceStore.enabled.set(false)
        rankingsPollingPreferenceStore.lastPoll.delete()
        rankingsPollingPreferenceStore.rankingsId.delete()

        var pollStatus = rankingsNotificationsUtils.getPollStatus()
        assertNull(pollStatus.oldRankingsId)
        assertFalse(pollStatus.proceed)
        assertFalse(pollStatus.retry)

        rankingsPollingPreferenceStore.enabled.set(true)

        pollStatus = rankingsNotificationsUtils.getPollStatus()
        assertNull(pollStatus.oldRankingsId)
        assertFalse(pollStatus.proceed)
        assertFalse(pollStatus.retry)

        rankingsPollingPreferenceStore.rankingsId.set(RANKINGS_ID_1)

        pollStatus = rankingsNotificationsUtils.getPollStatus()
        assertNotNull(pollStatus.oldRankingsId)
        assertEquals(RANKINGS_ID_1, pollStatus.oldRankingsId)
        assertFalse(pollStatus.proceed)
        assertTrue(pollStatus.retry)

        setHasNetworkConnection(true)

        pollStatus = rankingsNotificationsUtils.getPollStatus()
        assertNotNull(pollStatus.oldRankingsId)
        assertEquals(RANKINGS_ID_1, pollStatus.oldRankingsId)
        assertTrue(pollStatus.proceed)
        assertTrue(pollStatus.retry)
    }

    private fun setHasNetworkConnection(hasNetworkConnection: Boolean) {
        if (deviceUtils is TestDeviceUtilsImpl) {
            (deviceUtils as TestDeviceUtilsImpl).setHasNetworkConnection(hasNetworkConnection)
        } else {
            throw IllegalStateException("deviceUtils ($deviceUtils) is not a TestDeviceUtilsImpl")
        }
    }

}
