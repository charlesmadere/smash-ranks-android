package com.garpr.android.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.FavoritePlayer;
import com.garpr.android.models.Region;

import java.util.List;

public interface FavoritePlayersManager {

    void addListener(@NonNull final OnFavoritePlayersChangeListener listener);

    void addPlayer(@NonNull final AbsPlayer player, @NonNull final Region region);

    void clear();

    boolean containsPlayer(@NonNull final AbsPlayer player);

    boolean containsPlayer(@NonNull final String playerId);

    @Nullable
    List<AbsPlayer> getAbsPlayers();

    @Nullable
    List<FavoritePlayer> getPlayers();

    boolean isEmpty();

    void removeListener(@Nullable final OnFavoritePlayersChangeListener listener);

    void removePlayer(@NonNull final AbsPlayer player);

    void removePlayer(@NonNull final String playerId);

    boolean showAddOrRemovePlayerDialog(@NonNull Context context,
            @Nullable final AbsPlayer player, @NonNull final Region region);

    int size();


    interface OnFavoritePlayersChangeListener {
        void onFavoritePlayersChanged(final FavoritePlayersManager manager);
    }

}
