package com.garpr.android.dagger;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.garpr.android.models.SimpleDate;
import com.garpr.android.networking.GarPrApi;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.networking.ServerApiImpl;
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


    public AppModule(@NonNull final Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return mApplication;
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
                .registerTypeAdapter(SimpleDate.class, SimpleDate.JSON_DESERIALIZER)
                .create();
    }

    @Provides
    @Singleton
    PreferenceStore providesPreferenceStore() {
        return new PreferenceStoreImpl(mApplication);
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(final Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://www.garpr.com:3001/")
                .build();
    }

    @Provides
    @Singleton
    ServerApi providesServerApi() {
        return new ServerApiImpl();
    }

}
