package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.CrashlyticsWrapperImpl;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.DeviceUtilsImpl;
import com.garpr.android.misc.GoogleApiWrapper;
import com.garpr.android.misc.GoogleApiWrapperImpl;
import com.garpr.android.misc.NotificationManager;
import com.garpr.android.misc.NotificationManagerImpl;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.RegionManagerImpl;
import com.garpr.android.misc.Timber;
import com.garpr.android.misc.TimberImpl;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.Ratings;
import com.garpr.android.models.SimpleDate;
import com.garpr.android.networking.GarPrApi;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.networking.ServerApiImpl;
import com.garpr.android.preferences.GeneralPreferenceStore;
import com.garpr.android.preferences.GeneralPreferenceStoreImpl;
import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.KeyValueStoreImpl;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;
import com.garpr.android.preferences.RankingsPollingPreferenceStoreImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private static final String GENERAL_KEY_VALUE_STORE = "GENERAL_KEY_VALUE_STORE";
    private static final String RANKINGS_POLLING_KEY_VALUE_STORE = "RANKINGS_POLLING_KEY_VALUE_STORE";

    private final Application mApplication;
    private final String mDefaultRegion;
    private final String mGarPrUrl;


    public AppModule(@NonNull final Application application, @NonNull final String garPrUrl,
            @NonNull final String defaultRegion) {
        mApplication = application;
        mGarPrUrl = garPrUrl;
        mDefaultRegion = defaultRegion;
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
    @Named(GENERAL_KEY_VALUE_STORE)
    KeyValueStore providesGeneralKeyValueStore() {
        return new KeyValueStoreImpl(mApplication, mApplication.getPackageName() +
                ".Preferences.v2.General");
    }

    @Provides
    @Singleton
    GeneralPreferenceStore providesGeneralPreferenceStore(final Gson gson,
            @Named(GENERAL_KEY_VALUE_STORE) final KeyValueStore keyValueStore) {
        return new GeneralPreferenceStoreImpl(gson, keyValueStore, mDefaultRegion);
    }

    @Provides
    @Singleton
    GoogleApiWrapper providesGoogleApiWrapper(final CrashlyticsWrapper crashlyticsWrapper,
            final Timber timber) {
        return new GoogleApiWrapperImpl(mApplication, crashlyticsWrapper, timber);
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder()
                .registerTypeAdapter(AbsPlayer.class, AbsPlayer.JSON_DESERIALIZER)
                .registerTypeAdapter(AbsTournament.class, AbsTournament.JSON_DESERIALIZER)
                .registerTypeAdapter(Ratings.class, Ratings.JSON_DESERIALIZER)
                .registerTypeAdapter(SimpleDate.class, SimpleDate.JSON_DESERIALIZER)
                .registerTypeAdapter(SimpleDate.class, SimpleDate.JSON_SERIALIZER)
                .create();
    }

    @Provides
    @Singleton
    NotificationManager providesNotificationManager() {
        return new NotificationManagerImpl(mApplication);
    }

    @Provides
    @Singleton
    @Named(RANKINGS_POLLING_KEY_VALUE_STORE)
    KeyValueStore providesRankingsPollingKeyValueStore() {
        return new KeyValueStoreImpl(mApplication, mApplication.getPackageName() +
                ".Preferences.v2.RankingsPolling");
    }

    @Provides
    @Singleton
    RankingsPollingPreferenceStore providesRankingsPollingPreferenceStore(final Gson gson,
            @Named(RANKINGS_POLLING_KEY_VALUE_STORE) final KeyValueStore keyValueStore) {
        return new RankingsPollingPreferenceStoreImpl(gson, keyValueStore);
    }

    @Provides
    @Singleton
    RegionManager providesRegionManager(final GeneralPreferenceStore generalPreferenceStore) {
        return new RegionManagerImpl(generalPreferenceStore.getCurrentRegion());
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
