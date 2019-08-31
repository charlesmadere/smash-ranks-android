package com.garpr.android.features.player

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayerMatchesRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PlayerViewModelTest : BaseTest() {

    private lateinit var viewModel: PlayerViewModel

    @Inject
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository

    @Inject
    protected lateinit var identityRepository: IdentityRepository

    @Inject
    protected lateinit var playerMatchesRepository: PlayerMatchesRepository

    @Inject
    protected lateinit var smashRosterStorage: SmashRosterStorage

    @Inject
    protected lateinit var smashRosterSyncManager: SmashRosterSyncManager

    @Inject
    protected lateinit var threadUtils: ThreadUtils

    @Inject
    protected lateinit var timber: Timber

    companion object {
        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private const val CHARLEZARD_ID = "587a951dd2994e15c7dea9fe"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        viewModel = PlayerViewModel(favoritePlayersRepository, identityRepository,
                playerMatchesRepository, smashRosterStorage, smashRosterSyncManager,
                threadUtils, timber)
        viewModel.initialize(NORCAL, CHARLEZARD_ID)
    }

    @Test
    fun testSearchWithEmptyString() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        viewModel.stateLiveData.observeForever {

        }

        viewModel.search("")
    }

    @Test
    fun testSearchWithNullString() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        viewModel.search(null)
    }

}
