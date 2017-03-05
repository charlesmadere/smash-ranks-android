package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.preferences.KeyValueStore;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FavoritePlayersManagerImpl implements FavoritePlayersManager {

    private static final String TAG = "FavoritePlayersManagerImpl";

    private final KeyValueStore mKeyValueStore;
    private final List<WeakReference<OnFavoritePlayersChangeListener>> mListeners;
    private final Timber mTimber;


    public FavoritePlayersManagerImpl(@NonNull final KeyValueStore keyValueStore,
            @NonNull final Timber timber) {
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

    }

    @Nullable
    @Override
    public List<AbsPlayer> getPlayers() {
        return null;
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

    }

}
