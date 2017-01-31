package com.garpr.android.dagger;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.garpr.android.misc.Timber;
import com.garpr.android.misc.TimberImpl;
import com.garpr.android.models.Ratings;
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
    private final String mGarPrUrl;


    public AppModule(@NonNull final Application application, @NonNull final String garPrUrl) {
        mApplication = application;
        mGarPrUrl = garPrUrl;
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
                .registerTypeAdapter(Ratings.class, Ratings.JSON_DESERIALIZER)
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
                .baseUrl(mGarPrUrl)
                .build();
    }

    @Provides
    @Singleton
    ServerApi providesServerApi(final GarPrApi garPrApi) {
        return new ServerApiImpl(garPrApi);
    }

    @Provides
    @Singleton
    Timber providesTimber() {
        return new TimberImpl();
    }

}
