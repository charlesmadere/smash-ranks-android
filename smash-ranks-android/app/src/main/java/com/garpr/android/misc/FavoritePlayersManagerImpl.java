package com.garpr.android.misc;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.garpr.android.R;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.preferences.KeyValueStore;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FavoritePlayersManagerImpl implements FavoritePlayersManager {

    private static final String TAG = "FavoritePlayersManagerImpl";

    private final Gson mGson;
    private final KeyValueStore mKeyValueStore;
    private final List<WeakReference<OnFavoritePlayersChangeListener>> mListeners;
    private final Timber mTimber;


    public FavoritePlayersManagerImpl(@NonNull final Gson gson,
            @NonNull final KeyValueStore keyValueStore, @NonNull final Timber timber) {
        mGson = gson;
        mKeyValueStore = keyValueStore;
        mListeners = new LinkedList<>();
        mTimber = timber;
    }

    @Override
    public void addListener(@NonNull final OnFavoritePlayersChangeListener listener) {
        synchronized (mListeners) {
            boolean addListener = true;
            final Iterator<WeakReference<OnFavoritePlayersChangeListener>> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnFavoritePlayersChangeListener> reference = iterator.next();
                final OnFavoritePlayersChangeListener item = reference.get();

                if (item == null) {
                    iterator.remove();
                } else if (item == listener) {
                    addListener = false;
                }
            }

            if (addListener) {
                mListeners.add(new WeakReference<>(listener));
            }
        }
    }

    @Override
    public void addPlayer(@NonNull final AbsPlayer player) {
        if (containsPlayer(player)) {
            mTimber.d(TAG, "Not adding favorite, it already exists in the store");
            return;
        }

        final List<AbsPlayer> players = getPlayers();
        mTimber.d(TAG, "Adding favorite (there are currently " +
                (players == null ? 0 : players.size()) + " favorite(s))");

        final String playerJson = mGson.toJson(player, AbsPlayer.class);
        mKeyValueStore.setString(player.getId(), playerJson);
        notifyListeners();
    }

    @Override
    public void clear() {
        mKeyValueStore.clear();
        notifyListeners();
    }

    @Override
    public boolean containsPlayer(@NonNull final AbsPlayer player) {
        return containsPlayer(player.getId());
    }

    @Override
    public boolean containsPlayer(@NonNull final String playerId) {
        return mKeyValueStore.contains(playerId);
    }

    @Nullable
    @Override
    public List<AbsPlayer> getPlayers() {
        final Map<String, ?> all = mKeyValueStore.getAll();

        if (all == null || all.isEmpty()) {
            return null;
        }

        final List<AbsPlayer> players = new ArrayList<>(all.size());

        for (final Map.Entry<String, ?> entry : all.entrySet()) {
            final String json = (String) entry.getValue();
            players.add(mGson.fromJson(json, AbsPlayer.class));
        }

        Collections.sort(players, AbsPlayer.ALPHABETICAL_ORDER);
        return players;
    }

    @Override
    public boolean isEmpty() {
        final Map<String, ?> all = mKeyValueStore.getAll();
        return all == null || all.isEmpty();
    }

    private void notifyListeners() {
        synchronized (mListeners) {
            final Iterator<WeakReference<OnFavoritePlayersChangeListener>> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnFavoritePlayersChangeListener> reference = iterator.next();
                final OnFavoritePlayersChangeListener item = reference.get();

                if (item == null) {
                    iterator.remove();
                } else {
                    item.onFavoritePlayersChanged(this);
                }
            }
        }
    }

    @Override
    public void removeListener(@Nullable final OnFavoritePlayersChangeListener listener) {
        synchronized (mListeners) {
            final Iterator<WeakReference<OnFavoritePlayersChangeListener>> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnFavoritePlayersChangeListener> reference = iterator.next();
                final OnFavoritePlayersChangeListener item = reference.get();

                if (item == null || item == listener) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void removePlayer(@NonNull final AbsPlayer player) {
        removePlayer(player.getId());
    }

    @Override
    public void removePlayer(@NonNull final String playerId) {
        mKeyValueStore.remove(playerId);
        notifyListeners();
    }

    @Override
    public boolean showAddOrRemovePlayerDialog(@NonNull Context context,
            @Nullable final AbsPlayer player) {
        if (player == null) {
            return false;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setNegativeButton(R.string.cancel, null);

        if (containsPlayer(player)) {
            builder.setMessage(context.getString(R.string.remove_x_from_favorites, player.getName()))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            removePlayer(player);
                        }
                    });
        } else {
            builder.setMessage(context.getString(R.string.add_x_to_favorites, player.getName()))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            addPlayer(player);
                        }
                    });
        }

        builder.show();
        return true;
    }

    @Override
    public int size() {
        final Map<String, ?> all = mKeyValueStore.getAll();
        return all == null ? 0 : all.size();
    }

}
