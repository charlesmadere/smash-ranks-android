package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.models.Region
import com.google.gson.Gson
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class SmashRosterStorageTest : BaseTest() {

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var smashRosterStorage: SmashRosterStorage

    protected lateinit var region1: Region
    protected lateinit var region2: Region


    companion object {
        private const val PLAYER_ID_CHARLEZARD = "587a951dd2994e15c7dea9fe"
        private const val PLAYER_ID_DJ_NINTENDO = "545b233a8ab65f7a95f74854"
        private const val PLAYER_ID_HAX = "53c64dba8ab65f6e6651f7bc"
        private const val PLAYER_ID_IMYT = "5877eb55d2994e15c7dea98b"
        private const val PLAYER_ID_PEWPEWU = "588852e8d2994e3bbfa52da7"

        private const val REGION_1 = "{\"display_name\":\"Norcal\",\"endpoint\":\"gar_pr\",\"id\":\"norcal\"}"
        private const val REGION_2 = "{\"display_name\":\"New York City\",\"endpoint\":\"not_gar_pr\",\"id\":\"nyc\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        region1 = gson.fromJson(REGION_1, Region::class.java)
        region2 = gson.fromJson(REGION_2, Region::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetSmashCharacterWithCharlezard() {
        assertNull(smashRosterStorage.getSmashCharacter(region1, PLAYER_ID_CHARLEZARD))
        assertNull(smashRosterStorage.getSmashCharacter(region2, PLAYER_ID_CHARLEZARD))
    }

    @Test
    @Throws(Exception::class)
    fun testGetSmashCharacterWithDjNintendo() {
        assertNull(smashRosterStorage.getSmashCharacter(region1, PLAYER_ID_DJ_NINTENDO))
        assertNull(smashRosterStorage.getSmashCharacter(region2, PLAYER_ID_DJ_NINTENDO))
    }

    @Test
    @Throws(Exception::class)
    fun testGetSmashCharacterWithEmptyPlayerId() {
        assertNull(smashRosterStorage.getSmashCharacter(region1, ""))
        assertNull(smashRosterStorage.getSmashCharacter(region2, ""))
    }

    @Test
    @Throws(Exception::class)
    fun testGetSmashCharacterWithHax() {
        assertNull(smashRosterStorage.getSmashCharacter(region1, PLAYER_ID_HAX))
        assertNull(smashRosterStorage.getSmashCharacter(region2, PLAYER_ID_HAX))
    }

    @Test
    @Throws(Exception::class)
    fun testGetSmashCharacterWithImyt() {
        assertNull(smashRosterStorage.getSmashCharacter(region1, PLAYER_ID_IMYT))
        assertNull(smashRosterStorage.getSmashCharacter(region2, PLAYER_ID_IMYT))
    }

    @Test
    @Throws(Exception::class)
    fun testGetSmashCharacterWithPewPewU() {
        assertNull(smashRosterStorage.getSmashCharacter(region1, PLAYER_ID_PEWPEWU))
        assertNull(smashRosterStorage.getSmashCharacter(region2, PLAYER_ID_PEWPEWU))
    }

    @Test
    @Throws(Exception::class)
    fun testGetSmashCharacterWithNullPlayerId() {
        assertNull(smashRosterStorage.getSmashCharacter(region1, null))
        assertNull(smashRosterStorage.getSmashCharacter(region2, null))
    }

    @Test
    @Throws(Exception::class)
    fun testGetSmashCharacterWithWhitespacePlayerId() {
        assertNull(smashRosterStorage.getSmashCharacter(region1, " "))
        assertNull(smashRosterStorage.getSmashCharacter(region2, " "))
    }

}
