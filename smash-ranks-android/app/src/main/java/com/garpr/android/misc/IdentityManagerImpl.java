package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.preferences.Preference;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IdentityManagerImpl implements IdentityManager {

    private static final String TAG = "IdentityManagerImpl";

    private final List<WeakReference<OnIdentityChangeListener>> mListeners;
    private final Preference<AbsPlayer> mIdentity;
    private final Timber mTimber;


    public IdentityManagerImpl(@NonNull final Preference<AbsPlayer> identity,
            @NonNull final Timber timber) {
        mListeners = new LinkedList<>();
        mIdentity = identity;
        mTimber = timber;
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
    public AbsPlayer getIdentity() {
        return mIdentity.get();
    }

    @NonNull
    private String getPlayerString(@Nullable final AbsPlayer player) {
        if (player == null) {
            return "null";
        } else {
            return "(id:" + player.getId() + ") (name:" + player.getName() + ")";
        }
    }

    @Override
    public boolean hasIdentity() {
        return mIdentity.exists();
    }

    @Override
    public boolean isId(@Nullable final String id) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }

        final AbsPlayer identity = getIdentity();
        return identity != null && identity.getId().equals(id);
    }

    @Override
    public boolean isPlayer(@Nullable final AbsPlayer player) {
        if (player == null) {
            return false;
        }

        final AbsPlayer identity = getIdentity();
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
    public void setIdentity(@Nullable final AbsPlayer player) {
        mTimber.d(TAG, "old identity is \"" + getPlayerString(getIdentity()) + "\"" +
                ", new identity is \"" + getPlayerString(player) + "\"");

        mIdentity.set(player);
        notifyListeners();
    }

}
