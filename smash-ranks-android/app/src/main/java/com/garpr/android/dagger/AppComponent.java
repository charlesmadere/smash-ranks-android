package com.garpr.android.dagger;

import com.garpr.android.App;
import com.garpr.android.activities.BaseActivity;
import com.garpr.android.activities.SettingsActivity;
import com.garpr.android.fragments.BaseFragment;
import com.garpr.android.fragments.PlayersFragment;
import com.garpr.android.fragments.RankingsFragment;
import com.garpr.android.fragments.TournamentsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    // activities
    void inject(BaseActivity activity);
    void inject(SettingsActivity activity);

    // fragments
    void inject(BaseFragment fragment);
    void inject(PlayersFragment fragment);
    void inject(RankingsFragment fragment);
    void inject(TournamentsFragment fragment);

    // other
    void inject(App app);

}
