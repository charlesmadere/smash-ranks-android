package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.preferences.GeneralPreferenceStore;

public class IdentityManagerImpl implements IdentityManager {

    private final GeneralPreferenceStore mGeneralPreferenceStore;


    public IdentityManagerImpl(@NonNull final GeneralPreferenceStore generalPreferenceStore) {
        mGeneralPreferenceStore = generalPreferenceStore;
    }

    @Override
    public void delete() {
        mGeneralPreferenceStore.getIdentity().delete();
    }

    @Nullable
    @Override
    public AbsPlayer get() {
        return mGeneralPreferenceStore.getIdentity().get();
    }

    @Override
    public boolean hasIdentity() {
        return mGeneralPreferenceStore.getIdentity().exists();
    }

    @Override
    public boolean isId(@Nullable final String id) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }

        final AbsPlayer identity = get();
        return identity != null && identity.getId().equals(id);

    }

    @Override
    public boolean isPlayer(@Nullable final AbsPlayer player) {
        if (player == null) {
            return false;
        }

        final AbsPlayer identity = get();
        return identity != null && identity.equals(player);
    }

    @Override
    public void set(@Nullable final AbsPlayer player) {
        if (player == null) {
            delete();
        } else {
            mGeneralPreferenceStore.getIdentity().set(player);
        }
    }

}
