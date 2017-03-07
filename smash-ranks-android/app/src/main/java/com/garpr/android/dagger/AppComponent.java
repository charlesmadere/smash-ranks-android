package com.garpr.android.dagger;

import com.garpr.android.App;
import com.garpr.android.activities.BaseActivity;
import com.garpr.android.activities.DeepLinkActivity;
import com.garpr.android.activities.HeadToHeadActivity;
import com.garpr.android.activities.HomeActivity;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.activities.SetIdentityActivity;
import com.garpr.android.activities.SettingsActivity;
import com.garpr.android.activities.TournamentActivity;
import com.garpr.android.fragments.BaseFragment;
import com.garpr.android.fragments.PlayersFragment;
import com.garpr.android.fragments.RankingsFragment;
import com.garpr.android.fragments.TournamentsFragment;
import com.garpr.android.sync.RankingsPollingService;
import com.garpr.android.views.DeleteIdentityPreferenceView;
import com.garpr.android.views.FavoritePlayersLayout;
import com.garpr.android.views.IdentityFrameLayout;
import com.garpr.android.views.LastPollPreferenceView;
import com.garpr.android.views.PlayersLayout;
import com.garpr.android.views.PollFrequencyPreferenceView;
import com.garpr.android.views.RankingsLayout;
import com.garpr.android.views.RegionPreferenceView;
import com.garpr.android.views.SearchableFrameLayout;
import com.garpr.android.views.SetIdentityPreferenceView;
import com.garpr.android.views.ThemePreferenceView;
import com.garpr.android.views.TournamentPageView;
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
    void inject(SetIdentityActivity activity);
    void inject(SettingsActivity activity);
    void inject(TournamentActivity activity);

    // fragments
    void inject(BaseFragment fragment);
    void inject(PlayersFragment fragment);
    void inject(RankingsFragment fragment);
    void inject(TournamentsFragment fragment);

    // services
    void inject(RankingsPollingService service);

    // views
    void inject(DeleteIdentityPreferenceView view);
    void inject(FavoritePlayersLayout view);
    void inject(IdentityFrameLayout view);
    void inject(LastPollPreferenceView view);
    void inject(PlayersLayout view);
    void inject(PollFrequencyPreferenceView view);
    void inject(RankingsLayout view);
    void inject(RegionPreferenceView view);
    void inject(SearchableFrameLayout view);
    void inject(SetIdentityPreferenceView view);
    void inject(ThemePreferenceView view);
    void inject(TournamentPageView view);
    void inject(TournamentsLayout view);

}
