package com.garpr.android.dagger

import com.garpr.android.App
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.common.views.IdentityConstraintLayout
import com.garpr.android.features.common.views.IdentityFrameLayout
import com.garpr.android.features.common.views.PaletteSimpleDraweeView
import com.garpr.android.features.common.views.SearchableFrameLayout
import com.garpr.android.features.common.views.SearchableRefreshLayout
import com.garpr.android.features.common.views.TintedImageView
import com.garpr.android.features.deepLink.DeepLinkActivity
import com.garpr.android.features.favoritePlayers.AddOrRemovePlayerFromFavoritesDialogFragment
import com.garpr.android.features.favoritePlayers.FavoritePlayerItemView
import com.garpr.android.features.favoritePlayers.FavoritePlayersLayout
import com.garpr.android.features.headToHead.HeadToHeadActivity
import com.garpr.android.features.headToHead.HeadToHeadDialogFragment
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.features.home.HomeToolbar
import com.garpr.android.features.home.ShareRegionDialogFragment
import com.garpr.android.features.player.MatchItemView
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.features.player.PlayerProfileItemView
import com.garpr.android.features.players.PlayerItemView
import com.garpr.android.features.players.PlayersActivity
import com.garpr.android.features.players.PlayersLayout
import com.garpr.android.features.rankings.RankingItemView
import com.garpr.android.features.rankings.RankingsActivity
import com.garpr.android.features.rankings.RankingsFragment
import com.garpr.android.features.rankings.RankingsLayout
import com.garpr.android.features.setIdentity.SetIdentityActivity
import com.garpr.android.features.setRegion.SetRegionActivity
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
import com.garpr.android.features.tournament.TournamentActivity
import com.garpr.android.features.tournament.TournamentInfoItemView
import com.garpr.android.features.tournament.TournamentMatchDialogFragment
import com.garpr.android.features.tournaments.TournamentDividerView
import com.garpr.android.features.tournaments.TournamentItemView
import com.garpr.android.features.tournaments.TournamentsActivity
import com.garpr.android.features.tournaments.TournamentsLayout
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
    fun inject(view: IdentityFrameLayout)
    fun inject(view: PaletteSimpleDraweeView)
    fun inject(view: SearchableFrameLayout)
    fun inject(view: SearchableRefreshLayout)
    fun inject(view: TintedImageView)

    // features/deepLink
    fun inject(activity: DeepLinkActivity)

    // features/favoritePlayers
    fun inject(dialog: AddOrRemovePlayerFromFavoritesDialogFragment)
    fun inject(view: FavoritePlayerItemView)
    fun inject(view: FavoritePlayersLayout)

    // features/headToHead
    fun inject(activity: HeadToHeadActivity)
    fun inject(dialog: HeadToHeadDialogFragment)

    // features/home
    fun inject(activity: HomeActivity)
    fun inject(toolbar: HomeToolbar)
    fun inject(dialog: ShareRegionDialogFragment)

    // feature/player
    fun inject(view: MatchItemView)
    fun inject(activity: PlayerActivity)
    fun inject(view: PlayerProfileItemView)

    // features/players
    fun inject(view: PlayerItemView)
    fun inject(activity: PlayersActivity)
    fun inject(view: PlayersLayout)

    // features/rankings
    fun inject(view: RankingItemView)
    fun inject(activity: RankingsActivity)
    fun inject(fragment: RankingsFragment)
    fun inject(view: RankingsLayout)

    // features/setIdentity
    fun inject(activity: SetIdentityActivity)

    // features/setRegion
    fun inject(activity: SetRegionActivity)

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

    // features/tournament
    fun inject(activity: TournamentActivity)
    fun inject(view: TournamentInfoItemView)
    fun inject(dialog: TournamentMatchDialogFragment)

    // features/tournaments
    fun inject(view: TournamentDividerView)
    fun inject(view: TournamentItemView)
    fun inject(activity: TournamentsActivity)
    fun inject(view: TournamentsLayout)

    // features/splash
    fun inject(activity: SplashActivity)
    fun inject(view: SplashCardView)

    // sync/rankings
    fun inject(worker: RankingsPollingWorker)

    // sync/roster
    fun inject(worker: SmashRosterSyncWorker)

}
