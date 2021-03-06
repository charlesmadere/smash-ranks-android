package com.garpr.android.koin

import com.garpr.android.features.deepLink.DeepLinkViewModel
import com.garpr.android.features.favoritePlayers.AddOrRemovePlayerFromFavoritesViewModel
import com.garpr.android.features.headToHead.HeadToHeadViewModel
import com.garpr.android.features.home.HomeViewModel
import com.garpr.android.features.home.shareRegion.ShareRegionViewModel
import com.garpr.android.features.logViewer.LogViewerViewModel
import com.garpr.android.features.player.PlayerViewModel
import com.garpr.android.features.players.PlayersViewModel
import com.garpr.android.features.rankings.RankingsAndFavoritesViewModel
import com.garpr.android.features.rankings.RankingsViewModel
import com.garpr.android.features.setIdentity.SetIdentityViewModel
import com.garpr.android.features.setRegion.SetRegionViewModel
import com.garpr.android.features.settings.SettingsViewModel
import com.garpr.android.features.splash.SplashScreenViewModel
import com.garpr.android.features.tournament.TournamentViewModel
import com.garpr.android.features.tournaments.TournamentsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {

    viewModel { AddOrRemovePlayerFromFavoritesViewModel(get(), get()) }
    viewModel { DeepLinkViewModel(get(), get(), get(), get()) }
    viewModel { HeadToHeadViewModel(get(), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { LogViewerViewModel(get(), get()) }
    viewModel { PlayerViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { PlayersViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { RankingsAndFavoritesViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { RankingsViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ShareRegionViewModel(get(), get()) }
    viewModel { SetIdentityViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { SetRegionViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SplashScreenViewModel(get(), get(), get(), get(), get()) }
    viewModel { TournamentViewModel(get(), get(), get(), get(), get()) }
    viewModel { TournamentsViewModel(get(), get(), get(), get()) }

}
