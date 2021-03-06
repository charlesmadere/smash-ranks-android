package com.garpr.android.features.deepLink

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.garpr.android.R
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.deepLink.DeepLinkViewModel.Breadcrumb
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.features.players.PlayersActivity
import com.garpr.android.features.rankings.RankingsActivity
import com.garpr.android.features.tournament.TournamentActivity
import com.garpr.android.features.tournaments.TournamentsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeepLinkActivity : BaseActivity() {

    private val viewModel: DeepLinkViewModel by viewModel()

    companion object {
        private const val TAG = "DeepLinkActivity"
    }

    override val activityName = TAG

    private fun error() {
        Toast.makeText(this, R.string.error_loading_deep_link_data, Toast.LENGTH_LONG).show()
        startActivity(HomeActivity.getLaunchIntent(context = this))
        supportFinishAfterTransition()
    }

    private fun fetchBreadcrumbs() {
        viewModel.fetchBreadcrumbs()
    }

    private fun initListeners() {
        viewModel.breadcrumbsLiveData.observe(this, Observer {
            processBreadcrumbs(it)
        })

        viewModel.networkErrorLiveData.observe(this, Observer {
            error()
        })

        viewModel.urlParseErrorLiveData.observe(this, Observer {
            error()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link)
        viewModel.initialize(intent?.dataString)
        initListeners()
        fetchBreadcrumbs()
    }

    private fun processBreadcrumbs(breadcrumbs: List<Breadcrumb>) {
        val intents = mutableListOf<Intent>()

        breadcrumbs.forEach {
            intents.add(when (it) {
                is Breadcrumb.Home -> {
                    HomeActivity.getLaunchIntent(
                            context = this,
                            initialPosition = it.initialPosition
                    )
                }

                is Breadcrumb.Player -> {
                    PlayerActivity.getLaunchIntent(
                            context = this,
                            playerId = it.playerId,
                            region = it.region
                    )
                }

                is Breadcrumb.Players -> {
                    PlayersActivity.getLaunchIntent(
                            context = this,
                            region = it.region
                    )
                }

                is Breadcrumb.Rankings -> {
                    RankingsActivity.getLaunchIntent(
                            context = this,
                            region = it.region
                    )
                }

                is Breadcrumb.Tournament -> {
                    TournamentActivity.getLaunchIntent(
                            context = this,
                            tournamentId = it.tournamentId,
                            region = it.region
                    )
                }

                is Breadcrumb.Tournaments -> {
                    TournamentsActivity.getLaunchIntent(
                            context = this,
                            region = it.region
                    )
                }
            })
        }

        if (breadcrumbs.isEmpty()) {
            startActivity(HomeActivity.getLaunchIntent(context = this))
        } else {
            ContextCompat.startActivities(this, intents.toTypedArray())
        }

        supportFinishAfterTransition()
    }

}
