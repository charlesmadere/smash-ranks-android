package com.garpr.android.dagger

import android.app.Application
import com.garpr.android.data.converters.AbsPlayerConverter
import com.garpr.android.data.converters.AbsRegionConverter
import com.garpr.android.data.converters.AbsTournamentConverter
import com.garpr.android.data.converters.MatchConverter
import com.garpr.android.data.converters.RankedPlayerConverter
import com.garpr.android.data.converters.SimpleDateConverter
import com.garpr.android.data.models.Region
import com.garpr.android.features.home.HomeToolbarManager
import com.garpr.android.features.home.HomeToolbarManagerImpl
import com.garpr.android.features.notifications.NotificationsManager
import com.garpr.android.features.notifications.NotificationsManagerImpl
import com.garpr.android.features.player.PlayerProfileManager
import com.garpr.android.features.player.PlayerProfileManagerImpl
import com.garpr.android.features.player.SmashRosterAvatarUrlHelper
import com.garpr.android.features.player.SmashRosterAvatarUrlHelperImpl
import com.garpr.android.features.rankings.PreviousRankUtils
import com.garpr.android.features.rankings.PreviousRankUtilsImpl
import com.garpr.android.managers.AppUpgradeManager
import com.garpr.android.managers.AppUpgradeManagerImpl
import com.garpr.android.misc.Constants
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.SchedulersImpl
import com.garpr.android.misc.ShareUtils
import com.garpr.android.misc.ShareUtilsImpl
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.misc.TimberImpl
import com.garpr.android.networking.GarPrApi
import com.garpr.android.networking.ServerApi
import com.garpr.android.networking.ServerApiImpl
import com.garpr.android.networking.SmashRosterApi
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.GeneralPreferenceStoreImpl
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.preferences.KeyValueStoreProviderImpl
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.preferences.RankingsPollingPreferenceStoreImpl
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.preferences.SmashRosterPreferenceStoreImpl
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.FavoritePlayersRepositoryImpl
import com.garpr.android.repositories.HeadToHeadRepository
import com.garpr.android.repositories.HeadToHeadRepositoryImpl
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.IdentityRepositoryImpl
import com.garpr.android.repositories.MatchesRepository
import com.garpr.android.repositories.MatchesRepositoryImpl
import com.garpr.android.repositories.NightModeRepository
import com.garpr.android.repositories.NightModeRepositoryImpl
import com.garpr.android.repositories.PlayerMatchesRepository
import com.garpr.android.repositories.PlayerMatchesRepositoryImpl
import com.garpr.android.repositories.PlayersRepository
import com.garpr.android.repositories.PlayersRepositoryImpl
import com.garpr.android.repositories.RankingsRepository
import com.garpr.android.repositories.RankingsRepositoryImpl
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.repositories.RegionRepositoryImpl
import com.garpr.android.repositories.RegionsRepository
import com.garpr.android.repositories.RegionsRepositoryImpl
import com.garpr.android.repositories.TournamentsRepository
import com.garpr.android.repositories.TournamentsRepositoryImpl
import com.garpr.android.sync.rankings.RankingsNotificationsUtils
import com.garpr.android.sync.rankings.RankingsNotificationsUtilsImpl
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.rankings.RankingsPollingManagerImpl
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterStorageImpl
import com.garpr.android.sync.roster.SmashRosterSyncManager
import com.garpr.android.sync.roster.SmashRosterSyncManagerImpl
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(
        private val application: Application,
        private val defaultRegion: Region,
        private val smashRosterBasePath: String
) {

    companion object {
        private const val FAVORITE_PLAYERS_KEY_VALUE_STORE = "FAVORITE_PLAYERS_KEY_VALUE_STORE"
        private const val GAR_PR_API = "GAR_PR_API"
        private const val GAR_PR_RETROFIT = "GAR_PR_RETROFIT"
        private const val GENERAL_KEY_VALUE_STORE = "GENERAL_KEY_VALUE_STORE"
        private const val NOT_GAR_PR_API = "NOT_GAR_PR_API"
        private const val NOT_GAR_PR_RETROFIT = "NOT_GAR_PR_RETROFIT"
        private const val RANKINGS_POLLING_KEY_VALUE_STORE = "RANKINGS_POLLING_KEY_VALUE_STORE"
        private const val SMASH_ROSTER_KEY_VALUE_STORE = "SMASH_ROSTER_KEY_VALUE_STORE"
        private const val SMASH_ROSTER_RETROFIT = "SMASH_ROSTER_RETROFIT"
    }

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun providesAppUpgradeManager(
            favoritePlayersRepository: FavoritePlayersRepository,
            generalPreferenceStore: GeneralPreferenceStore,
            timber: Timber
    ): AppUpgradeManager {
        return AppUpgradeManagerImpl(favoritePlayersRepository, generalPreferenceStore, timber)
    }

    @Named(FAVORITE_PLAYERS_KEY_VALUE_STORE)
    @Provides
    @Singleton
    fun providesFavoritePlayersKeyValueStore(
            keyValueStoreProvider: KeyValueStoreProvider
    ): KeyValueStore {
        return keyValueStoreProvider.getKeyValueStore(
                "${application.packageName}.Preferences.v2.FavoritePlayers")
    }

    @Provides
    @Singleton
    fun providesFavoritePlayersRepository(
            @Named(FAVORITE_PLAYERS_KEY_VALUE_STORE) keyValueStore: KeyValueStore,
            moshi: Moshi,
            timber: Timber
    ): FavoritePlayersRepository {
        return FavoritePlayersRepositoryImpl(keyValueStore, moshi, timber)
    }

    @Named(GAR_PR_API)
    @Provides
    @Singleton
    fun providesGarPrApi(
            @Named(GAR_PR_RETROFIT) retrofit: Retrofit
    ): GarPrApi {
        return retrofit.create()
    }

    @Named(GAR_PR_RETROFIT)
    @Provides
    @Singleton
    fun providesGarPrRetrofit(
            moshiConverterFactory: MoshiConverterFactory,
            okHttpClient: OkHttpClient,
            rxJava2CallAdapterFactoryFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(rxJava2CallAdapterFactoryFactory)
                .addConverterFactory(moshiConverterFactory)
                .baseUrl("${Constants.GAR_PR_BASE_PATH}:${Constants.GAR_PR_API_PORT}")
                .client(okHttpClient)
                .build()
    }

    @Named(GENERAL_KEY_VALUE_STORE)
    @Provides
    @Singleton
    fun providesGeneralKeyValueStore(
            keyValueStoreProvider: KeyValueStoreProvider
    ): KeyValueStore {
        return keyValueStoreProvider.getKeyValueStore(
                "${application.packageName}.Preferences.v2.General")
    }

    @Provides
    @Singleton
    fun providesGeneralPreferenceStore(
            @Named(GENERAL_KEY_VALUE_STORE) keyValueStore: KeyValueStore,
            moshi: Moshi
    ): GeneralPreferenceStore {
        return GeneralPreferenceStoreImpl(keyValueStore, moshi, defaultRegion)
    }

    @Provides
    @Singleton
    fun providesHeadToHeadRepository(
            schedulers: Schedulers,
            serverApi: ServerApi
    ): HeadToHeadRepository {
        return HeadToHeadRepositoryImpl(schedulers, serverApi)
    }

    @Provides
    @Singleton
    fun providesHomeToolbarManager(
            identityRepository: IdentityRepository
    ): HomeToolbarManager {
        return HomeToolbarManagerImpl(identityRepository)
    }

    @Provides
    @Singleton
    fun providesIdentityRepository(
            generalPreferenceStore: GeneralPreferenceStore,
            timber: Timber
    ): IdentityRepository {
        return IdentityRepositoryImpl(generalPreferenceStore.identity, timber)
    }

    @Provides
    @Singleton
    fun providesKeyValueStoreProvider(application: Application): KeyValueStoreProvider {
        return KeyValueStoreProviderImpl(application)
    }

    @Provides
    @Singleton
    fun providesMatchesRepository(
            schedulers: Schedulers,
            serverApi: ServerApi
    ): MatchesRepository {
        return MatchesRepositoryImpl(schedulers, serverApi)
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
                .add(AbsPlayerConverter)
                .add(AbsRegionConverter)
                .add(AbsTournamentConverter)
                .add(MatchConverter)
                .add(RankedPlayerConverter)
                .add(SimpleDateConverter)
                .build()
    }

    @Provides
    @Singleton
    fun providesMoshiConverterFactory(
            moshi: Moshi
    ): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun providesNightModeRepository(
            generalPreferenceStore: GeneralPreferenceStore,
            timber: Timber
    ): NightModeRepository {
        return NightModeRepositoryImpl(generalPreferenceStore, timber)
    }

    @Named(NOT_GAR_PR_API)
    @Provides
    @Singleton
    fun providesNotGarPrApi(
            @Named(NOT_GAR_PR_RETROFIT) retrofit: Retrofit
    ): GarPrApi {
        return retrofit.create()
    }

    @Named(NOT_GAR_PR_RETROFIT)
    @Provides
    @Singleton
    fun providesNotGarPrRetrofit(
            moshiConverterFactory: MoshiConverterFactory,
            okHttpClient: OkHttpClient,
            rxJava2CallAdapterFactoryFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(rxJava2CallAdapterFactoryFactory)
                .addConverterFactory(moshiConverterFactory)
                .baseUrl("${Constants.NOT_GAR_PR_BASE_PATH}:${Constants.NOT_GAR_PR_API_PORT}")
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun providesNotificationManager(
            rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
            regionRepository: RegionRepository,
            timber: Timber
    ): NotificationsManager {
        return NotificationsManagerImpl(application, rankingsPollingPreferenceStore,
                regionRepository, timber)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .build()
    }

    @Provides
    @Singleton
    fun providesPlayerMatchesRepository(
            matchesRepository: MatchesRepository,
            playersRepository: PlayersRepository,
            schedulers: Schedulers
    ): PlayerMatchesRepository {
        return PlayerMatchesRepositoryImpl(matchesRepository, playersRepository, schedulers)
    }

    @Provides
    @Singleton
    fun providesPlayerProfileViewManager(
            identityRepository: IdentityRepository,
            smashRosterAvatarUrlHelper: SmashRosterAvatarUrlHelper
    ): PlayerProfileManager {
        return PlayerProfileManagerImpl(application, identityRepository,
                smashRosterAvatarUrlHelper)
    }

    @Provides
    @Singleton
    fun providesPlayersRepository(
            schedulers: Schedulers,
            serverApi: ServerApi
    ): PlayersRepository {
        return PlayersRepositoryImpl(schedulers, serverApi)
    }

    @Provides
    @Singleton
    fun providesPreviousRankUtils(): PreviousRankUtils {
        return PreviousRankUtilsImpl()
    }

    @Provides
    @Singleton
    fun providesRankingNotificationsUtils(
            deviceUtils: DeviceUtils,
            rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
            timber: Timber
    ): RankingsNotificationsUtils {
        return RankingsNotificationsUtilsImpl(deviceUtils, rankingsPollingPreferenceStore,
                timber)
    }

    @Named(RANKINGS_POLLING_KEY_VALUE_STORE)
    @Provides
    @Singleton
    fun providesRankingsPollingKeyValueStore(
            keyValueStoreProvider: KeyValueStoreProvider
    ): KeyValueStore {
        return keyValueStoreProvider.getKeyValueStore(
                "${application.packageName}.Preferences.v2.RankingsPolling")
    }

    @Provides
    @Singleton
    fun providesRankingsPollingPreferenceStore(
            moshi: Moshi,
            @Named(RANKINGS_POLLING_KEY_VALUE_STORE) keyValueStore: KeyValueStore
    ): RankingsPollingPreferenceStore {
        return RankingsPollingPreferenceStoreImpl(keyValueStore, moshi)
    }

    @Provides
    @Singleton
    fun providesRankingsPollingSyncManager(
            rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
            timber: Timber,
            workManagerWrapper: WorkManagerWrapper
    ): RankingsPollingManager {
        return RankingsPollingManagerImpl(rankingsPollingPreferenceStore, timber,
                workManagerWrapper)
    }

    @Provides
    @Singleton
    fun providesRankingsRepository(
            notificationsManager: NotificationsManager,
            rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
            regionRepository: RegionRepository,
            schedulers: Schedulers,
            serverApi: ServerApi
    ): RankingsRepository {
        return RankingsRepositoryImpl(notificationsManager, rankingsPollingPreferenceStore,
                regionRepository, schedulers, serverApi)
    }

    @Provides
    @Singleton
    fun providesRegionRepository(
            generalPreferenceStore: GeneralPreferenceStore,
            rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
            timber: Timber
    ): RegionRepository {
        return RegionRepositoryImpl(generalPreferenceStore, rankingsPollingPreferenceStore, timber)
    }

    @Provides
    @Singleton
    fun providesRegionsRepository(
            schedulers: Schedulers,
            serverApi: ServerApi
    ): RegionsRepository {
        return RegionsRepositoryImpl(schedulers, serverApi)
    }

    @Provides
    @Singleton
    fun providesRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun providesSchedulers(
            threadUtils: ThreadUtils
    ): Schedulers {
        return SchedulersImpl(threadUtils)
    }

    @Provides
    @Singleton
    fun providesServerApi2(
            @Named(GAR_PR_API) garPrApi: GarPrApi,
            @Named(NOT_GAR_PR_API) notGarPrApi: GarPrApi,
            smashRosterApi: SmashRosterApi
    ): ServerApi {
        return ServerApiImpl(garPrApi, notGarPrApi, smashRosterApi)
    }

    @Provides
    @Singleton
    fun providesShareUtils(
            regionRepository: RegionRepository,
            timber: Timber
    ): ShareUtils {
        return ShareUtilsImpl(regionRepository, timber)
    }

    @Provides
    @Singleton
    fun providesSmashRosterApi(
            @Named(SMASH_ROSTER_RETROFIT) retrofit: Retrofit
    ): SmashRosterApi {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun providesSmashRosterAvatarUrlHelper(): SmashRosterAvatarUrlHelper {
        return SmashRosterAvatarUrlHelperImpl(smashRosterBasePath)
    }

    @Named(SMASH_ROSTER_KEY_VALUE_STORE)
    @Provides
    @Singleton
    fun providesSmashRosterKeyValueStore(
            keyValueStoreProvider: KeyValueStoreProvider
    ): KeyValueStore {
        return keyValueStoreProvider.getKeyValueStore(
                "${application.packageName}.Preferences.v2.SmashRoster")
    }

    @Named(SMASH_ROSTER_RETROFIT)
    @Provides
    @Singleton
    fun providesSmashRosterRetrofit(
            moshiConverterFactory: MoshiConverterFactory,
            okHttpClient: OkHttpClient,
            rxJava2CallAdapterFactoryFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(rxJava2CallAdapterFactoryFactory)
                .addConverterFactory(moshiConverterFactory)
                .baseUrl(smashRosterBasePath)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun providesSmashRosterPreferenceStore(
            @Named(SMASH_ROSTER_KEY_VALUE_STORE) keyValueStore: KeyValueStore,
            moshi: Moshi
    ): SmashRosterPreferenceStore {
        return SmashRosterPreferenceStoreImpl(keyValueStore, moshi)
    }

    @Provides
    @Singleton
    fun providesSmashRosterStorage(
            keyValueStoreProvider: KeyValueStoreProvider,
            moshi: Moshi,
            timber: Timber
    ): SmashRosterStorage {
        return SmashRosterStorageImpl(keyValueStoreProvider, moshi, application.packageName,
                timber)
    }

    @Provides
    @Singleton
    fun providesSmashRosterSyncManager(
            schedulers: Schedulers,
            serverApi: ServerApi,
            smashRosterPreferenceStore: SmashRosterPreferenceStore,
            smashRosterStorage: SmashRosterStorage,
            timber: Timber,
            workManagerWrapper: WorkManagerWrapper
    ): SmashRosterSyncManager {
        return SmashRosterSyncManagerImpl(schedulers, serverApi, smashRosterPreferenceStore,
                smashRosterStorage, timber, workManagerWrapper)
    }

    @Provides
    @Singleton
    fun providesTimber(
            deviceUtils: DeviceUtils,
            crashlyticsWrapper: CrashlyticsWrapper
    ): Timber {
        return TimberImpl(deviceUtils, crashlyticsWrapper)
    }

    @Provides
    @Singleton
    fun providesTournamentsRepository(
            schedulers: Schedulers,
            serverApi: ServerApi
    ): TournamentsRepository {
        return TournamentsRepositoryImpl(schedulers, serverApi)
    }

}
