package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.preferences.GeneralPreferenceStore;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IdentityManagerImpl implements IdentityManager {

    private final GeneralPreferenceStore mGeneralPreferenceStore;
    private final List<WeakReference<OnIdentityChangeListener>> mListeners;


    public IdentityManagerImpl(@NonNull final GeneralPreferenceStore generalPreferenceStore) {
        mGeneralPreferenceStore = generalPreferenceStore;
        mListeners = new LinkedList<>();
    }

    @Override
    public void addListener(@NonNull final OnIdentityChangeListener listener) {
        synchronized (mListeners) {
            boolean addListener = true;
            final Iterator<WeakReference<OnIdentityChangeListener>> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnIdentityChangeListener> reference = iterator.next();
                final OnIdentityChangeListener item = reference.get();

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

    private void notifyListeners() {
        synchronized (mListeners) {
            final Iterator<WeakReference<OnIdentityChangeListener>> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnIdentityChangeListener> reference = iterator.next();
                final OnIdentityChangeListener item = reference.get();

                if (item == null) {
                    iterator.remove();
                } else {
                    item.onIdentityChange(this);
                }
            }
        }
    }

    @Override
    public void removeListener(@Nullable final OnIdentityChangeListener listener) {
        synchronized (mListeners) {
            final Iterator<WeakReference<OnIdentityChangeListener>> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnIdentityChangeListener> reference = iterator.next();
                final OnIdentityChangeListener item = reference.get();

                if (item == null || item == listener) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void set(@Nullable final AbsPlayer player) {
        mGeneralPreferenceStore.getIdentity().set(player);
        notifyListeners();
    }

}
