package com.garpr.android.features.tournaments

import com.garpr.android.BaseTest
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.TournamentsRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TournamentsViewModelTest : BaseTest() {

    private val tournamentsRepository = TournamentsRepositoryOverride()
    private lateinit var viewModel: TournamentsViewModel

    protected val threadUtils: ThreadUtils by inject()
    protected val timber: Timber by inject()

    companion object {

    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = TournamentsViewModel(threadUtils, timber, tournamentsRepository)
    }



    private class TournamentsRepositoryOverride(
            internal var tournamentsBundle: TournamentsBundle? = null
    ) : TournamentsRepository {
        override fun getTournament(region: Region, tournamentId: String): Single<FullTournament> {
            throw NotImplementedError()
        }

        override fun getTournaments(region: Region): Single<TournamentsBundle> {
            val tournamentsBundle = this.tournamentsBundle

            return if (tournamentsBundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(tournamentsBundle)
            }
        }
    }

}
