package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.CrashlyticsWrapperImpl;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.DeviceUtilsImpl;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.misc.ThreadUtilsImpl;
import com.garpr.android.models.Region;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule extends BaseAppModule {

    public AppModule(@NonNull final Application application, @NonNull final Region defaultRegion) {
        super(application, defaultRegion);
    }

    @Provides
    @Singleton
    CrashlyticsWrapper providesCrashlyticsWrapper(final Application application) {
        return new CrashlyticsWrapperImpl(application);
    }

    @Provides
    @Singleton
    DeviceUtils providesDeviceUtils(final Application application) {
        return new DeviceUtilsImpl(application);
    }

    @Provides
    @Singleton
    ThreadUtils providesThreadUtils(final DeviceUtils deviceUtils) {
        return new ThreadUtilsImpl(deviceUtils.hasLowRam());
    }

}
