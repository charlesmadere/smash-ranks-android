package com.garpr.android.dagger;

import com.garpr.android.activities.SettingsActivity;
import com.garpr.android.fragments.PlayersFragment;
import com.garpr.android.fragments.RankingsFragment;
import com.garpr.android.fragments.TournamentsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    // activities
    void inject(SettingsActivity activity);

    // fragments
    void inject(PlayersFragment fragment);
    void inject(RankingsFragment fragment);
    void inject(TournamentsFragment fragment);

}
