package com.garpr.android.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.AbsPlayer;

import java.util.List;

public interface FavoritePlayersManager {

    void addListener(@NonNull final OnFavoritePlayersChangeListener listener);

    void addPlayer(@NonNull final AbsPlayer player);

    void clear();

    boolean containsPlayer(@NonNull final AbsPlayer player);

    boolean containsPlayer(@NonNull final String playerId);

    @Nullable
    List<AbsPlayer> getPlayers();

    boolean isEmpty();

    void removeListener(@Nullable final OnFavoritePlayersChangeListener listener);

    void removePlayer(@NonNull final AbsPlayer player);

    void removePlayer(@NonNull final String playerId);

    boolean showAddOrRemovePlayerDialog(@NonNull Context context,
            @Nullable final AbsPlayer player);


    interface OnFavoritePlayersChangeListener {
        void onFavoritePlayersChanged(final FavoritePlayersManager manager);
    }

}
