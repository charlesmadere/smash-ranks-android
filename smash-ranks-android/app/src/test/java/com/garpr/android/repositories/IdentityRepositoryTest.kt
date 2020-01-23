package com.garpr.android.repositories

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.Region
import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.test.inject

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
    fun testGetAndSetIdentity() {
        var value: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable.subscribe {
            value = it
        }

        assertEquals(false, value?.isPresent())

        identityRepository.setIdentity(HMW, NORCAL)
        assertEquals(HMW, value?.orNull())

        identityRepository.setIdentity(NMW, NORCAL)
        assertEquals(NMW, value?.orNull())

        identityRepository.removeIdentity()
        assertEquals(false, value?.isPresent())
    }

    @Test
    fun testHasIdentityObservable() {
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
    fun testIdentityObservable() {
        var value: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable.subscribe {
            value = it
        }

        assertEquals(false, value?.isPresent())

        identityRepository.setIdentity(HMW, NORCAL)
        assertEquals(HMW, value?.orNull())

        identityRepository.setIdentity(HMW, NORCAL)
        assertEquals(HMW, value?.orNull())

        identityRepository.setIdentity(NMW, NORCAL)
        assertEquals(NMW, value?.orNull())

        identityRepository.removeIdentity()
        assertEquals(false, value?.isPresent())
    }

}
