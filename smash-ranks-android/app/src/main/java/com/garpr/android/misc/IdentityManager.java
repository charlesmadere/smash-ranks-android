package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.AbsPlayer;

public interface IdentityManager {

    void addListener(@NonNull final OnIdentityChangeListener listener);

    @Nullable
    AbsPlayer getIdentity();

    boolean hasIdentity();

    boolean isId(@Nullable final String id);

    boolean isPlayer(@Nullable final AbsPlayer player);

    void removeListener(@Nullable final OnIdentityChangeListener listener);

    void setIdentity(@Nullable final AbsPlayer player);


    interface OnIdentityChangeListener {
        void onIdentityChange(final IdentityManager identityManager);
    }

}
