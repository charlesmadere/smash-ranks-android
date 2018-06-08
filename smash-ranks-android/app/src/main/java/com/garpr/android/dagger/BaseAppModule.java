package com.garpr.android.dagger;

import android.app.Application;
import android.support.annotation.NonNull;
import com.garpr.android.managers.*;
import com.garpr.android.misc.*;
import com.garpr.android.models.*;
import com.garpr.android.networking.GarPrApi;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.networking.ServerApiImpl;
import com.garpr.android.networking.SmashRosterApi;
import com.garpr.android.preferences.*;
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
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

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
    @NonNull private final String mSmashRosterBasePath;


    public BaseAppModule(@NonNull final Application application,
            @NonNull final Region defaultRegion, @NonNull final String smashRosterBasePath) {
        mApplication = application;
        mDefaultRegion = defaultRegion;
        mSmashRosterBasePath = smashRosterBasePath;
    }

    @NonNull
    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @NonNull
    @Provides
    @Singleton
    AppUpgradeManager providesAppUpgradeManager(final FavoritePlayersManager favoritePlayersManager,
            final GeneralPreferenceStore generalPreferenceStore, final Timber timber) {
        return new AppUpgradeManagerImpl(favoritePlayersManager, generalPreferenceStore, timber);
    }

    @NonNull
    @Provides
    @Singleton
    DeepLinkUtils providesDeepLinkUtils(final RegionManager regionManager, final Timber timber) {
        return new DeepLinkUtilsImpl(regionManager, timber);
    }

    @Named(FAVORITE_PLAYERS_KEY_VALUE_STORE)
    @NonNull
    @Provides
    @Singleton
    KeyValueStore providesFavoritePlayersKeyValueStore(
            final KeyValueStoreProvider keyValueStoreProvider) {
        return keyValueStoreProvider.getKeyValueStore(mApplication.getPackageName() +
                ".Preferences.v2.FavoritePlayers");
    }

    @NonNull
    @Provides
    @Singleton
    FavoritePlayersManager providesFavoritePlayersManager(final Gson gson,
            @Named(FAVORITE_PLAYERS_KEY_VALUE_STORE) final KeyValueStore keyValueStore,
            final Timber timber) {
        return new FavoritePlayersManagerImpl(gson, keyValueStore, timber);
    }

    @NonNull
    @Provides
    @Singleton
    FirebaseApiWrapper providesFirebaseApiWrapper() {
        return new FirebaseApiWrapperImpl(mApplication);
    }

    @NonNull
    @Provides
    @Singleton
    FullTournamentUtils providesFullTournamentUtils(final ThreadUtils threadUtils) {
        return new FullTournamentUtilsImpl(threadUtils);
    }

    @Named(GAR_PR_API)
    @NonNull
    @Provides
    @Singleton
    GarPrApi providesGarPrApi(@Named(GAR_PR_RETROFIT) final Retrofit retrofit) {
        return retrofit.create(GarPrApi.class);
    }

    @Named(GAR_PR_RETROFIT)
    @Provides
    @Singleton
    Retrofit providesGarPrRetrofit(final GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(Constants.GAR_PR_BASE_PATH + ':' + Constants.GAR_PR_API_PORT)
                .build();
    }

    @Named(GENERAL_KEY_VALUE_STORE)
    @Provides
    @Singleton
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

    @Named(NOT_GAR_PR_API)
    @NonNull
    @Provides
    @Singleton
    GarPrApi providesNotGarPrApi(@Named(NOT_GAR_PR_RETROFIT) final Retrofit retrofit) {
        return retrofit.create(GarPrApi.class);
    }

    @Named(NOT_GAR_PR_RETROFIT)
    @NonNull
    @Provides
    @Singleton
    Retrofit providesNotGarPrRetrofit(final GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(Constants.NOT_GAR_PR_BASE_PATH + ':' + Constants.NOT_GAR_PR_API_PORT)
                .build();
    }

    @NonNull
    @Provides
    @Singleton
    NotificationsManager providesNotificationManager(
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final RegionManager regionManager, final Timber timber) {
        return new NotificationsManagerImpl(mApplication, rankingsPollingPreferenceStore,
                regionManager, timber);
    }

    @NonNull
    @Provides
    @Singleton
    PlayerProfileManager providesPlayerProfileViewManager(
            final FavoritePlayersManager favoritePlayersManager,
            final IdentityManager identityManager, final RegionManager regionManager,
            final SmashRosterAvatarUrlHelper smashRosterAvatarUrlHelper,
            final SmashRosterStorage smashRosterStorage) {
        return new PlayerProfileManagerImpl(mApplication, favoritePlayersManager, identityManager,
                regionManager, smashRosterAvatarUrlHelper, smashRosterStorage);
    }

    @NonNull
    @Provides
    @Singleton
    PlayerToolbarManager providesPlayerToolbarManager() {
        return new PlayerToolbarManagerImpl();
    }

    @NonNull
    @Provides
    @Singleton
    PreviousRankUtils providesPreviousRankUtils() {
        return new PreviousRankUtilsImpl();
    }

    @NonNull
    @Provides
    @Singleton
    RankingsNotificationsUtils providesRankingNotificationsUtils(final DeviceUtils deviceUtils,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final Timber timber) {
        return new RankingsNotificationsUtilsImpl(deviceUtils, rankingsPollingPreferenceStore,
                timber);
    }

    @Named(RANKINGS_POLLING_KEY_VALUE_STORE)
    @NonNull
    @Provides
    @Singleton
    KeyValueStore providesRankingsPollingKeyValueStore(
            final KeyValueStoreProvider keyValueStoreProvider) {
        return keyValueStoreProvider.getKeyValueStore(mApplication.getPackageName() +
                ".Preferences.v2.RankingsPolling");
    }

    @NonNull
    @Provides
    @Singleton
    RankingsPollingPreferenceStore providesRankingsPollingPreferenceStore(final Gson gson,
            @Named(RANKINGS_POLLING_KEY_VALUE_STORE) final KeyValueStore keyValueStore) {
        return new RankingsPollingPreferenceStoreImpl(gson, keyValueStore);
    }

    @NonNull
    @Provides
    @Singleton
    RankingsPollingSyncManager providesRankingsPollingSyncManager(
            final FirebaseApiWrapper firebaseApiWrapper, final GoogleApiWrapper googleApiWrapper,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final Timber timber) {
        return new RankingsPollingSyncManagerImpl(firebaseApiWrapper, googleApiWrapper,
                rankingsPollingPreferenceStore, timber);
    }

    @NonNull
    @Provides
    @Singleton
    RegionManager providesRegionManager(final GeneralPreferenceStore generalPreferenceStore,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore, final Timber timber) {
        return new RegionManagerImpl(generalPreferenceStore, rankingsPollingPreferenceStore, timber);
    }

    @NonNull
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

    @NonNull
    @Provides
    @Singleton
    ShareUtils providesShareUtils(final RegionManager regionManager, final Timber timber) {
        return new ShareUtilsImpl(regionManager, timber);
    }

    @NonNull
    @Provides
    @Singleton
    SmashRosterApi providesSmashRosterApi(@Named(SMASH_ROSTER_RETROFIT) final Retrofit retrofit) {
        return retrofit.create(SmashRosterApi.class);
    }

    @NonNull
    @Provides
    @Singleton
    SmashRosterAvatarUrlHelper providesSmashRosterAvatarUrlHelper() {
        return new SmashRosterAvatarUrlHelperImpl(mSmashRosterBasePath);
    }

    @Named(SMASH_ROSTER_KEY_VALUE_STORE)
    @NonNull
    @Provides
    @Singleton
    KeyValueStore providesSmashRosterKeyValueStore(
            final KeyValueStoreProvider keyValueStoreProvider) {
        return keyValueStoreProvider.getKeyValueStore(mApplication.getPackageName() +
                ".Preferences.v2.SmashRoster");
    }

    @Named(SMASH_ROSTER_RETROFIT)
    @NonNull
    @Provides
    @Singleton
    Retrofit providesSmashRosterRetrofit(final GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(Constants.SMASH_ROSTER_BASE_PATH)
                .build();
    }

    @NonNull
    @Provides
    @Singleton
    SmashRosterPreferenceStore providesSmashRosterPreferenceStore(final Gson gson,
            @Named(SMASH_ROSTER_KEY_VALUE_STORE) final KeyValueStore keyValueStore) {
        return new SmashRosterPreferenceStoreImpl(gson, keyValueStore);
    }

    @NonNull
    @Provides
    @Singleton
    SmashRosterStorage providesSmashRosterStorage(final Gson gson,
            final KeyValueStoreProvider keyValueStoreProvider, final Timber timber) {
        return new SmashRosterStorageImpl(gson, keyValueStoreProvider,
                mApplication.getPackageName(), timber);
    }

    @NonNull
    @Provides
    @Singleton
    SmashRosterSyncManager providesSmashRosterSyncManager(final FirebaseApiWrapper firebaseApiWrapper,
            final GoogleApiWrapper googleApiWrapper,
            final ServerApi serverApi,
            final SmashRosterPreferenceStore smashRosterPreferenceStore,
            final SmashRosterStorage smashRosterStorage,
            final ThreadUtils threadUtils,
            final Timber timber) {
        return new SmashRosterSyncManagerImpl(firebaseApiWrapper, googleApiWrapper, serverApi,
                smashRosterPreferenceStore, smashRosterStorage, threadUtils, timber);
    }

    @NonNull
    @Provides
    @Singleton
    Timber providesTimber(final DeviceUtils deviceUtils, final CrashlyticsWrapper crashlyticsWrapper) {
        return new TimberImpl(deviceUtils.getHasLowRam(), crashlyticsWrapper);
    }

    @NonNull
    @Provides
    @Singleton
    TournamentAdapterManager providesTournamentAdapterManager() {
        return new TournamentAdapterManagerImpl(mApplication);
    }

}
