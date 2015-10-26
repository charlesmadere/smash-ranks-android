package com.garpr.android.misc;


import android.content.Context;

import com.garpr.android.App;
import com.garpr.android.models.Favorites;
import com.garpr.android.models.Player;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public final class FavoritesStore {


    private static final int BUFFER_SIZE = 1024;
    private static final String FILE_NAME = "FavoritesStore.json";
    private static final String TAG = "FavoritesStore";




    public static void contains(final Player player, final Response<Boolean> response) {
        read(new Response<Favorites>(TAG, response) {
            @Override
            public void error(final Exception e) {}


            @Override
            public void success(final Favorites favorites) {
                if (favorites == null || favorites.isEmpty()) {
                    response.success(Boolean.FALSE);
                } else {
                    response.success(favorites.contains(player));
                }
            }
        });
    }


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

                String favoritesString = null;

                try {
                    final byte[] buffer = new byte[BUFFER_SIZE];
                    final StringBuilder builder = new StringBuilder(BUFFER_SIZE);

                    while (fis.read(buffer) != -1 && response.isAlive()) {
                        builder.append(new String(buffer));
                    }

                    if (!response.isAlive()) {
                        return;
                    }

                    favoritesString = builder.toString();
                } catch (final IOException e) {
                    Console.e(TAG, "Failed reading in bytes of \"" + FILE_NAME + '"', e);
                }

                try {
                    fis.close();
                } catch (final IOException e) {
                    Console.w(TAG, "Read but failed closing of \"" + FILE_NAME + '"', e);
                }

                if (!response.isAlive()) {
                    return;
                }

                if (Utils.validStrings(favoritesString)) {
                    try {
                        final JSONObject favoritesJSON = new JSONObject(favoritesString);
                        final Favorites favorites = new Favorites(favoritesJSON);
                        response.success(favorites);
                    } catch (final JSONException e) {
                        // this should never happen
                        throw new RuntimeException(e);
                    }
                } else {
                    response.success(null);
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
                final String favoritesString = favoritesJSON.toString();
                final byte[] favoritesBytes = favoritesString.getBytes();

                try {
                    fos.write(favoritesBytes);
                    fos.flush();
                } catch (final IOException e) {
                    Console.e(TAG, "Failed writing out bytes of \"" + FILE_NAME + '"', e);
                }

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
