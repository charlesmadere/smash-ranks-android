package com.garpr.android.features.home

import android.content.Context
import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

@RunWith(RobolectricTestRunner::class)
class HomeActivityTest : BaseTest() {

    protected val context: Context by inject()

    companion object {
        private val EXTRA_INITIAL_POSITION: String by lazy {
            val c = HomeActivity.Companion::class
            val property = c.declaredMemberProperties.first { it.name == "EXTRA_INITIAL_POSITION" }
            property.isAccessible = true
            property.get(HomeActivity.Companion) as String
        }
    }

    @Test
    fun testGetLaunchIntentWithInitialPosition() {
        val intent = HomeActivity.getLaunchIntent(
                context = context,
                initialPosition = HomeTab.TOURNAMENTS
        )
        assertTrue(intent.hasExtra(EXTRA_INITIAL_POSITION))
        assertEquals(HomeTab.TOURNAMENTS, intent.getParcelableExtra(EXTRA_INITIAL_POSITION))
    }

    @Test
    fun testGetLaunchIntentWithInitialPositionAndRestartActivityTask() {
        val intent = HomeActivity.getLaunchIntent(
                context = context,
                initialPosition = HomeTab.FAVORITE_PLAYERS,
                restartActivityTask = true
        )
        assertTrue(intent.hasExtra(EXTRA_INITIAL_POSITION))
        assertEquals(HomeTab.FAVORITE_PLAYERS, intent.getParcelableExtra(EXTRA_INITIAL_POSITION))
    }

    @Test
    fun testGetLaunchIntentWithNoInitialPosition() {
        val intent = HomeActivity.getLaunchIntent(context)
        assertFalse(intent.hasExtra(EXTRA_INITIAL_POSITION))
        assertNull(intent.getParcelableExtra(EXTRA_INITIAL_POSITION))
    }

}
