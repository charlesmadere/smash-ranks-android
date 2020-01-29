package com.garpr.android.managers

import com.garpr.android.BuildConfig
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.koin.FAVORITE_PLAYERS_KEY_VALUE_STORE
import com.garpr.android.misc.PackageNameProvider
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.test.BaseAndroidTest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.inject
import org.koin.core.qualifier.named

class AppUpgradeManagerTest : BaseAndroidTest() {

    private lateinit var favoritePlayerAdapter: JsonAdapter<FavoritePlayer>
    private lateinit var smashCompetitorAdapter: JsonAdapter<SmashCompetitor>

    protected val appUpgradeManager: AppUpgradeManager by inject()
    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val generalPreferenceStore: GeneralPreferenceStore by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val favoritePlayersKeyValueStore: KeyValueStore by inject(named(FAVORITE_PLAYERS_KEY_VALUE_STORE))
    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()
    protected val moshi: Moshi by inject()
    protected val packageNameProvider: PackageNameProvider by inject()
    protected val smashRosterStorage: SmashRosterStorage by inject()

    @Before
    override fun setUp() {
        super.setUp()

        favoritePlayerAdapter = moshi.adapter(FavoritePlayer::class.java)
        smashCompetitorAdapter = moshi.adapter(SmashCompetitor::class.java)
    }

    @Test
    fun testUpgradeAppFromCurrentVersion() {
        generalPreferenceStore.lastVersion.set(BuildConfig.VERSION_CODE)
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        identityRepository.setIdentity(IMYT, NORCAL)
        appUpgradeManager.upgradeApp()

        var playersSize: Int? = null

        favoritePlayersRepository.sizeObservable.subscribe {
            playersSize = it
        }

        var identity: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable.subscribe {
            identity = it
        }

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertEquals(1, playersSize)
        assertEquals(IMYT, identity?.orNull())
    }

    @Test
    fun testUpgradeAppFromVersion0() {
        generalPreferenceStore.lastVersion.set(0)
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        identityRepository.setIdentity(IMYT, NORCAL)
        appUpgradeManager.upgradeApp()

        var playersSize: Int? = null

        favoritePlayersRepository.sizeObservable.subscribe {
            playersSize = it
        }

        var identity: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable.subscribe {
            identity = it
        }

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertEquals(0, playersSize)
        assertEquals(false, identity?.isPresent())
    }

    @Test
    fun testUpgradeAppFromVersion22002() {
        generalPreferenceStore.lastVersion.set(22002)
        favoritePlayersKeyValueStore.setString(CHARLEZARD.id, favoritePlayerAdapter.toJson(CHARLEZARD))
        favoritePlayersKeyValueStore.setString(IMYT.id, favoritePlayerAdapter.toJson(IMYT))

        appUpgradeManager.upgradeApp()

        assertTrue(favoritePlayersKeyValueStore.all.isNullOrEmpty())

        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        assertEquals(2, players?.size)
        assertEquals(CHARLEZARD, players?.get(0))
        assertEquals(IMYT, players?.get(1))
    }

    @Test
    fun testUpgradeAppFromVersion22003() {
        generalPreferenceStore.lastVersion.set(22003)

        val garPrKeyValueStore = keyValueStoreProvider.getKeyValueStore(
                name = "${packageNameProvider.packageName}.SmashRosterStorage.${Endpoint.GAR_PR}"
        )

        garPrKeyValueStore.setString(MIKKUZ.id, smashCompetitorAdapter.toJson(MIKKUZ))

        val notGarPrKeyValueStore = keyValueStoreProvider.getKeyValueStore(
                name = "${packageNameProvider.packageName}.SmashRosterStorage.${Endpoint.NOT_GAR_PR}"
        )

        notGarPrKeyValueStore.setString(HAX.id, smashCompetitorAdapter.toJson(HAX))

        appUpgradeManager.upgradeApp()

        assertTrue(garPrKeyValueStore.all.isNullOrEmpty())
        assertTrue(notGarPrKeyValueStore.all.isNullOrEmpty())

        assertEquals(MIKKUZ, smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, MIKKUZ.id))
        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.GAR_PR, HAX.id))

        assertNull(smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, MIKKUZ.id))
        assertEquals(HAX, smashRosterStorage.getSmashCompetitor(Endpoint.NOT_GAR_PR, HAX.id))
    }

    companion object {
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

        private val IMYT = FavoritePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt",
                region = NORCAL
        )

        private val HAX = SmashCompetitor(
                id = "53c64dba8ab65f6e6651f7bc",
                name = "Aziz",
                tag = "Hax$"
        )

        private val MIKKUZ = SmashCompetitor(
                id = "583a4a15d2994e0577b05c74",
                name = "Justin",
                tag = "mikkuz"
        )
    }

}
