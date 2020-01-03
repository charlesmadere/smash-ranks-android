package com.garpr.android.repositories

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.Region
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IdentityRepositoryTest : BaseTest() {

    protected val identityRepository: IdentityRepository by inject()

    companion object {
        private val HMW = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val NMW = RankedPlayer(
                id = "583a4a15d2994e0577b05c8a",
                name = "NMW",
                rating = 41.565775187219025f,
                rank = 3,
                previousRank = 3
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Test
    fun testAddListener() {
        var value: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable.subscribe {
            value = it
        }

        assertEquals(false, value?.isPresent)

        identityRepository.setIdentity(HMW, NORCAL)
        assertEquals(HMW, value?.item)

        identityRepository.setIdentity(HMW, NORCAL)
        assertEquals(HMW, value?.item)

        identityRepository.setIdentity(NMW, NORCAL)
        assertEquals(NMW, value?.item)

        identityRepository.removeIdentity()
        assertEquals(false, value?.isPresent)
    }

    @Test
    fun testGetAndSetIdentity() {
        var value: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable.subscribe {
            value = it
        }

        assertEquals(false, value?.isPresent)

        identityRepository.setIdentity(HMW, NORCAL)
        assertEquals(HMW, value?.item)

        identityRepository.setIdentity(NMW, NORCAL)
        assertEquals(NMW, value?.item)

        identityRepository.removeIdentity()
        assertEquals(false, value?.isPresent)
    }

    @Test
    fun testHasIdentity() {
        var value: Boolean? = null

        identityRepository.hasIdentityObservable.subscribe {
            value = it
        }

        assertEquals(false, value)

        identityRepository.setIdentity(HMW, NORCAL)
        assertEquals(true, value)

        identityRepository.setIdentity(NMW, NORCAL)
        assertEquals(true, value)

        identityRepository.removeIdentity()
        assertEquals(false, value)
    }

    @Test
    fun testIsPlayerWithNullPlayer() {
        assertFalse(identityRepository.isPlayer(null as AbsPlayer?))

        identityRepository.setIdentity(NMW, NORCAL)
        assertFalse(identityRepository.isPlayer(null as AbsPlayer?))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(null as AbsPlayer?))
    }

    @Test
    fun testIsPlayerWithPlayer() {
        assertFalse(identityRepository.isPlayer(HMW))

        identityRepository.setIdentity(NMW, NORCAL)
        assertFalse(identityRepository.isPlayer(HMW))
        assertTrue(identityRepository.isPlayer(NMW))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(HMW))
        assertFalse(identityRepository.isPlayer(NMW))
    }

}
