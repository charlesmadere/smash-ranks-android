package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.Region;

public interface IdentityManager {

    void addListener(@NonNull final OnIdentityChangeListener listener);

    @Nullable
    AbsPlayer getIdentity();

    boolean hasIdentity();

    boolean isId(@Nullable final String id);

    boolean isPlayer(@Nullable final AbsPlayer player);

    void removeIdentity();

    void removeListener(@Nullable final OnIdentityChangeListener listener);

    void setIdentity(@NonNull final AbsPlayer player, @NonNull final Region region);


    interface OnIdentityChangeListener {
        void onIdentityChange(final IdentityManager identityManager);
    }

}
