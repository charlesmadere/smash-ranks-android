package com.garpr.android.data.database

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DbFavoritePlayerTest : BaseTest() {

    @Test
    fun testConstructFromAbsPlayerWithCharlezard() {
        val charlezard = DbFavoritePlayer(
                player = CHARLEZARD,
                region = NORCAL
        )

        assertEquals(CHARLEZARD.id, charlezard.id)
        assertEquals(CHARLEZARD.name, charlezard.name)
        assertEquals(CHARLEZARD.region, charlezard.region)
    }

    @Test
    fun testConstructFromAbsPlayerWithDruggedfox() {
        val druggedfox = DbFavoritePlayer(
                player = DRUGGEDFOX,
                region = GEORGIA
        )

        assertEquals(DRUGGEDFOX.id, druggedfox.id)
        assertEquals(DRUGGEDFOX.name, druggedfox.name)
        assertEquals(DRUGGEDFOX.region, druggedfox.region)
    }

    @Test
    fun testToFavoritePlayerWithCharlezard() {
        val favoritePlayer = DB_CHARLEZARD.toFavoritePlayer()
        assertEquals(DB_CHARLEZARD.id, favoritePlayer.id)
        assertEquals(DB_CHARLEZARD.name, favoritePlayer.name)
        assertEquals(DB_CHARLEZARD.region, favoritePlayer.region)
    }

    @Test
    fun testToFavoritePlayerWithDruggedfox() {
        val favoritePlayer = DB_DRUGGEDFOX.toFavoritePlayer()
        assertEquals(DB_DRUGGEDFOX.id, favoritePlayer.id)
        assertEquals(DB_DRUGGEDFOX.name, favoritePlayer.name)
        assertEquals(DB_DRUGGEDFOX.region, favoritePlayer.region)
    }

    companion object {
        private val GEORGIA = Region(
                displayName = "Georgia",
                id = "georgia",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val CHARLEZARD = FavoritePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard",
                region = NORCAL
        )

        private val DRUGGEDFOX = FavoritePlayer(
                id = "583a4a15d2994e0577b05c86",
                name = "druggedfox",
                region = GEORGIA
        )

        private val DB_CHARLEZARD = DbFavoritePlayer(
                id = CHARLEZARD.id,
                name = CHARLEZARD.name,
                region = CHARLEZARD.region
        )

        private val DB_DRUGGEDFOX = DbFavoritePlayer(
                id = DRUGGEDFOX.id,
                name = DRUGGEDFOX.name,
                region = DRUGGEDFOX.region
        )
    }

}
