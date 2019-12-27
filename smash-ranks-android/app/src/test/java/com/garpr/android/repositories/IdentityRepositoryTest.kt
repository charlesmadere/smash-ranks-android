package com.garpr.android.repositories

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.RankedPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IdentityRepositoryTest : BaseTest() {

    protected val identityRepository: IdentityRepository by inject()
    protected val regionRepository: RegionRepository by inject()

    companion object {
        private val LITE_PLAYER = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val RANKED_PLAYER = RankedPlayer(
                id = "583a4a15d2994e0577b05c8a",
                name = "NMW",
                rating = 41.565775187219025f,
                rank = 3,
                previousRank = 3
        )
    }

    @Test
    fun testAddListener() {
        var optional: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable
                .subscribe {
                    optional = it
                }

        assertEquals(false, optional?.isPresent)

        identityRepository.setIdentity(LITE_PLAYER, regionRepository.getRegion())
        assertEquals(LITE_PLAYER, optional?.item)

        identityRepository.setIdentity(LITE_PLAYER, regionRepository.getRegion())
        assertEquals(LITE_PLAYER, optional?.item)

        identityRepository.setIdentity(RANKED_PLAYER, regionRepository.getRegion())
        assertEquals(RANKED_PLAYER, optional?.item)

        identityRepository.removeIdentity()
        assertEquals(false, optional?.isPresent)
    }

    @Test
    fun testGetIdentity() {
        assertNull(identityRepository.identity)
    }

    @Test
    fun testGetAndSetIdentity() {
        assertNull(identityRepository.identity)

        identityRepository.setIdentity(LITE_PLAYER, regionRepository.getRegion())
        assertEquals(LITE_PLAYER, identityRepository.identity)

        identityRepository.setIdentity(RANKED_PLAYER, regionRepository.getRegion())
        assertEquals(RANKED_PLAYER, identityRepository.identity)

        identityRepository.removeIdentity()
        assertNull(identityRepository.identity)
    }

    @Test
    fun testHasIdentity() {
        assertFalse(identityRepository.hasIdentity)

        identityRepository.setIdentity(LITE_PLAYER, regionRepository.getRegion())
        assertTrue(identityRepository.hasIdentity)

        identityRepository.setIdentity(RANKED_PLAYER, regionRepository.getRegion())
        assertTrue(identityRepository.hasIdentity)

        identityRepository.removeIdentity()
        assertFalse(identityRepository.hasIdentity)
    }

    @Test
    fun testIsPlayerWithEmptyString() {
        assertFalse(identityRepository.isPlayer(""))

        identityRepository.setIdentity(LITE_PLAYER, regionRepository.getRegion())
        assertFalse(identityRepository.isPlayer(""))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(""))
    }

    @Test
    fun testIsPlayerWithIdString() {
        assertFalse(identityRepository.isPlayer(LITE_PLAYER.id))
        assertFalse(identityRepository.isPlayer(RANKED_PLAYER.id))

        identityRepository.setIdentity(LITE_PLAYER, regionRepository.getRegion())
        assertTrue(identityRepository.isPlayer(LITE_PLAYER.id))
        assertFalse(identityRepository.isPlayer(RANKED_PLAYER.id))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(LITE_PLAYER.id))
        assertFalse(identityRepository.isPlayer(RANKED_PLAYER.id))
    }

    @Test
    fun testIsPlayerWithNullPlayer() {
        assertFalse(identityRepository.isPlayer(null as AbsPlayer?))

        identityRepository.setIdentity(RANKED_PLAYER, regionRepository.getRegion())
        assertFalse(identityRepository.isPlayer(null as AbsPlayer?))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(null as AbsPlayer?))
    }

    @Test
    fun testIsPlayerWithNullString() {
        assertFalse(identityRepository.isPlayer(null as String?))

        identityRepository.setIdentity(LITE_PLAYER, regionRepository.getRegion())
        assertFalse(identityRepository.isPlayer(null as String?))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(null as String?))
    }

    @Test
    fun testIsPlayerWithPlayer() {
        assertFalse(identityRepository.isPlayer(LITE_PLAYER))

        identityRepository.setIdentity(RANKED_PLAYER, regionRepository.getRegion())
        assertFalse(identityRepository.isPlayer(LITE_PLAYER))
        assertTrue(identityRepository.isPlayer(RANKED_PLAYER))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(LITE_PLAYER))
        assertFalse(identityRepository.isPlayer(RANKED_PLAYER))
    }

    @Test
    fun testIsPlayerWithWhitespaceString() {
        assertFalse(identityRepository.isPlayer(" "))

        identityRepository.setIdentity(LITE_PLAYER, regionRepository.getRegion())
        assertFalse(identityRepository.isPlayer("  "))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer("   "))
    }

}
