package com.garpr.android.dagger;

import android.app.Application;

import com.garpr.android.data.models.Region;
import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.CrashlyticsWrapperImpl;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.DeviceUtilsImpl;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.misc.ThreadUtilsImpl;
import com.garpr.android.misc.Timber;
import com.garpr.android.wrappers.FacebookFrescoWrapper;
import com.garpr.android.wrappers.ImageLibraryWrapper;
import com.garpr.android.wrappers.WorkManagerWrapper;
import com.garpr.android.wrappers.WorkManagerWrapperImpl;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class AppModule extends BaseAppModule {

    public AppModule(@NonNull final Application application, @NonNull final Region defaultRegion,
            @NonNull final String smashRosterBasePath) {
        super(application, defaultRegion, smashRosterBasePath);
    }

    @NonNull
    @Provides
    @Singleton
    CrashlyticsWrapper providesCrashlyticsWrapper(final Application application) {
        return new CrashlyticsWrapperImpl(application);
    }

    @NonNull
    @Provides
    @Singleton
    DeviceUtils providesDeviceUtils(final Application application) {
        return new DeviceUtilsImpl(application);
    }

    @NonNull
    @Provides
    @Singleton
    ImageLibraryWrapper providesImageLibraryWrapper(final Application application,
            final OkHttpClient okHttpClient, final Timber timber) {
        return new FacebookFrescoWrapper(application, okHttpClient, timber);
    }

    @NonNull
    @Provides
    @Singleton
    ThreadUtils providesThreadUtils(final DeviceUtils deviceUtils) {
        return new ThreadUtilsImpl(deviceUtils.getHasLowRam());
    }

    @NonNull
    @Provides
    @Singleton
    WorkManagerWrapper providesWorkManagerWrapper() {
        return new WorkManagerWrapperImpl();
    }

}
