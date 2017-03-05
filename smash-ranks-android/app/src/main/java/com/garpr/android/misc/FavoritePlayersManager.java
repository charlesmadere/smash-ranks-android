package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.AbsPlayer;

import java.util.List;

public interface FavoritePlayersManager {

    void addListener(@NonNull final OnFavoritePlayersChangeListener listener);

    void addPlayer(@NonNull final AbsPlayer player);

    boolean isEmpty();

    @Nullable
    List<AbsPlayer> getPlayers();

    void removeListener(@Nullable final OnFavoritePlayersChangeListener listener);

    void removePlayer(@NonNull final AbsPlayer player);


    interface OnFavoritePlayersChangeListener {
        void onFavoritePlayersChanged(final FavoritePlayersManager manager);
    }

}
