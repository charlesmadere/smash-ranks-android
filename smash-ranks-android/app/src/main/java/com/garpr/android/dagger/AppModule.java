package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.CrashlyticsWrapperImpl;
import com.garpr.android.models.LiteRegion;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule extends BaseAppModule {

    public AppModule(@NonNull final Application application, @NonNull final LiteRegion defaultRegion) {
        super(application, defaultRegion);
    }

    @Provides
    @Singleton
    CrashlyticsWrapper providesCrashlyticsWrapper(final Application application) {
        return new CrashlyticsWrapperImpl(application);
    }

}
