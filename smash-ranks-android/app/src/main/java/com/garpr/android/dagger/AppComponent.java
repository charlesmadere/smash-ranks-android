package com.garpr.android.dagger;

import com.garpr.android.App;
import com.garpr.android.activities.BaseActivity;
import com.garpr.android.activities.DeepLinkActivity;
import com.garpr.android.activities.HeadToHeadActivity;
import com.garpr.android.activities.HomeActivity;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.activities.PlayersActivity;
import com.garpr.android.activities.RankingsActivity;
import com.garpr.android.activities.SetIdentityActivity;
import com.garpr.android.activities.SetRegionActivity;
import com.garpr.android.activities.SettingsActivity;
import com.garpr.android.activities.SplashActivity;
import com.garpr.android.activities.TournamentActivity;
import com.garpr.android.activities.TournamentsActivity;
import com.garpr.android.dialogs.AddOrRemovePlayerFromFavoritesDialogFragment;
import com.garpr.android.dialogs.HeadToHeadDialogFragment;
import com.garpr.android.dialogs.ShareRegionDialogFragment;
import com.garpr.android.dialogs.TournamentMatchDialogFragment;
import com.garpr.android.sync.rankings.RankingsPollingWorker;
import com.garpr.android.sync.roster.SmashRosterSyncWorker;
import com.garpr.android.views.DeleteFavoritePlayersPreferenceView;
import com.garpr.android.views.FavoritePlayerItemView;
import com.garpr.android.views.FavoritePlayersLayout;
import com.garpr.android.views.IdentityConstraintLayout;
import com.garpr.android.views.IdentityFrameLayout;
import com.garpr.android.views.IdentityPreferenceView;
import com.garpr.android.views.LastPollPreferenceView;
import com.garpr.android.views.MatchItemView;
import com.garpr.android.views.PaletteSimpleDraweeView;
import com.garpr.android.views.PlayerItemView;
import com.garpr.android.views.PlayerProfileItemView;
import com.garpr.android.views.PlayersLayout;
import com.garpr.android.views.RankingItemView;
import com.garpr.android.views.RankingsLayout;
import com.garpr.android.views.RankingsPollingPollFrequencyPreferenceView;
import com.garpr.android.views.RegionPreferenceView;
import com.garpr.android.views.RingtonePreferenceView;
import com.garpr.android.views.SearchableFrameLayout;
import com.garpr.android.views.SearchableRefreshLayout;
import com.garpr.android.views.SmashRosterSyncPreferenceView;
import com.garpr.android.views.SplashCardView;
import com.garpr.android.views.TestNotificationView;
import com.garpr.android.views.ThemePreferenceView;
import com.garpr.android.views.TintedImageView;
import com.garpr.android.views.TournamentDividerView;
import com.garpr.android.views.TournamentInfoItemView;
import com.garpr.android.views.TournamentItemView;
import com.garpr.android.views.TournamentsLayout;
import com.garpr.android.views.toolbars.HomeToolbar;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    void inject(App app);

    // activities
    void inject(BaseActivity activity);
    void inject(DeepLinkActivity activity);
    void inject(HeadToHeadActivity activity);
    void inject(HomeActivity activity);
    void inject(PlayerActivity activity);
    void inject(PlayersActivity activity);
    void inject(RankingsActivity activity);
    void inject(SetIdentityActivity activity);
    void inject(SetRegionActivity activity);
    void inject(SettingsActivity activity);
    void inject(SplashActivity activity);
    void inject(TournamentActivity activity);
    void inject(TournamentsActivity activity);

    // dialogs
    void inject(AddOrRemovePlayerFromFavoritesDialogFragment dialog);
    void inject(HeadToHeadDialogFragment dialog);
    void inject(ShareRegionDialogFragment dialog);
    void inject(TournamentMatchDialogFragment dialog);

    // sync
    void inject(RankingsPollingWorker worker);
    void inject(SmashRosterSyncWorker worker);

    // toolbars
    void inject(HomeToolbar toolbar);

    // views
    void inject(DeleteFavoritePlayersPreferenceView view);
    void inject(FavoritePlayerItemView view);
    void inject(FavoritePlayersLayout view);
    void inject(IdentityConstraintLayout view);
    void inject(IdentityFrameLayout view);
    void inject(IdentityPreferenceView view);
    void inject(LastPollPreferenceView view);
    void inject(MatchItemView view);
    void inject(PaletteSimpleDraweeView view);
    void inject(PlayerItemView view);
    void inject(PlayerProfileItemView view);
    void inject(PlayersLayout view);
    void inject(RankingsPollingPollFrequencyPreferenceView view);
    void inject(RankingItemView view);
    void inject(RankingsLayout view);
    void inject(RegionPreferenceView view);
    void inject(RingtonePreferenceView view);
    void inject(SearchableFrameLayout view);
    void inject(SearchableRefreshLayout view);
    void inject(SmashRosterSyncPreferenceView view);
    void inject(SplashCardView view);
    void inject(TestNotificationView view);
    void inject(ThemePreferenceView view);
    void inject(TintedImageView view);
    void inject(TournamentDividerView view);
    void inject(TournamentInfoItemView view);
    void inject(TournamentItemView view);
    void inject(TournamentsLayout view);

}
