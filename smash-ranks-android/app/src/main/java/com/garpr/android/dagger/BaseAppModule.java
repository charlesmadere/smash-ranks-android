package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.garpr.android.managers.AppUpgradeManager;
import com.garpr.android.managers.AppUpgradeManagerImpl;
import com.garpr.android.managers.FavoritePlayersManager;
import com.garpr.android.managers.FavoritePlayersManagerImpl;
import com.garpr.android.managers.HomeToolbarManager;
import com.garpr.android.managers.HomeToolbarManagerImpl;
import com.garpr.android.managers.IdentityManager;
import com.garpr.android.managers.IdentityManagerImpl;
import com.garpr.android.managers.NotificationsManager;
import com.garpr.android.managers.NotificationsManagerImpl;
import com.garpr.android.managers.PlayerProfileManager;
import com.garpr.android.managers.PlayerProfileManagerImpl;
import com.garpr.android.managers.PlayerToolbarManager;
import com.garpr.android.managers.PlayerToolbarManagerImpl;
import com.garpr.android.managers.RegionManager;
import com.garpr.android.managers.RegionManagerImpl;
import com.garpr.android.managers.TournamentAdapterManager;
import com.garpr.android.managers.TournamentAdapterManagerImpl;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeepLinkUtils;
import com.garpr.android.misc.DeepLinkUtilsImpl;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.FullTournamentUtils;
import com.garpr.android.misc.FullTournamentUtilsImpl;
import com.garpr.android.misc.PreviousRankUtils;
import com.garpr.android.misc.PreviousRankUtilsImpl;
import com.garpr.android.misc.RankingsNotificationsUtils;
import com.garpr.android.misc.RankingsNotificationsUtilsImpl;
import com.garpr.android.misc.ShareUtils;
import com.garpr.android.misc.ShareUtilsImpl;
import com.garpr.android.misc.SmashRosterStorage;
import com.garpr.android.misc.SmashRosterStorageImpl;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.misc.Timber;
import com.garpr.android.misc.TimberImpl;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsRegion;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.Avatar;
import com.garpr.android.models.Match;
import com.garpr.android.models.Region;
import com.garpr.android.models.SimpleDate;
import com.garpr.android.networking.GarPrApi;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.networking.ServerApiImpl;
import com.garpr.android.networking.SmashRosterApi;
import com.garpr.android.preferences.GeneralPreferenceStore;
import com.garpr.android.preferences.GeneralPreferenceStoreImpl;
import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.KeyValueStoreProvider;
import com.garpr.android.preferences.KeyValueStoreProviderImpl;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;
import com.garpr.android.preferences.RankingsPollingPreferenceStoreImpl;
import com.garpr.android.preferences.SmashRosterPreferenceStore;
import com.garpr.android.preferences.SmashRosterPreferenceStoreImpl;
import com.garpr.android.sync.RankingsPollingSyncManager;
import com.garpr.android.sync.RankingsPollingSyncManagerImpl;
import com.garpr.android.sync.SmashRosterSyncManager;
import com.garpr.android.sync.SmashRosterSyncManagerImpl;
import com.garpr.android.wrappers.FirebaseApiWrapper;
import com.garpr.android.wrappers.FirebaseApiWrapperImpl;
import com.garpr.android.wrappers.GoogleApiWrapper;
import com.garpr.android.wrappers.GoogleApiWrapperImpl;
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
    private static final String GAR_PR_API = "GAR_PR_API";
    private static final String GAR_PR_RETROFIT = "GAR_PR_RETROFIT";
    private static final String GENERAL_KEY_VALUE_STORE = "GENERAL_KEY_VALUE_STORE";
    private static final String NOT_GAR_PR_API = "NOT_GAR_PR_API";
    private static final String NOT_GAR_PR_RETROFIT = "NOT_GAR_PR_RETROFIT";
    private static final String RANKINGS_POLLING_KEY_VALUE_STORE = "RANKINGS_POLLING_KEY_VALUE_STORE";
    private static final String SMASH_ROSTER_KEY_VALUE_STORE = "SMASH_ROSTER_KEY_VALUE_STORE";
    private static final String SMASH_ROSTER_RETROFIT = "SMASH_ROSTER_RETROFIT";

    @NonNull private final Application mApplication;
    @NonNull private final Region mDefaultRegion;


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
    AppUpgradeManager providesAppUpgradeManager(final FavoritePlayersManager favoritePlayersManager,
            final GeneralPreferenceStore generalPreferenceStore, final Timber timber) {
        return new AppUpgradeManagerImpl(favoritePlayersManager, generalPreferenceStore, timber);
    }

    @Provides
    @Singleton
    DeepLinkUtils providesDeepLinkUtils(final RegionManager regionManager, final Timber timber) {
        return new DeepLinkUtilsImpl(regionManager, timber);
    }

    @Provides
    @Singleton
    @Named(FAVORITE_PLAYERS_KEY_VALUE_STORE)
    KeyValueStore providesFavoritePlayersKeyValueStore(
            final KeyValueStoreProvider keyValueStoreProvider) {
        return keyValueStoreProvider.getKeyValueStore(mApplication.getPackageName() +
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
    FirebaseApiWrapper providesFirebaseApiWrapper() {
        return new FirebaseApiWrapperImpl(mApplication);
    }

    @Provides
    @Singleton
    FullTournamentUtils providesFullTournamentUtils(final ThreadUtils threadUtils) {
        return new FullTournamentUtilsImpl(threadUtils);
    }

    @Provides
    @Singleton
    @Named(GAR_PR_API)
    GarPrApi providesGarPrApi(@Named(GAR_PR_RETROFIT) final Retrofit retrofit) {
        return retrofit.create(GarPrApi.class);
    }

    @Provides
    @Singleton
    @Named(GAR_PR_RETROFIT)
    Retrofit providesGarPrRetrofit(final GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(Constants.GAR_PR_BASE_PATH + ':' + Constants.GAR_PR_API_PORT)
                .build();
    }

    @Provides
    @Singleton
    @Named(GENERAL_KEY_VALUE_STORE)
    KeyValueStore providesGeneralKeyValueStore(final KeyValueStoreProvider keyValueStoreProvider) {
        return keyValueStoreProvider.getKeyValueStore(mApplication.getPackageName() +
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
                .registerTypeAdapter(AbsPlayer.class, AbsPlayer.Companion.getJSON_DESERIALIZER())
                .registerTypeAdapter(AbsPlayer.class, AbsPlayer.Companion.getJSON_SERIALIZER())
                .registerTypeAdapter(AbsRegion.class, AbsRegion.Companion.getJSON_DESERIALIZER())
                .registerTypeAdapter(AbsRegion.class, AbsRegion.Companion.getJSON_SERIALIZER())
                .registerTypeAdapter(AbsTournament.class, AbsTournament.Companion.getJSON_DESERIALIZER())
                .registerTypeAdapter(Avatar.class, Avatar.Companion.getJSON_DESERIALIZER())
                .registerTypeAdapter(Match.class, Match.Companion.getJSON_DESERIALIZER())
                .registerTypeAdapter(SimpleDate.class, SimpleDate.Companion.getJSON_DESERIALIZER())
                .registerTypeAdapter(SimpleDate.class, SimpleDate.Companion.getJSON_SERIALIZER())
                .create();
    }

    @Provides
    @Singleton
    GsonConverterFactory providesGsonConverterFactory(final Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    HomeToolbarManager providesHomeToolbarManager(final IdentityManager identityManager,
            final RegionManager regionManager) {
        return new HomeToolbarManagerImpl(identityManager, regionManager);
    }

    @Provides
    @Singleton
    IdentityManager providesIdentityManager(final GeneralPreferenceStore generalPreferenceStore,
            final Timber timber) {
        return new IdentityManagerImpl(generalPreferenceStore.getIdentity(), timber);
    }

    @Provides
    @Singleton
    KeyValueStoreProvider providesKeyValueStoreProvider() {
        return new KeyValueStoreProviderImpl(mApplication);
    }

    @Provides
    @Singleton
    @Named(NOT_GAR_PR_API)
    GarPrApi providesNotGarPrApi(@Named(NOT_GAR_PR_RETROFIT) final Retrofit retrofit) {
        return retrofit.create(GarPrApi.class);
    }

    @Provides
    @Singleton
    @Named(NOT_GAR_PR_RETROFIT)
    Retrofit providesNotGarPrRetrofit(final GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(Constants.NOT_GAR_PR_BASE_PATH + ':' + Constants.NOT_GAR_PR_API_PORT)
                .build();
    }

    @Provides
    @Singleton
    NotificationsManager providesNotificationManager(
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final RegionManager regionManager, final Timber timber) {
        return new NotificationsManagerImpl(mApplication, rankingsPollingPreferenceStore,
                regionManager, timber);
    }

    @Provides
    @Singleton
    PlayerProfileManager providesPlayerProfileViewManager(
            final FavoritePlayersManager favoritePlayersManager,
            final IdentityManager identityManager, final RegionManager regionManager,
            final SmashRosterStorage smashRosterStorage) {
        return new PlayerProfileManagerImpl(mApplication, favoritePlayersManager, identityManager,
                regionManager, smashRosterStorage);
    }

    @Provides
    @Singleton
    PlayerToolbarManager providesPlayerToolbarManager() {
        return new PlayerToolbarManagerImpl();
    }

    @Provides
    @Singleton
    PreviousRankUtils providesPreviousRankUtils() {
        return new PreviousRankUtilsImpl();
    }

    @Provides
    @Singleton
    RankingsNotificationsUtils providesRankingNotificationsUtils(final DeviceUtils deviceUtils,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final Timber timber) {
        return new RankingsNotificationsUtilsImpl(deviceUtils, rankingsPollingPreferenceStore,
                timber);
    }

    @Provides
    @Singleton
    @Named(RANKINGS_POLLING_KEY_VALUE_STORE)
    KeyValueStore providesRankingsPollingKeyValueStore(
            final KeyValueStoreProvider keyValueStoreProvider) {
        return keyValueStoreProvider.getKeyValueStore(mApplication.getPackageName() +
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
            final FirebaseApiWrapper firebaseApiWrapper, final GoogleApiWrapper googleApiWrapper,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final Timber timber) {
        return new RankingsPollingSyncManagerImpl(firebaseApiWrapper, googleApiWrapper,
                rankingsPollingPreferenceStore, timber);
    }

    @Provides
    @Singleton
    RegionManager providesRegionManager(final GeneralPreferenceStore generalPreferenceStore,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore, final Timber timber) {
        return new RegionManagerImpl(generalPreferenceStore, rankingsPollingPreferenceStore, timber);
    }

    @Provides
    @Singleton
    ServerApi providesServerApi(final FullTournamentUtils fullTournamentUtils,
            @Named(GAR_PR_API) final GarPrApi garPrApi,
            @Named(NOT_GAR_PR_API) final GarPrApi notGarPrApi,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final RegionManager regionManager,
            final SmashRosterApi smashRosterApi,
            final Timber timber) {
        return new ServerApiImpl(fullTournamentUtils, garPrApi, notGarPrApi,
                rankingsPollingPreferenceStore, regionManager, smashRosterApi, timber);
    }

    @Provides
    @Singleton
    ShareUtils providesShareUtils(final RegionManager regionManager, final Timber timber) {
        return new ShareUtilsImpl(regionManager, timber);
    }

    @Provides
    @Singleton
    SmashRosterApi providesSmashRosterApi(@Named(SMASH_ROSTER_RETROFIT) final Retrofit retrofit) {
        return retrofit.create(SmashRosterApi.class);
    }

    @Provides
    @Singleton
    @Named(SMASH_ROSTER_KEY_VALUE_STORE)
    KeyValueStore providesSmashRosterKeyValueStore(
            final KeyValueStoreProvider keyValueStoreProvider) {
        return keyValueStoreProvider.getKeyValueStore(mApplication.getPackageName() +
                ".Preferences.v2.SmashRoster");
    }

    @Provides
    @Singleton
    @Named(SMASH_ROSTER_RETROFIT)
    Retrofit providesSmashRosterRetrofit(final GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(Constants.SMASH_ROSTER_BASE_PATH)
                .build();
    }

    @Provides
    @Singleton
    SmashRosterPreferenceStore providesSmashRosterPreferenceStore(final Gson gson,
            @Named(SMASH_ROSTER_KEY_VALUE_STORE) final KeyValueStore keyValueStore) {
        return new SmashRosterPreferenceStoreImpl(gson, keyValueStore);
    }

    @Provides
    @Singleton
    SmashRosterStorage providesSmashRosterStorage(final Gson gson,
            final KeyValueStoreProvider keyValueStoreProvider, final Timber timber) {
        return new SmashRosterStorageImpl(gson, keyValueStoreProvider,
                mApplication.getPackageName(), timber);
    }

    @Provides
    @Singleton
    SmashRosterSyncManager providesSmashRosterSyncManager(final FirebaseApiWrapper firebaseApiWrapper,
            final GoogleApiWrapper googleApiWrapper,
            final RegionManager regionManager,
            final ServerApi serverApi,
            final SmashRosterPreferenceStore smashRosterPreferenceStore,
            final SmashRosterStorage smashRosterStorage,
            final ThreadUtils threadUtils,
            final Timber timber) {
        return new SmashRosterSyncManagerImpl(firebaseApiWrapper, googleApiWrapper, regionManager, serverApi,
                smashRosterPreferenceStore, smashRosterStorage, threadUtils, timber);
    }

    @Provides
    @Singleton
    Timber providesTimber(final DeviceUtils deviceUtils, final CrashlyticsWrapper crashlyticsWrapper) {
        return new TimberImpl(deviceUtils.getHasLowRam(), crashlyticsWrapper);
    }

    @Provides
    @Singleton
    TournamentAdapterManager providesTournamentAdapterManager() {
        return new TournamentAdapterManagerImpl(mApplication);
    }

}
