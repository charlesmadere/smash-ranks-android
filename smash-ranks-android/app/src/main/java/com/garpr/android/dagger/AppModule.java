package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.CrashlyticsWrapperImpl;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.DeviceUtilsImpl;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.RegionManagerImpl;
import com.garpr.android.misc.Timber;
import com.garpr.android.misc.TimberImpl;
import com.garpr.android.models.Ratings;
import com.garpr.android.models.SimpleDate;
import com.garpr.android.networking.GarPrApi;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.networking.ServerApiImpl;
import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.KeyValueStoreImpl;
import com.garpr.android.preferences.PreferenceStore;
import com.garpr.android.preferences.PreferenceStoreImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private final Application mApplication;
    private final String mDefaultRegion;
    private final String mGarPrUrl;


    public AppModule(@NonNull final Application application, @NonNull final String defaultRegion,
            @NonNull final String garPrUrl) {
        mApplication = application;
        mDefaultRegion = defaultRegion;
        mGarPrUrl = garPrUrl;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    CrashlyticsWrapper providesCrashlyticsWrapper() {
        return new CrashlyticsWrapperImpl(mApplication);
    }

    @Provides
    @Singleton
    DeviceUtils providesDeviceUtils() {
        return new DeviceUtilsImpl(mApplication);
    }

    @Provides
    @Singleton
    GarPrApi providesGarPrApi(final Retrofit retrofit) {
        return retrofit.create(GarPrApi.class);
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Ratings.class, Ratings.JSON_DESERIALIZER)
                .registerTypeAdapter(SimpleDate.class, SimpleDate.JSON_DESERIALIZER)
                .create();
    }

    @Provides
    @Singleton
    KeyValueStore providesKeyValueStore() {
        return new KeyValueStoreImpl(mApplication);
    }

    @Provides
    @Singleton
    PreferenceStore providesPreferenceStore(final Gson gson, final KeyValueStore keyValueStore) {
        return new PreferenceStoreImpl(mApplication, gson, keyValueStore, mDefaultRegion);
    }

    @Provides
    @Singleton
    RegionManager providesRegionManager(final PreferenceStore preferenceStore) {
        return new RegionManagerImpl(preferenceStore.getCurrentRegion());
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(final Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mGarPrUrl)
                .build();
    }

    @Provides
    @Singleton
    ServerApi providesServerApi(final GarPrApi garPrApi, final Timber timber) {
        return new ServerApiImpl(garPrApi, timber);
    }

    @Provides
    @Singleton
    Timber providesTimber(final DeviceUtils deviceUtils, final CrashlyticsWrapper crashlyticsWrapper) {
        return new TimberImpl(deviceUtils.hasLowRam(), crashlyticsWrapper);
    }

}
