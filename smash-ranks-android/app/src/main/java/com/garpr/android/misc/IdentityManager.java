package com.garpr.android.misc;

import android.support.annotation.Nullable;

import com.garpr.android.models.AbsPlayer;

public interface IdentityManager {

    void delete();

    @Nullable
    AbsPlayer get();

    boolean hasIdentity();

    boolean isId(@Nullable final String id);

    boolean isPlayer(@Nullable final AbsPlayer player);

    void set(@Nullable final AbsPlayer player);

}
