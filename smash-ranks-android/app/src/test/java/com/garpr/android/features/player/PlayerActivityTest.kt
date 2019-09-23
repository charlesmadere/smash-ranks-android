package com.garpr.android.features.player

import android.content.Context
import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.activities.BaseActivity
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
class PlayerActivityTest : BaseTest() {

    protected val context: Context by inject()

    companion object {
        private val EXTRA_PLAYER_ID: String by lazy {
            val c = PlayerActivity.Companion::class
            val property = c.declaredMemberProperties.first { it.name == "EXTRA_PLAYER_ID" }
            property.isAccessible = true
            property.get(PlayerActivity.Companion) as String
        }

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val NYC = Region(
                displayName = "New York City",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val ABS_PLAYER: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val FAV_PLAYER = FavoritePlayer(
                id = ABS_PLAYER.id,
                name = ABS_PLAYER.name,
                region = NORCAL
        )
    }

    @Test
    fun testGetLaunchIntentWithAbsPlayer() {
        val intent = PlayerActivity.getLaunchIntent(
                context = context,
                player = ABS_PLAYER
        )

        assertTrue(intent.hasExtra(EXTRA_PLAYER_ID))
        assertEquals(ABS_PLAYER.id, intent.getStringExtra(EXTRA_PLAYER_ID))

        assertFalse(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertNull(intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

    @Test
    fun testGetLaunchIntentWithAbsPlayerAndRegion() {
        val intent = PlayerActivity.getLaunchIntent(
                context = context,
                player = ABS_PLAYER,
                region = NORCAL
        )

        assertTrue(intent.hasExtra(EXTRA_PLAYER_ID))
        assertEquals(ABS_PLAYER.id, intent.getStringExtra(EXTRA_PLAYER_ID))

        assertTrue(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertEquals(NORCAL, intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

    @Test
    fun testGetLaunchIntentWithFavPlayer() {
        val intent = PlayerActivity.getLaunchIntent(
                context = context,
                player = FAV_PLAYER
        )

        assertTrue(intent.hasExtra(EXTRA_PLAYER_ID))
        assertEquals(FAV_PLAYER.id, intent.getStringExtra(EXTRA_PLAYER_ID))

        assertTrue(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertEquals(NORCAL, intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

    @Test
    fun testGetLaunchIntentWithFavPlayerAndRegion() {
        val intent = PlayerActivity.getLaunchIntent(
                context = context,
                player = FAV_PLAYER,
                region = NYC
        )

        assertTrue(intent.hasExtra(EXTRA_PLAYER_ID))
        assertEquals(FAV_PLAYER.id, intent.getStringExtra(EXTRA_PLAYER_ID))

        assertTrue(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertEquals(NORCAL, intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

    @Test
    fun testGetLaunchIntentWithPlayerId() {
        val intent = PlayerActivity.getLaunchIntent(
                context = context,
                playerId = ABS_PLAYER.id
        )

        assertTrue(intent.hasExtra(EXTRA_PLAYER_ID))
        assertEquals(ABS_PLAYER.id, intent.getStringExtra(EXTRA_PLAYER_ID))

        assertFalse(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertNull(intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

    @Test
    fun testGetLaunchIntentWithPlayerIdAndRegion() {
        val intent = PlayerActivity.getLaunchIntent(
                context = context,
                playerId = ABS_PLAYER.id,
                region = NORCAL
        )

        assertTrue(intent.hasExtra(EXTRA_PLAYER_ID))
        assertEquals(ABS_PLAYER.id, intent.getStringExtra(EXTRA_PLAYER_ID))

        assertTrue(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertEquals(NORCAL, intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

}
