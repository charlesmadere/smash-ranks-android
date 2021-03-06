package com.garpr.android.sync.roster

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.misc.PackageNameProvider
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.test.BaseAndroidTest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.inject

class SmashRosterStorageTest : BaseAndroidTest() {

    private lateinit var smashCompetitorAdapter: JsonAdapter<SmashCompetitor>
    private lateinit var garPrSmashRoster: Map<String, SmashCompetitor>
    private lateinit var notGarPrSmashRoster: Map<String, SmashCompetitor>

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()
    protected val moshi: Moshi by inject()
    protected val packageNameProvider: PackageNameProvider by inject()
    protected val smashRosterStorage: SmashRosterStorage by inject()

    @Before
    override fun setUp() {
        super.setUp()

        smashCompetitorAdapter = moshi.adapter(SmashCompetitor::class.java)

        val type = Types.newParameterizedType(Map::class.java, String::class.java,
                SmashCompetitor::class.java)
        val smashRosterAdapter: JsonAdapter<Map<String, SmashCompetitor>> = moshi.adapter(type)

        garPrSmashRoster = checkNotNull(smashRosterAdapter.fromJson(JSON_GAR_PR_SMASH_ROSTER))
        notGarPrSmashRoster = checkNotNull(smashRosterAdapter.fromJson(JSON_NOT_GAR_PR_SMASH_ROSTER))
    }

    @Test
    fun testClear() {
        smashRosterStorage.clear()
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_CHARLEZARD))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_HAX))
    }

    @Test
    fun testGetSmashCharacterWithBlankString() {
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, " "))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, " "))
    }

    @Test
    fun testGetSmashCompetitorWithCharlezard() {
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_CHARLEZARD))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_CHARLEZARD))
    }

    @Test
    fun testGetSmashCharacterWithDjNintendo() {
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_DJ_NINTENDO))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_DJ_NINTENDO))
    }

    @Test
    fun testGetSmashCharacterWithEmptyString() {
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, ""))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, ""))
    }

    @Test
    fun testGetSmashCharacterWithHax() {
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_HAX))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_HAX))
    }

    @Test
    fun testGetSmashCharacterWithImyt() {
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_IMYT))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_IMYT))
    }

    @Test
    fun testGetSmashCharacterWithNullPlayerId() {
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, null))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, null))
    }

    @Test
    fun testGetSmashCharacterWithPewPewU() {
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_PEWPEWU))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_PEWPEWU))
    }

    @Test
    fun testGetSmashCharacterWithRishi() {
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_RISHI))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_RISHI))
    }

    @Test
    fun testMigrate() {
        val garPrKeyValueStore = keyValueStoreProvider.getKeyValueStore(
                name = "${packageNameProvider.packageName}.SmashRosterStorage.${Endpoint.GAR_PR}"
        )

        garPrKeyValueStore.setString(PLAYER_ID_CHARLEZARD, smashCompetitorAdapter.toJson(CHARLEZARD))
        garPrKeyValueStore.setString(PLAYER_ID_IMYT, smashCompetitorAdapter.toJson(IMYT))

        val notGarPrKeyValueStore = keyValueStoreProvider.getKeyValueStore(
                name = "${packageNameProvider.packageName}.SmashRosterStorage.${Endpoint.NOT_GAR_PR}"
        )

        notGarPrKeyValueStore.setString(PLAYER_ID_HAX, smashCompetitorAdapter.toJson(HAX))

        smashRosterStorage.migrate()

        assertTrue(garPrKeyValueStore.all.isNullOrEmpty())
        assertTrue(notGarPrKeyValueStore.all.isNullOrEmpty())

        assertEquals(CHARLEZARD, smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_CHARLEZARD))
        assertEquals(IMYT, smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_IMYT))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_HAX))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_CHARLEZARD))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_IMYT))
        assertEquals(HAX, smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_HAX))
    }

    @Test
    fun testWriteToStorageWithEmpty() {
        smashRosterStorage.writeToStorage(Endpoint.GAR_PR, emptyMap())
        smashRosterStorage.writeToStorage(Endpoint.NOT_GAR_PR, emptyMap())

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_CHARLEZARD))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_CHARLEZARD))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_IMYT))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_IMYT))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_RISHI))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_RISHI))
    }

    @Test
    fun testWriteToStorageWithGarPrRoster() {
        smashRosterStorage.writeToStorage(Endpoint.GAR_PR, garPrSmashRoster)

        assertNotNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_CHARLEZARD))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_CHARLEZARD))

        assertNotNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_IMYT))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_IMYT))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_RISHI))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_RISHI))
    }

    @Test
    fun testWriteToStorageWithNotGarPrRoster() {
        smashRosterStorage.writeToStorage(Endpoint.NOT_GAR_PR, notGarPrSmashRoster)

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_CHARLEZARD))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_CHARLEZARD))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_IMYT))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_IMYT))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_RISHI))
        assertNotNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_RISHI))
    }

    @Test
    fun testWriteToStorageWithGarPrRosterAndNotGarPrRoster() {
        smashRosterStorage.writeToStorage(Endpoint.GAR_PR, garPrSmashRoster)
        smashRosterStorage.writeToStorage(Endpoint.NOT_GAR_PR, notGarPrSmashRoster)

        assertNotNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_CHARLEZARD))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_CHARLEZARD))

        assertNotNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_IMYT))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_IMYT))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_RISHI))
        assertNotNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_RISHI))
    }

    @Test
    fun testWriteToStorageWithNull() {
        smashRosterStorage.writeToStorage(Endpoint.GAR_PR, null)
        smashRosterStorage.writeToStorage(Endpoint.NOT_GAR_PR, null)

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_CHARLEZARD))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_CHARLEZARD))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_IMYT))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_IMYT))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, PLAYER_ID_RISHI))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, PLAYER_ID_RISHI))
    }

    companion object {
        private const val PLAYER_ID_CHARLEZARD = "587a951dd2994e15c7dea9fe"
        private const val PLAYER_ID_DJ_NINTENDO = "545b233a8ab65f7a95f74854"
        private const val PLAYER_ID_HAX = "53c64dba8ab65f6e6651f7bc"
        private const val PLAYER_ID_IMYT = "5877eb55d2994e15c7dea98b"
        private const val PLAYER_ID_PEWPEWU = "588852e8d2994e3bbfa52da7"
        private const val PLAYER_ID_RISHI = "5778339fe592575dfd89bd0e"

        private val CHARLEZARD = SmashCompetitor(
                id = PLAYER_ID_CHARLEZARD,
                name = "Charles",
                tag = "Charlezard"
        )

        private val HAX = SmashCompetitor(
                id = PLAYER_ID_HAX,
                name = "Aziz",
                tag = "Hax$"
        )

        private val IMYT = SmashCompetitor(
                id = PLAYER_ID_IMYT,
                name = "Declan",
                tag = "Imyt"
        )

        private const val JSON_GAR_PR_SMASH_ROSTER = "{\"5888542ad2994e3bbfa52de4\":{\"name\":\"Leon Zhou\",\"tag\":\"ycz6\",\"mains\":[\"sam\"],\"websites\":{\"twitter\":\"https://twitter.com/ycz6\"},\"id\":\"5888542ad2994e3bbfa52de4\"},\"587a951dd2994e15c7dea9fe\":{\"name\":\"Charles Madere\",\"tag\":\"Charlezard\",\"mains\":[\"shk\"],\"websites\":{\"twitch\":\"https://www.twitch.tv/chillinwithcharles\",\"twitter\":\"https://twitter.com/charlesmadere\",\"other\":\"https://github.com/charlesmadere\"},\"avatar\":{\"original\":\"avatars/587a951dd2994e15c7dea9fe/original.jpg\",\"small\":\"avatars/587a951dd2994e15c7dea9fe/small.jpg\",\"medium\":\"avatars/587a951dd2994e15c7dea9fe/medium.jpg\",\"large\":\"avatars/587a951dd2994e15c7dea9fe/large.jpg\"},\"id\":\"587a951dd2994e15c7dea9fe\"},\"588999c5d2994e713ad63c6f\":{\"name\":\"Vincent Chan\",\"tag\":\"Pimp Jong Illest\",\"mains\":[\"fox\",\"ics\",\"dnk\"],\"websites\":{\"twitch\":\"https://www.twitch.tv/commonyoshii\",\"twitter\":\"https://twitter.com/Pimp_Jong_Illst\",\"youtube\":\"https://www.youtube.com/watch?v=-iDtR1yKJM0\",\"other\":\"https://www.youtube.com/watch?v=oX-hCzATFDQ\"},\"avatar\":{\"original\":\"avatars/588999c5d2994e713ad63c6f/original.jpg\",\"small\":\"avatars/588999c5d2994e713ad63c6f/small.jpg\",\"medium\":\"avatars/588999c5d2994e713ad63c6f/medium.jpg\",\"large\":\"avatars/588999c5d2994e713ad63c6f/large.jpg\"},\"id\":\"588999c5d2994e713ad63c6f\"},\"5877eb55d2994e15c7dea98b\":{\"name\":\"Declan Doyle\",\"tag\":\"Imyt\",\"mains\":[\"shk\",\"fox\",\"doc\"],\"websites\":{\"twitch\":\"https://www.twitch.tv/imyt\",\"twitter\":\"https://twitter.com/OnlyImyt\"},\"avatar\":{\"original\":\"avatars/5877eb55d2994e15c7dea98b/original.jpg\",\"small\":\"avatars/5877eb55d2994e15c7dea98b/small.jpg\",\"medium\":\"avatars/5877eb55d2994e15c7dea98b/medium.jpg\"},\"id\":\"5877eb55d2994e15c7dea98b\"},\"588999c5d2994e713ad63b35\":{\"name\":\"Scott\",\"tag\":\"dimsum\",\"mains\":[\"fox\",\"ics\"],\"avatar\":{\"original\":\"avatars/588999c5d2994e713ad63b35/original.jpg\",\"small\":\"avatars/588999c5d2994e713ad63b35/small.jpg\",\"medium\":\"avatars/588999c5d2994e713ad63b35/medium.jpg\",\"large\":\"avatars/588999c5d2994e713ad63b35/large.jpg\"},\"id\":\"588999c5d2994e713ad63b35\"}}"
        private const val JSON_NOT_GAR_PR_SMASH_ROSTER = "{\"5778339fe592575dfd89bd0e\":{\"name\":\"Rishi Fishi\",\"tag\":\"Rishi\",\"mains\":[\"mrt\"],\"websites\":{\"twitch\":\"https://www.twitch.tv/smashg0d\",\"twitter\":\"https://twitter.com/SmashG0D\"},\"id\":\"5778339fe592575dfd89bd0e\"}}"
    }

}
