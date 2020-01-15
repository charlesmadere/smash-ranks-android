package com.garpr.android.data.database

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
import org.junit.Assert.assertEquals
import org.junit.Test

class DbFavoritePlayerTest {

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
    }

}
