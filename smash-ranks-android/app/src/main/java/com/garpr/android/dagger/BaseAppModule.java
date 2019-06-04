package com.garpr.android.dagger;

import android.app.Application;

import com.garpr.android.data.converters.AbsPlayerConverter;
import com.garpr.android.data.converters.AbsRegionConverter;
import com.garpr.android.data.converters.AbsTournamentConverter;
import com.garpr.android.data.converters.MatchConverter;
import com.garpr.android.data.converters.RankedPlayerConverter;
import com.garpr.android.data.converters.SimpleDateConverter;
import com.garpr.android.data.models.Region;
import com.garpr.android.features.deepLink.DeepLinkUtils;
import com.garpr.android.features.deepLink.DeepLinkUtilsImpl;
import com.garpr.android.features.home.HomeToolbarManager;
import com.garpr.android.features.home.HomeToolbarManagerImpl;
import com.garpr.android.features.notifications.NotificationsManager;
import com.garpr.android.features.notifications.NotificationsManagerImpl;
import com.garpr.android.features.player.PlayerProfileManager;
import com.garpr.android.features.player.PlayerProfileManagerImpl;
import com.garpr.android.features.player.SmashRosterAvatarUrlHelper;
import com.garpr.android.features.player.SmashRosterAvatarUrlHelperImpl;
import com.garpr.android.features.splash.AppUpgradeManager;
import com.garpr.android.features.splash.AppUpgradeManagerImpl;
import com.garpr.android.features.splash.SplashScreenManager;
import com.garpr.android.features.splash.SplashScreenManagerImpl;
import com.garpr.android.features.sync.rankings.RankingsNotificationsUtils;
import com.garpr.android.features.sync.rankings.RankingsNotificationsUtilsImpl;
import com.garpr.android.features.sync.rankings.RankingsPollingManager;
import com.garpr.android.features.sync.rankings.RankingsPollingManagerImpl;
import com.garpr.android.features.sync.roster.SmashRosterSyncManager;
import com.garpr.android.features.sync.roster.SmashRosterSyncManagerImpl;
import com.garpr.android.features.tournament.TournamentAdapterManager;
import com.garpr.android.features.tournament.TournamentAdapterManagerImpl;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsWrapper;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.FullTournamentUtils;
import com.garpr.android.misc.FullTournamentUtilsImpl;
import com.garpr.android.misc.PreviousRankUtils;
import com.garpr.android.misc.PreviousRankUtilsImpl;
import com.garpr.android.misc.ShareUtils;
import com.garpr.android.misc.ShareUtilsImpl;
import com.garpr.android.misc.SmashRosterStorage;
import com.garpr.android.misc.SmashRosterStorageImpl;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.misc.Timber;
import com.garpr.android.misc.TimberImpl;
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
import com.garpr.android.repositories.FavoritePlayersRepository;
import com.garpr.android.repositories.FavoritePlayersRepositoryImpl;
import com.garpr.android.repositories.IdentityRepository;
import com.garpr.android.repositories.IdentityRepositoryImpl;
import com.garpr.android.repositories.NightModeRepository;
import com.garpr.android.repositories.NightModeRepositoryImpl;
import com.garpr.android.repositories.RegionRepository;
import com.garpr.android.repositories.RegionRepositoryImpl;
import com.garpr.android.wrappers.WorkManagerWrapper;
import com.squareup.moshi.Moshi;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

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
    AppUpgradeManager providesAppUpgradeManager(
            final FavoritePlayersRepository favoritePlayersRepository,
            final GeneralPreferenceStore generalPreferenceStore, final Timber timber) {
        return new AppUpgradeManagerImpl(favoritePlayersRepository, generalPreferenceStore, timber);
    }

    @NonNull
    @Provides
    @Singleton
    DeepLinkUtils providesDeepLinkUtils(final RegionRepository regionRepository, final Timber timber) {
        return new DeepLinkUtilsImpl(regionRepository, timber);
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
    FavoritePlayersRepository providesFavoritePlayersRepository(final Moshi moshi,
            @Named(FAVORITE_PLAYERS_KEY_VALUE_STORE) final KeyValueStore keyValueStore,
            final Timber timber) {
        return new FavoritePlayersRepositoryImpl(keyValueStore, moshi, timber);
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
    @NonNull
    @Provides
    @Singleton
    Retrofit providesGarPrRetrofit(final MoshiConverterFactory moshiConverterFactory,
            final OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(moshiConverterFactory)
                .baseUrl(Constants.GAR_PR_BASE_PATH + ':' + Constants.GAR_PR_API_PORT)
                .client(okHttpClient)
                .build();
    }

    @Named(GENERAL_KEY_VALUE_STORE)
    @NonNull
    @Provides
    @Singleton
    KeyValueStore providesGeneralKeyValueStore(final KeyValueStoreProvider keyValueStoreProvider) {
        return keyValueStoreProvider.getKeyValueStore(mApplication.getPackageName() +
                ".Preferences.v2.General");
    }

    @NonNull
    @Provides
    @Singleton
    GeneralPreferenceStore providesGeneralPreferenceStore(final Moshi moshi,
            @Named(GENERAL_KEY_VALUE_STORE) final KeyValueStore keyValueStore) {
        return new GeneralPreferenceStoreImpl(keyValueStore, moshi, mDefaultRegion);
    }

    @NonNull
    @Provides
    @Singleton
    HomeToolbarManager providesHomeToolbarManager(final IdentityRepository identityRepository) {
        return new HomeToolbarManagerImpl(identityRepository);
    }

    @NonNull
    @Provides
    @Singleton
    IdentityRepository providesIdentityRepository(
            final GeneralPreferenceStore generalPreferenceStore, final Timber timber) {
        return new IdentityRepositoryImpl(generalPreferenceStore.getIdentity(), timber);
    }

    @NonNull
    @Provides
    @Singleton
    KeyValueStoreProvider providesKeyValueStoreProvider() {
        return new KeyValueStoreProviderImpl(mApplication);
    }

    @NonNull
    @Provides
    @Singleton
    Moshi providesMoshi() {
        return new Moshi.Builder()
                .add(AbsPlayerConverter.INSTANCE)
                .add(AbsRegionConverter.INSTANCE)
                .add(AbsTournamentConverter.INSTANCE)
                .add(MatchConverter.INSTANCE)
                .add(RankedPlayerConverter.INSTANCE)
                .add(SimpleDateConverter.INSTANCE)
                .build();
    }

    @NonNull
    @Provides
    @Singleton
    MoshiConverterFactory providesMoshiConverterFactory(final Moshi moshi) {
        return MoshiConverterFactory.create(moshi);
    }

    @NonNull
    @Provides
    @Singleton
    NightModeRepository providesNightModeRepository(
            final GeneralPreferenceStore generalPreferenceStore, final Timber timber) {
        return new NightModeRepositoryImpl(generalPreferenceStore, timber);
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
    Retrofit providesNotGarPrRetrofit(final MoshiConverterFactory moshiConverterFactory,
            final OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(moshiConverterFactory)
                .baseUrl(Constants.NOT_GAR_PR_BASE_PATH + ':' + Constants.NOT_GAR_PR_API_PORT)
                .client(okHttpClient)
                .build();
    }

    @NonNull
    @Provides
    @Singleton
    NotificationsManager providesNotificationManager(
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final RegionRepository regionRepository, final Timber timber) {
        return new NotificationsManagerImpl(mApplication, rankingsPollingPreferenceStore,
                regionRepository, timber);
    }

    @NonNull
    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @NonNull
    @Provides
    @Singleton
    PlayerProfileManager providesPlayerProfileViewManager(
            final FavoritePlayersRepository favoritePlayersRepository,
            final IdentityRepository identityRepository,
            final SmashRosterAvatarUrlHelper smashRosterAvatarUrlHelper,
            final SmashRosterStorage smashRosterStorage) {
        return new PlayerProfileManagerImpl(mApplication, favoritePlayersRepository,
                identityRepository, smashRosterAvatarUrlHelper, smashRosterStorage);
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
    RankingsPollingPreferenceStore providesRankingsPollingPreferenceStore(final Moshi moshi,
            @Named(RANKINGS_POLLING_KEY_VALUE_STORE) final KeyValueStore keyValueStore) {
        return new RankingsPollingPreferenceStoreImpl(keyValueStore, moshi);
    }

    @NonNull
    @Provides
    @Singleton
    RankingsPollingManager providesRankingsPollingSyncManager(
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final Timber timber, final WorkManagerWrapper workManagerWrapper) {
        return new RankingsPollingManagerImpl(rankingsPollingPreferenceStore, timber,
                workManagerWrapper);
    }

    @NonNull
    @Provides
    @Singleton
    RegionRepository providesRegionRepository(final GeneralPreferenceStore generalPreferenceStore,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore, final Timber timber) {
        return new RegionRepositoryImpl(generalPreferenceStore, rankingsPollingPreferenceStore, timber);
    }

    @NonNull
    @Provides
    @Singleton
    ServerApi providesServerApi(final FullTournamentUtils fullTournamentUtils,
            @Named(GAR_PR_API) final GarPrApi garPrApi,
            @Named(NOT_GAR_PR_API) final GarPrApi notGarPrApi,
            final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            final RegionRepository regionRepository,
            final SmashRosterApi smashRosterApi,
            final ThreadUtils threadUtils,
            final Timber timber) {
        return new ServerApiImpl(fullTournamentUtils, garPrApi, notGarPrApi,
                rankingsPollingPreferenceStore, regionRepository, smashRosterApi, threadUtils,
                timber);
    }

    @NonNull
    @Provides
    @Singleton
    ShareUtils providesShareUtils(final RegionRepository regionRepository, final Timber timber) {
        return new ShareUtilsImpl(regionRepository, timber);
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
    Retrofit providesSmashRosterRetrofit(final MoshiConverterFactory moshiConverterFactory,
            final OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(moshiConverterFactory)
                .baseUrl(mSmashRosterBasePath)
                .client(okHttpClient)
                .build();
    }

    @NonNull
    @Provides
    @Singleton
    SmashRosterPreferenceStore providesSmashRosterPreferenceStore(final Moshi moshi,
            @Named(SMASH_ROSTER_KEY_VALUE_STORE) final KeyValueStore keyValueStore) {
        return new SmashRosterPreferenceStoreImpl(keyValueStore, moshi);
    }

    @NonNull
    @Provides
    @Singleton
    SmashRosterStorage providesSmashRosterStorage(final Moshi moshi,
            final KeyValueStoreProvider keyValueStoreProvider, final Timber timber) {
        return new SmashRosterStorageImpl(keyValueStoreProvider, moshi,
                mApplication.getPackageName(), timber);
    }

    @NonNull
    @Provides
    @Singleton
    SmashRosterSyncManager providesSmashRosterSyncManager(final ServerApi serverApi,
            final SmashRosterPreferenceStore smashRosterPreferenceStore,
            final SmashRosterStorage smashRosterStorage,
            final ThreadUtils threadUtils,
            final Timber timber,
            final WorkManagerWrapper workManagerWrapper) {
        return new SmashRosterSyncManagerImpl(serverApi, smashRosterPreferenceStore,
                smashRosterStorage, threadUtils, timber, workManagerWrapper);
    }

    @NonNull
    @Provides
    @Singleton
    SplashScreenManager providesSplashScreenManager(final GeneralPreferenceStore generalPreferenceStore) {
        return new SplashScreenManagerImpl(generalPreferenceStore);
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
