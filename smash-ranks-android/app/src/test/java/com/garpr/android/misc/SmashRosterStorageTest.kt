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
    fun testGetSmashCharacterWithEmptyPlayerId() {
        assertNull(smashRosterStorage.getSmashCharacter(region1, ""))
        assertNull(smashRosterStorage.getSmashCharacter(region2, ""))
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
