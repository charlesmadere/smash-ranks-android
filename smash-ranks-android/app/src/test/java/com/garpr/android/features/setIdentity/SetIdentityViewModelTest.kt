package com.garpr.android.features.setIdentity

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.features.setIdentity.SetIdentityViewModel.ListItem
import com.garpr.android.features.setIdentity.SetIdentityViewModel.SaveIconStatus
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayersRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SetIdentityViewModelTest : BaseTest() {

    private val playersRepository = PlayersRepositoryOverride()
    private lateinit var viewModel: SetIdentityViewModel

    protected val identityRepository: IdentityRepository by inject()
    protected val threadUtils: ThreadUtils by inject()
    protected val timber: Timber by inject()

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val IMYT: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val INSTANT: AbsPlayer = LitePlayer(
                id = "5a726ab6d2994e350f044054",
                name = "Instant"
        )

        private val WEEDLORD: AbsPlayer = LitePlayer(
                id = "588852e8d2994e3bbfa52dcb",
                name = "420XX | w33dL0rd840"
        )

        private val KIM: AbsPlayer = LitePlayer(
                id = "588999c5d2994e713ad63c9b",
                name = "\$\$\$ | Kim\$\$\$"
        )

        private val EMPTY_PLAYERS_BUNDLE = PlayersBundle()

        private val PLAYERS_BUNDLE = PlayersBundle(
                players = listOf(CHARLEZARD, IMYT, INSTANT, WEEDLORD, KIM)
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = SetIdentityViewModel(identityRepository, playersRepository, threadUtils, timber)
    }

    @Test
    fun testFetchPlayers() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        assertNull(state?.selectedIdentity)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(false, state?.isRefreshEnabled)
        assertEquals(true, state?.showSearchIcon)
        assertEquals(9, state?.list?.size)
        assertNull(state?.searchResults)
        assertEquals(SaveIconStatus.DISABLED, state?.saveIconStatus)

        assertTrue(state?.list?.get(0) is ListItem.Divider.Letter)
        var letter = state?.list?.get(0) as ListItem.Divider.Letter
        assertEquals(CHARLEZARD.name.substring(0, 1), letter.letter)

        assertTrue(state?.list?.get(1) is ListItem.Player)
        var player = state?.list?.get(1) as ListItem.Player
        assertEquals(CHARLEZARD, player.player)

        assertTrue(state?.list?.get(2) is ListItem.Divider.Letter)
        letter = state?.list?.get(2) as ListItem.Divider.Letter
        assertEquals(IMYT.name.substring(0, 1), letter.letter)

        assertTrue(state?.list?.get(3) is ListItem.Player)
        player = state?.list?.get(3) as ListItem.Player
        assertEquals(IMYT, player.player)

        assertTrue(state?.list?.get(4) is ListItem.Player)
        player = state?.list?.get(4) as ListItem.Player
        assertEquals(INSTANT, player.player)

        assertTrue(state?.list?.get(5) is ListItem.Divider.Digit)

        assertTrue(state?.list?.get(6) is ListItem.Player)
        player = state?.list?.get(6) as ListItem.Player
        assertEquals(WEEDLORD, player.player)

        assertTrue(state?.list?.get(7) is ListItem.Divider.Other)

        assertTrue(state?.list?.get(8) is ListItem.Player)
        player = state?.list?.get(8) as ListItem.Player
        assertEquals(KIM, player.player)
    }

    @Test
    fun testFetchPlayersWithEmptyPlayersBundle() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        playersRepository.playersBundle = EMPTY_PLAYERS_BUNDLE
        viewModel.fetchPlayers(NORCAL)
        assertNull(state?.selectedIdentity)
        assertEquals(false, state?.hasError)
        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(true, state?.isRefreshEnabled)
        assertEquals(false, state?.showSearchIcon)
        assertTrue(state?.list.isNullOrEmpty())
        assertNull(state?.searchResults)
        assertEquals(SaveIconStatus.DISABLED, state?.saveIconStatus)
    }

    @Test
    fun testFetchPlayersWithNullPlayersBundle() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        playersRepository.playersBundle = null
        viewModel.fetchPlayers(NORCAL)
        assertNull(state?.selectedIdentity)
        assertEquals(true, state?.hasError)
        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(true, state?.isRefreshEnabled)
        assertEquals(false, state?.showSearchIcon)
        assertTrue(state?.list.isNullOrEmpty())
        assertNull(state?.searchResults)
        assertEquals(SaveIconStatus.GONE, state?.saveIconStatus)
    }

    @Test
    fun testSaveIconStatusWithEmptyIdentityRepository() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.selectedIdentity = INSTANT
        assertEquals(SaveIconStatus.ENABLED, state?.saveIconStatus)

        viewModel.selectedIdentity = null
        assertEquals(SaveIconStatus.DISABLED, state?.saveIconStatus)
    }

    @Test
    fun testSaveIconStatusWithWeedlordInIdentityRepository() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        identityRepository.setIdentity(WEEDLORD, NORCAL)
        viewModel.fetchPlayers(NORCAL)
        viewModel.selectedIdentity = CHARLEZARD
        assertEquals(SaveIconStatus.ENABLED, state?.saveIconStatus)

        viewModel.selectedIdentity = null
        assertEquals(SaveIconStatus.ENABLED, state?.saveIconStatus)

        viewModel.selectedIdentity = WEEDLORD
        assertEquals(SaveIconStatus.DISABLED, state?.saveIconStatus)

        viewModel.selectedIdentity = IMYT
        assertEquals(SaveIconStatus.ENABLED, state?.saveIconStatus)

        viewModel.selectedIdentity = null
        assertEquals(SaveIconStatus.ENABLED, state?.saveIconStatus)
    }

    @Test
    fun testSaveSelectedIdentity() {
        viewModel.fetchPlayers(NORCAL)
        viewModel.selectedIdentity = CHARLEZARD

        var throwable: Throwable? = null
        var listenerNotified = false

        identityRepository.addListener(object : IdentityRepository.OnIdentityChangeListener {
            override fun onIdentityChange(identityRepository: IdentityRepository) {
                listenerNotified = true
            }
        })

        try {
            viewModel.saveSelectedIdentity(NORCAL)
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(throwable)
        assertTrue(listenerNotified)
        assertEquals(CHARLEZARD, identityRepository.identity)
    }

    @Test
    fun testSaveSelectedIdentityWithCharlezardInIdentityRepository() {
        identityRepository.setIdentity(CHARLEZARD, NORCAL)
        viewModel.fetchPlayers(NORCAL)
        viewModel.selectedIdentity = WEEDLORD

        var throwable: Throwable? = null
        var listenerNotified = false

        identityRepository.addListener(object : IdentityRepository.OnIdentityChangeListener {
            override fun onIdentityChange(identityRepository: IdentityRepository) {
                listenerNotified = true
            }
        })

        try {
            viewModel.saveSelectedIdentity(NORCAL)
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(throwable)
        assertTrue(listenerNotified)
        assertEquals(WEEDLORD, identityRepository.identity)
    }

    @Test
    fun testSaveSelectedIdentityWithNoIdentityThrowsException() {
        var throwable: Throwable? = null

        try {
            viewModel.saveSelectedIdentity(NORCAL)
        } catch (t: Throwable) {
            throwable = t
        }

        assertNotNull(throwable)
    }

    @Test
    fun testSearchWithA() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search("a")
        assertEquals(9, state?.list?.size)
        assertEquals(4, state?.searchResults?.size)

        assertTrue(state?.searchResults?.get(0) is ListItem.Divider.Letter)
        var letter = state?.searchResults?.get(0) as ListItem.Divider.Letter
        assertEquals(CHARLEZARD.name.substring(0, 1), letter.letter)

        assertTrue(state?.searchResults?.get(1) is ListItem.Player)
        var player = state?.searchResults?.get(1) as ListItem.Player
        assertEquals(CHARLEZARD, player.player)

        assertTrue(state?.searchResults?.get(2) is ListItem.Divider.Letter)
        letter = state?.searchResults?.get(2) as ListItem.Divider.Letter
        assertEquals(INSTANT.name.substring(0, 1), letter.letter)

        assertTrue(state?.searchResults?.get(3) is ListItem.Player)
        player = state?.searchResults?.get(3) as ListItem.Player
        assertEquals(INSTANT, player.player)
    }

    @Test
    fun testSearchWithBlankString() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search(" ")
        assertEquals(9, state?.list?.size)
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithEmptyString() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search("")
        assertEquals(9, state?.list?.size)
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithI() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search("i")
        assertEquals(9, state?.list?.size)
        assertEquals(5, state?.searchResults?.size)

        assertTrue(state?.searchResults?.get(0) is ListItem.Divider.Letter)
        val letter = state?.searchResults?.get(0) as ListItem.Divider.Letter
        assertEquals(IMYT.name.substring(0, 1), letter.letter)

        assertTrue(state?.searchResults?.get(1) is ListItem.Player)
        var player = state?.searchResults?.get(1) as ListItem.Player
        assertEquals(IMYT, player.player)

        assertTrue(state?.searchResults?.get(2) is ListItem.Player)
        player = state?.searchResults?.get(2) as ListItem.Player
        assertEquals(INSTANT, player.player)

        assertTrue(state?.searchResults?.get(3) is ListItem.Divider.Other)

        assertTrue(state?.searchResults?.get(4) is ListItem.Player)
        player = state?.searchResults?.get(4) as ListItem.Player
        assertEquals(KIM, player.player)
    }

    @Test
    fun testSearchWithM() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search("m")
        assertEquals(9, state?.list?.size)
        assertEquals(4, state?.searchResults?.size)

        assertTrue(state?.searchResults?.get(0) is ListItem.Divider.Letter)
        val letter = state?.searchResults?.get(0) as ListItem.Divider.Letter
        assertEquals(IMYT.name.substring(0, 1), letter.letter)

        assertTrue(state?.searchResults?.get(1) is ListItem.Player)
        var player = state?.searchResults?.get(1) as ListItem.Player
        assertEquals(IMYT, player.player)

        assertTrue(state?.searchResults?.get(2) is ListItem.Divider.Other)

        assertTrue(state?.searchResults?.get(3) is ListItem.Player)
        player = state?.searchResults?.get(3) as ListItem.Player
        assertEquals(KIM, player.player)
    }

    @Test
    fun testSearchWithNullString() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search(null)
        assertEquals(9, state?.list?.size)
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithQ() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search("q")
        assertEquals(9, state?.list?.size)
        assertEquals(true, state?.searchResults?.isEmpty())
    }

    @Test
    fun testSetSelectedIdentityWithImytInIdentityRepository() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        identityRepository.setIdentity(IMYT, NORCAL)
        viewModel.fetchPlayers(NORCAL)
        assertNull(state?.selectedIdentity)
        assertEquals(IMYT, viewModel.selectedIdentity)

        viewModel.selectedIdentity = CHARLEZARD
        assertEquals(CHARLEZARD, state?.selectedIdentity)
        assertEquals(CHARLEZARD, viewModel.selectedIdentity)

        viewModel.selectedIdentity = null
        assertNull(state?.selectedIdentity)
        assertEquals(IMYT, viewModel.selectedIdentity)
    }

    @Test
    fun testSetSelectedIdentityWithEmptyIdentityRepository() {
        var state: SetIdentityViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        assertNull(state?.selectedIdentity)
        assertNull(viewModel.selectedIdentity)

        viewModel.selectedIdentity = IMYT
        assertEquals(IMYT, state?.selectedIdentity)
        assertEquals(IMYT, viewModel.selectedIdentity)

        viewModel.selectedIdentity = null
        assertNull(state?.selectedIdentity)
        assertNull(viewModel.selectedIdentity)
    }

    @Test
    fun testWarnBeforeCloseAfterFetchingPlayers() {
        viewModel.fetchPlayers(NORCAL)
        assertFalse(viewModel.warnBeforeClose)
    }

    @Test
    fun testWarnBeforeCloseInitialValue() {
        assertFalse(viewModel.warnBeforeClose)
    }

    @Test
    fun testWarnBeforeCloseWithSelectedIdentity() {
        viewModel.fetchPlayers(NORCAL)
        viewModel.selectedIdentity = WEEDLORD
        assertTrue(viewModel.warnBeforeClose)
    }

    @Test
    fun testWarnBeforeCloseWithSelectedIdentitySameAsIdentityInIdentityRepository() {
        identityRepository.setIdentity(IMYT, NORCAL)
        viewModel.fetchPlayers(NORCAL)
        viewModel.selectedIdentity = IMYT
        assertFalse(viewModel.warnBeforeClose)
    }

    private class PlayersRepositoryOverride(
            internal var playersBundle: PlayersBundle? = PLAYERS_BUNDLE
    ) : PlayersRepository {

        override fun getPlayer(region: Region, playerId: String): Single<FullPlayer> {
            throw NotImplementedError()
        }

        override fun getPlayers(region: Region): Single<PlayersBundle> {
            val playersBundle = this.playersBundle

            return if (playersBundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(playersBundle)
            }
        }

    }

}