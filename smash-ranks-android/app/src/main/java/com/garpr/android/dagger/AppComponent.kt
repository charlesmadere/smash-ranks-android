package com.garpr.android.dagger

import com.garpr.android.App
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.common.views.IdentityConstraintLayout
import com.garpr.android.features.common.views.PaletteSimpleDraweeView
import com.garpr.android.features.common.views.TintedImageView
import com.garpr.android.features.deepLink.DeepLinkViewModel
import com.garpr.android.features.favoritePlayers.AddOrRemovePlayerFromFavoritesViewModel
import com.garpr.android.features.favoritePlayers.FavoritePlayerItemView
import com.garpr.android.features.favoritePlayers.FavoritePlayersViewModel
import com.garpr.android.features.headToHead.HeadToHeadActivity
import com.garpr.android.features.headToHead.HeadToHeadDialogFragment
import com.garpr.android.features.headToHead.HeadToHeadViewModel
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.features.home.HomeViewModel
import com.garpr.android.features.home.ShareRegionDialogFragment
import com.garpr.android.features.logViewer.LogViewerViewModel
import com.garpr.android.features.player.MatchItemView
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.features.player.PlayerProfileItemView
import com.garpr.android.features.player.PlayerViewModel
import com.garpr.android.features.players.PlayerItemView
import com.garpr.android.features.players.PlayersActivity
import com.garpr.android.features.players.PlayersViewModel
import com.garpr.android.features.rankings.RankingItemView
import com.garpr.android.features.rankings.RankingsActivity
import com.garpr.android.features.rankings.RankingsFragment
import com.garpr.android.features.rankings.RankingsViewModel
import com.garpr.android.features.setIdentity.SetIdentityActivity
import com.garpr.android.features.setIdentity.SetIdentityViewModel
import com.garpr.android.features.setRegion.SetRegionActivity
import com.garpr.android.features.setRegion.SetRegionViewModel
import com.garpr.android.features.settings.DeleteFavoritePlayersPreferenceView
import com.garpr.android.features.settings.IdentityPreferenceView
import com.garpr.android.features.settings.LastPollPreferenceView
import com.garpr.android.features.settings.RankingsPollingPollFrequencyPreferenceView
import com.garpr.android.features.settings.RegionPreferenceView
import com.garpr.android.features.settings.RingtonePreferenceView
import com.garpr.android.features.settings.SettingsActivity
import com.garpr.android.features.settings.SmashRosterSyncPreferenceView
import com.garpr.android.features.settings.TestNotificationsView
import com.garpr.android.features.settings.ThemePreferenceView
import com.garpr.android.features.splash.SplashActivity
import com.garpr.android.features.splash.SplashCardView
import com.garpr.android.features.splash.SplashScreenViewModel
import com.garpr.android.features.tournament.TournamentActivity
import com.garpr.android.features.tournament.TournamentInfoView
import com.garpr.android.features.tournament.TournamentMatchDialogFragment
import com.garpr.android.features.tournament.TournamentViewModel
import com.garpr.android.features.tournaments.TournamentDividerView
import com.garpr.android.features.tournaments.TournamentItemView
import com.garpr.android.features.tournaments.TournamentsActivity
import com.garpr.android.features.tournaments.TournamentsFragment
import com.garpr.android.features.tournaments.TournamentsViewModel
import com.garpr.android.sync.rankings.RankingsPollingWorker
import com.garpr.android.sync.roster.SmashRosterSyncWorker
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ConfigModule::class])
interface AppComponent {

    fun inject(app: App)

    // features/base
    fun inject(activity: BaseActivity)

    // features/common/view
    fun inject(view: IdentityConstraintLayout)
    fun inject(view: PaletteSimpleDraweeView)
    fun inject(view: TintedImageView)

    // features/deepLink
    val deepLinkViewModel: DeepLinkViewModel

    // features/favoritePlayers
    fun inject(view: FavoritePlayerItemView)
    val addOrRemovePlayerFromFavoritesViewModel: AddOrRemovePlayerFromFavoritesViewModel
    val favoritePlayersViewModel: FavoritePlayersViewModel

    // features/headToHead
    fun inject(activity: HeadToHeadActivity)
    fun inject(dialog: HeadToHeadDialogFragment)
    val headToHeadViewModel: HeadToHeadViewModel

    // features/home
    fun inject(activity: HomeActivity)
    fun inject(dialog: ShareRegionDialogFragment)
    val homeViewModel: HomeViewModel

    // features/logViewer
    val logViewerViewModel: LogViewerViewModel

    // features/player
    fun inject(view: MatchItemView)
    fun inject(activity: PlayerActivity)
    fun inject(view: PlayerProfileItemView)
    val playerViewModel: PlayerViewModel

    // features/players
    fun inject(view: PlayerItemView)
    fun inject(activity: PlayersActivity)
    val playersViewModel: PlayersViewModel

    // features/rankings
    fun inject(view: RankingItemView)
    fun inject(activity: RankingsActivity)
    fun inject(fragment: RankingsFragment)
    val rankingsViewModel: RankingsViewModel

    // features/setIdentity
    fun inject(activity: SetIdentityActivity)
    val setIdentityViewModel: SetIdentityViewModel

    // features/setRegion
    fun inject(activity: SetRegionActivity)
    val setRegionViewModel: SetRegionViewModel

    // features/settings
    fun inject(view: DeleteFavoritePlayersPreferenceView)
    fun inject(view: IdentityPreferenceView)
    fun inject(view: LastPollPreferenceView)
    fun inject(view: RankingsPollingPollFrequencyPreferenceView)
    fun inject(view: RegionPreferenceView)
    fun inject(view: RingtonePreferenceView)
    fun inject(activity: SettingsActivity)
    fun inject(view: SmashRosterSyncPreferenceView)
    fun inject(view: TestNotificationsView)
    fun inject(view: ThemePreferenceView)

    // features/splash
    fun inject(activity: SplashActivity)
    fun inject(view: SplashCardView)
    val splashScreenViewModel: SplashScreenViewModel

    // features/tournament
    fun inject(activity: TournamentActivity)
    fun inject(view: TournamentInfoView)
    fun inject(dialog: TournamentMatchDialogFragment)
    val tournamentViewModel: TournamentViewModel

    // features/tournaments
    fun inject(view: TournamentDividerView)
    fun inject(view: TournamentItemView)
    fun inject(activity: TournamentsActivity)
    fun inject(fragment: TournamentsFragment)
    val tournamentsViewModel: TournamentsViewModel

    // sync/rankings
    fun inject(worker: RankingsPollingWorker)

    // sync/roster
    fun inject(worker: SmashRosterSyncWorker)

}
