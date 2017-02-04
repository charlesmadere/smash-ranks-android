package com.garpr.android.dagger;

import com.garpr.android.models.AbsTournamentTest;
import com.garpr.android.models.RatingsTest;
import com.garpr.android.models.SimpleDateTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { TestAppModule.class })
public interface TestAppComponent {

    // models
    void inject(AbsTournamentTest test);
    void inject(RatingsTest test);
    void inject(SimpleDateTest test);

}
