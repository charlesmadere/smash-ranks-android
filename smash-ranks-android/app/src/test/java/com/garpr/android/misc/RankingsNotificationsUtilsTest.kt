package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RankingsNotificationsUtilsTest : BaseTest() {

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var rankingsNotificationsUtils: RankingsNotificationsUtils


    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testGetNotificationInfoWithNulls() {
        assertEquals(RankingsNotificationsUtils.Info.CANCEL, rankingsNotificationsUtils
                .getNotificationInfo(null, null, null))
    }

}
