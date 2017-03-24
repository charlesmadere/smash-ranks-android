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
import com.garpr.android.activities.TournamentActivity;
import com.garpr.android.activities.TournamentsActivity;
import com.garpr.android.sync.RankingsPollingService;
import com.garpr.android.views.ClearFavoritePlayersPreferenceView;
import com.garpr.android.views.FavoritePlayersLayout;
import com.garpr.android.views.IdentityFrameLayout;
import com.garpr.android.views.IdentityPreferenceView;
import com.garpr.android.views.LastPollPreferenceView;
import com.garpr.android.views.MatchItemView;
import com.garpr.android.views.PlayerItemView;
import com.garpr.android.views.PlayersLayout;
import com.garpr.android.views.PollFrequencyPreferenceView;
import com.garpr.android.views.RankingItemView;
import com.garpr.android.views.RankingsLayout;
import com.garpr.android.views.RegionPreferenceView;
import com.garpr.android.views.SearchableFrameLayout;
import com.garpr.android.views.ThemePreferenceView;
import com.garpr.android.views.TournamentsLayout;

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
    void inject(TournamentActivity activity);
    void inject(TournamentsActivity activity);

    // services
    void inject(RankingsPollingService service);

    // views
    void inject(ClearFavoritePlayersPreferenceView view);
    void inject(FavoritePlayersLayout view);
    void inject(IdentityFrameLayout view);
    void inject(IdentityPreferenceView view);
    void inject(LastPollPreferenceView view);
    void inject(MatchItemView view);
    void inject(PlayerItemView view);
    void inject(PlayersLayout view);
    void inject(PollFrequencyPreferenceView view);
    void inject(RankingItemView view);
    void inject(RankingsLayout view);
    void inject(RegionPreferenceView view);
    void inject(SearchableFrameLayout view);
    void inject(ThemePreferenceView view);
    void inject(TournamentsLayout view);

}
