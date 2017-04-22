package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeepLinkUtils;
import com.garpr.android.misc.DeepLinkUtilsImpl;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.DeviceUtilsImpl;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.FavoritePlayersManagerImpl;
import com.garpr.android.misc.GoogleApiWrapper;
import com.garpr.android.misc.GoogleApiWrapperImpl;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.IdentityManagerImpl;
import com.garpr.android.misc.NotificationManager;
import com.garpr.android.misc.NotificationManagerImpl;
import com.garpr.android.misc.PreviousRankUtils;
import com.garpr.android.misc.PreviousRankUtilsImpl;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.RegionManagerImpl;
import com.garpr.android.misc.ShareUtils;
import com.garpr.android.misc.ShareUtilsImpl;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.misc.ThreadUtilsImpl;
import com.garpr.android.misc.Timber;
import com.garpr.android.misc.TimberImpl;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.Match;
import com.garpr.android.models.Ranking;
import com.garpr.android.models.Ratings;
import com.garpr.android.models.Region;
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
import com.garpr.android.sync.RankingsPollingSyncManager;
import com.garpr.android.sync.RankingsPollingSyncManagerImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class BaseAppModule {

    private static final String FAVORITE_PLAYERS_KEY_VALUE_STORE = "FAVORITE_PLAYERS_KEY_VALUE_STORE";
    private static final String GENERAL_KEY_VALUE_STORE = "GENERAL_KEY_VALUE_STORE";
    private static final String RANKINGS_POLLING_KEY_VALUE_STORE = "RANKINGS_POLLING_KEY_VALUE_STORE";

    private final Application mApplication;
    private final Region mDefaultRegion;


    public BaseAppModule(@NonNull final Application application,
            @NonNull final Region defaultRegion) {
        mApplication = application;
        mDefaultRegion = defaultRegion;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    DeepLinkUtils providesDeepLinkUtils(final RegionManager regionManager, final Timber timber) {
        return new DeepLinkUtilsImpl(regionManager, timber);
    }

    @Provides
    @Singleton
    DeviceUtils providesDeviceUtils() {
        return new DeviceUtilsImpl(mApplication);
    }

    @Provides
    @Singleton
    @Named(FAVORITE_PLAYERS_KEY_VALUE_STORE)
    KeyValueStore providesFavoritePlayersKeyvalueStore() {
        return new KeyValueStoreImpl(mApplication, mApplication.getPackageName() +
                ".Preferences.v2.FavoritePlayers");
    }

    @Provides
    @Singleton
    FavoritePlayersManager providesFavoritePlayersManager(final Gson gson,
            @Named(FAVORITE_PLAYERS_KEY_VALUE_STORE) final KeyValueStore keyValueStore,
            final Timber timber) {
        return new FavoritePlayersManagerImpl(gson, keyValueStore, timber);
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
                .registerTypeAdapter(AbsPlayer.class, AbsPlayer.JSON_SERIALIZER)
                .registerTypeAdapter(AbsTournament.class, AbsTournament.JSON_DESERIALIZER)
                .registerTypeAdapter(Match.class, Match.JSON_DESERIALIZER)
                .registerTypeAdapter(Ranking.class, Ranking.JSON_DESERIALIZER)
                .registerTypeAdapter(Ratings.class, Ratings.JSON_DESERIALIZER)
                .registerTypeAdapter(Ratings.class, Ratings.JSON_SERIALIZER)
                .registerTypeAdapter(SimpleDate.class, SimpleDate.JSON_DESERIALIZER)
                .registerTypeAdapter(SimpleDate.class, SimpleDate.JSON_SERIALIZER)
                .create();
    }

    @Provides
    @Singleton
    IdentityManager providesIdentityManager(final GeneralPreferenceStore generalPreferenceStore,
            final Timber timber) {
        return new IdentityManagerImpl(generalPreferenceStore.getIdentity(), timber);
    }

    @Provides
    @Singleton
    NotificationManager providesNotificationManager(
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final RegionManager regionManager) {
        return new NotificationManagerImpl(mApplication, rankingsPollingPreferenceStore,
                regionManager);
    }

    @Provides
    @Singleton
    PreviousRankUtils providesPreviousRankUtils() {
        return new PreviousRankUtilsImpl();
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
    RankingsPollingSyncManager providesRankingsPollingSyncManager(
            final GoogleApiWrapper googleApiWrapper,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final Timber timber) {
        return new RankingsPollingSyncManagerImpl(googleApiWrapper, rankingsPollingPreferenceStore,
                timber);
    }

    @Provides
    @Singleton
    RegionManager providesRegionManager(final GeneralPreferenceStore generalPreferenceStore,
            final Timber timber) {
        return new RegionManagerImpl(generalPreferenceStore.getCurrentRegion(), timber);
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(final Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constants.GAR_PR_BASE_PATH)
                .build();
    }

    @Provides
    @Singleton
    ServerApi providesServerApi(final GarPrApi garPrApi,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final RegionManager regionManager, final Timber timber) {
        return new ServerApiImpl(garPrApi, rankingsPollingPreferenceStore, regionManager, timber);
    }

    @Provides
    @Singleton
    ShareUtils providesShareUtils(final RegionManager regionManager, final Timber timber) {
        return new ShareUtilsImpl(regionManager, timber);
    }

    @Provides
    @Singleton
    ThreadUtils providesThreadUtils(final DeviceUtils deviceUtils) {
        return new ThreadUtilsImpl(deviceUtils.hasLowRam());
    }

    @Provides
    @Singleton
    Timber providesTimber(final DeviceUtils deviceUtils, final CrashlyticsWrapper crashlyticsWrapper) {
        return new TimberImpl(deviceUtils.hasLowRam(), crashlyticsWrapper);
    }

}
