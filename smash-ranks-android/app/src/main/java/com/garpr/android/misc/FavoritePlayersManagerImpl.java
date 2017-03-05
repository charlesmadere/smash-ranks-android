package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.AbsPlayer;

import java.util.List;

public class FavoritePlayersManagerImpl implements FavoritePlayersManager {

    // TODO

    @Override
    public void addListener(@NonNull final OnFavoritePlayersChangeListener listener) {

    }

    @Override
    public void addPlayer(@NonNull final AbsPlayer player) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Nullable
    @Override
    public List<AbsPlayer> getPlayers() {
        return null;
    }

    @Override
    public void removeListener(@Nullable final OnFavoritePlayersChangeListener listener) {

    }

    @Override
    public void removePlayer(@NonNull final AbsPlayer player) {

    }

}
