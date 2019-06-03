package com.garpr.android.dagger;

import com.garpr.android.App;
import com.garpr.android.activities.BaseActivity;
import com.garpr.android.dialogs.ShareRegionDialogFragment;
import com.garpr.android.dialogs.TournamentMatchDialogFragment;
import com.garpr.android.features.deepLink.DeepLinkActivity;
import com.garpr.android.features.favoritePlayers.AddOrRemovePlayerFromFavoritesDialogFragment;
import com.garpr.android.features.favoritePlayers.FavoritePlayerItemView;
import com.garpr.android.features.favoritePlayers.FavoritePlayersLayout;
import com.garpr.android.features.headToHead.HeadToHeadActivity;
import com.garpr.android.features.headToHead.HeadToHeadDialogFragment;
import com.garpr.android.features.home.HomeActivity;
import com.garpr.android.features.player.MatchItemView;
import com.garpr.android.features.player.PlayerActivity;
import com.garpr.android.features.player.PlayerProfileItemView;
import com.garpr.android.features.players.PlayerItemView;
import com.garpr.android.features.players.PlayersActivity;
import com.garpr.android.features.players.PlayersLayout;
import com.garpr.android.features.rankings.RankingItemView;
import com.garpr.android.features.rankings.RankingsActivity;
import com.garpr.android.features.rankings.RankingsLayout;
import com.garpr.android.features.setIdentity.SetIdentityActivity;
import com.garpr.android.features.setRegion.SetRegionActivity;
import com.garpr.android.features.settings.DeleteFavoritePlayersPreferenceView;
import com.garpr.android.features.settings.IdentityPreferenceView;
import com.garpr.android.features.settings.LastPollPreferenceView;
import com.garpr.android.features.settings.RankingsPollingPollFrequencyPreferenceView;
import com.garpr.android.features.settings.RegionPreferenceView;
import com.garpr.android.features.settings.RingtonePreferenceView;
import com.garpr.android.features.settings.SettingsActivity;
import com.garpr.android.features.settings.SmashRosterSyncPreferenceView;
import com.garpr.android.features.settings.ThemePreferenceView;
import com.garpr.android.features.splash.SplashActivity;
import com.garpr.android.features.splash.SplashCardView;
import com.garpr.android.features.tournament.TournamentActivity;
import com.garpr.android.features.tournament.TournamentInfoItemView;
import com.garpr.android.features.tournaments.TournamentDividerView;
import com.garpr.android.features.tournaments.TournamentItemView;
import com.garpr.android.features.tournaments.TournamentsActivity;
import com.garpr.android.features.tournaments.TournamentsLayout;
import com.garpr.android.sync.rankings.RankingsPollingWorker;
import com.garpr.android.sync.roster.SmashRosterSyncWorker;
import com.garpr.android.views.IdentityConstraintLayout;
import com.garpr.android.views.IdentityFrameLayout;
import com.garpr.android.views.PaletteSimpleDraweeView;
import com.garpr.android.views.SearchableFrameLayout;
import com.garpr.android.views.SearchableRefreshLayout;
import com.garpr.android.views.TestNotificationView;
import com.garpr.android.views.TintedImageView;
import com.garpr.android.views.toolbars.HomeToolbar;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    void inject(App app);

    // activities
    void inject(BaseActivity activity);

    // dialogs
    void inject(ShareRegionDialogFragment dialog);
    void inject(TournamentMatchDialogFragment dialog);

    // features/deepLink
    void inject(DeepLinkActivity activity);

    // features/favoritePlayers
    void inject(AddOrRemovePlayerFromFavoritesDialogFragment dialog);
    void inject(FavoritePlayerItemView view);
    void inject(FavoritePlayersLayout view);

    // features/headToHead
    void inject(HeadToHeadActivity activity);
    void inject(HeadToHeadDialogFragment dialog);

    // features/home
    void inject(HomeActivity activity);

    // feature/player
    void inject(MatchItemView view);
    void inject(PlayerActivity activity);
    void inject(PlayerProfileItemView view);

    // features/players
    void inject(PlayerItemView view);
    void inject(PlayersActivity activity);

    // features/rankings
    void inject(RankingItemView view);
    void inject(RankingsActivity activity);
    void inject(RankingsLayout view);

    // features/setIdentity
    void inject(SetIdentityActivity activity);

    // features/setRegion
    void inject(SetRegionActivity activity);

    // features/settings
    void inject(DeleteFavoritePlayersPreferenceView view);
    void inject(IdentityPreferenceView view);
    void inject(LastPollPreferenceView view);
    void inject(RankingsPollingPollFrequencyPreferenceView view);
    void inject(RegionPreferenceView view);
    void inject(RingtonePreferenceView view);
    void inject(SettingsActivity activity);
    void inject(SmashRosterSyncPreferenceView view);
    void inject(TestNotificationView view);
    void inject(ThemePreferenceView view);

    // features/tournament
    void inject(TournamentActivity activity);

    // features/tournaments
    void inject(TournamentDividerView view);
    void inject(TournamentItemView view);
    void inject(TournamentsActivity activity);
    void inject(TournamentsLayout view);

    // features/splash
    void inject(SplashActivity activity);
    void inject(SplashCardView view);

    // sync
    void inject(RankingsPollingWorker worker);
    void inject(SmashRosterSyncWorker worker);

    // toolbars
    void inject(HomeToolbar toolbar);

    // views
    void inject(IdentityConstraintLayout view);
    void inject(IdentityFrameLayout view);
    void inject(PaletteSimpleDraweeView view);
    void inject(PlayersLayout view);
    void inject(SearchableFrameLayout view);
    void inject(SearchableRefreshLayout view);
    void inject(TintedImageView view);
    void inject(TournamentInfoItemView view);

}
