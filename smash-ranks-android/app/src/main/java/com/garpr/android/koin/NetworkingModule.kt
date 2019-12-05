package com.garpr.android.koin

import com.garpr.android.misc.Constants
import com.garpr.android.networking.GarPrApi
import com.garpr.android.networking.ServerApi
import com.garpr.android.networking.ServerApiImpl
import com.garpr.android.networking.SmashRosterApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

val networkingModule = module {

    single {
        val moshi: Moshi = get()
        MoshiConverterFactory.create(moshi)
    }

    single {
        OkHttpClient.Builder()
                .build()
    }

    single<GarPrApi>(named(GAR_PR_API)) {
        val retrofit: Retrofit = get(named(GAR_PR_RETROFIT))
        retrofit.create()
    }

    single(named(GAR_PR_RETROFIT)) {
        val rxJava2CallAdapterFactory: RxJava2CallAdapterFactory = get()
        val moshiConverterFactory: MoshiConverterFactory = get()
        val okHttpClient: OkHttpClient = get()

        Retrofit.Builder()
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(moshiConverterFactory)
                .baseUrl("${Constants.GAR_PR_BASE_PATH}:${Constants.GAR_PR_API_PORT}")
                .client(okHttpClient)
                .build()
    }

    single<GarPrApi>(named(NOT_GAR_PR_API)) {
        val retrofit: Retrofit = get(named(NOT_GAR_PR_RETROFIT))
        retrofit.create()
    }

    single(named(NOT_GAR_PR_RETROFIT)) {
        val rxJava2CallAdapterFactory: RxJava2CallAdapterFactory = get()
        val moshiConverterFactory: MoshiConverterFactory = get()
        val okHttpClient: OkHttpClient = get()

        Retrofit.Builder()
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(moshiConverterFactory)
                .baseUrl("${Constants.NOT_GAR_PR_BASE_PATH}:${Constants.NOT_GAR_PR_API_PORT}")
                .client(okHttpClient)
                .build()
    }

    single { RxJava2CallAdapterFactory.create() }

    single(named(SMASH_ROSTER_RETROFIT)) {
        val rxJava2CallAdapterFactory: RxJava2CallAdapterFactory = get()
        val moshiConverterFactory: MoshiConverterFactory = get()
        val okHttpClient: OkHttpClient = get()

        Retrofit.Builder()
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(moshiConverterFactory)
                .baseUrl(Constants.SMASH_ROSTER_BASE_PATH)
                .client(okHttpClient)
                .build()
    }

    single<ServerApi> {
        val garPrApi: GarPrApi = get(named(GAR_PR_API))
        val notGarPrApi: GarPrApi = get(named(NOT_GAR_PR_API))
        ServerApiImpl(garPrApi, notGarPrApi, get())
    }

    single<SmashRosterApi> {
        val retrofit: Retrofit = get(named(SMASH_ROSTER_RETROFIT))
        retrofit.create()
    }

}
