package com.garpr.android.misc;


import android.content.Context;

import com.garpr.android.App;
import com.garpr.android.models.Favorites;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public final class FavoritesStore {


    private static final String FILE_NAME = "FavoritesStore.json";
    private static final String TAG = "FavoritesStore";




    private static FileInputStream read() throws FileNotFoundException {
        return App.get().openFileInput(FILE_NAME);
    }


    public static void read(final Response<Favorites> response) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!response.isAlive()) {
                    return;
                }

                final FileInputStream fis;

                try {
                    fis = read();
                } catch (final FileNotFoundException e) {
                    Console.d(TAG, "Failed opening of \"" + FILE_NAME + '"', e);

                    if (response.isAlive()) {
                        response.success(null);
                    }

                    return;
                }

                // TODO

                try {
                    fis.close();
                } catch (final IOException e) {
                    Console.w(TAG, "Read but failed closing of \"" + FILE_NAME + '"', e);
                }

                if (response.isAlive()) {

                }
            }
        };

        submit(runnable);
    }


    private static void submit(final Runnable runnable) {
        App.getExecutorService().submit(runnable);
    }


    private static FileOutputStream write() throws FileNotFoundException {
        return App.get().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
    }


    public static void write(final Favorites favorites) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final FileOutputStream fos;

                try {
                    fos = write();
                } catch (final FileNotFoundException e) {
                    Console.e(TAG, "Failed writing of \"" + FILE_NAME + '"', e);
                    return;
                }

                final JSONObject favoritesJSON = favorites.toJSON();

                // TODO

                try {
                    fos.close();
                } catch (final IOException e) {
                    Console.e(TAG, "Wrote but failed closing of \"" + FILE_NAME + '"', e);
                }
            }
        };

        submit(runnable);
    }


}
