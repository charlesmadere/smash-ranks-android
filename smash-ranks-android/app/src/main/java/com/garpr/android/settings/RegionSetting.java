package com.garpr.android.settings;


import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;


public final class RegionSetting extends Setting<Region> {


    private static final String TAG = "RegionSetting";

    private final JSONSetting mJSONSetting;
    private final LinkedList<Attachment> mListeners;




    RegionSetting(final String name, final String key) {
        super(name, key);
        mJSONSetting = new JSONSetting(name, key);
        mListeners = new LinkedList<>();
    }


    public void attachListener(final Heartbeat heartbeat, final RegionListener listener) {
        if (Utils.areAnyObjectsNull(heartbeat, listener)) {
            throw new RuntimeException();
        }

        synchronized (mListeners) {
            boolean attachListener = true;
            final Iterator<Attachment> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final Attachment attachment = iterator.next();

                if (attachment.isAlive()) {
                    if (attachment.hasListener(listener)) {
                        attachListener = false;
                    }
                } else {
                    iterator.remove();
                }
            }

            if (!heartbeat.isAlive()) {
                Console.w(TAG, "Attempted to attach a listener (" + listener.toString()
                        + ") with a dead heartbeat");
                return;
            }

            if (attachListener) {
                mListeners.add(new Attachment(heartbeat, listener));
                Console.d(TAG, "Attached " + listener.toString() + ", there are now "
                        + mListeners.size() + " region listener(s)");
            } else {
                Console.d(TAG, "Didn't need to attach " + listener.toString() +
                        " as it was already attached");
            }
        }
    }


    @Override
    public void delete() {
        throw new UnsupportedOperationException();
    }


    public void detachListener(final RegionListener listener) {
        synchronized (mListeners) {
            final Iterator<Attachment> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final Attachment attachment = iterator.next();

                if (!attachment.isAlive() || attachment.hasListener(listener)) {
                    iterator.remove();
                }
            }

            if (listener == null) {
                Console.d(TAG, "There are now " + mListeners.size() + " region listener(s)");
            } else {
                Console.d(TAG, "Detached " + listener.toString() + ", there are now "
                        + mListeners.size() + " region listener(s)");
            }
        }
    }


    @Override
    public Region get() {
        final JSONObject json = mJSONSetting.get();
        final Region region;

        if (json == null) {
            region = User.Region.get();
            set(region);
        } else {
            try {
                region = new Region(json);
            } catch (final JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return region;
    }


    @Override
    public void set(final Region newValue) {
        if (newValue == null) {
            throw new IllegalArgumentException("newValue can't be null");
        }

        final JSONObject json = newValue.toJSON();
        Console.d(TAG, "Region is now " + json.toString());
        CrashlyticsManager.setString(Constants.REGION, json.toString());
        mJSONSetting.set(json);
    }


    public void set(final Region newValue, final boolean notifyListeners) {
        set(newValue);

        if (notifyListeners) {
            synchronized (mListeners) {
                final Iterator<Attachment> iterator = mListeners.iterator();

                while (iterator.hasNext()) {
                    final Attachment attachment = iterator.next();

                    if (attachment.isAlive()) {
                        attachment.onRegionChanged(newValue);
                    } else {
                        iterator.remove();
                    }
                }
            }
        }
    }




    private final static class Attachment implements Heartbeat, RegionListener {


        private final WeakReference<Heartbeat> mHeartbeat;
        private final WeakReference<RegionListener> mListener;


        private Attachment(final Heartbeat heartbeat, final RegionListener listener) {
            mHeartbeat = new WeakReference<>(heartbeat);
            mListener = new WeakReference<>(listener);
        }


        private boolean hasListener(final RegionListener listener) {
            return mListener.get() == listener;
        }


        @Override
        public boolean isAlive() {
            final Heartbeat heartbeat = mHeartbeat.get();
            return heartbeat != null && heartbeat.isAlive() && mListener.get() != null;
        }


        @Override
        public void onRegionChanged(final Region region) {
            if (!isAlive()) {
                return;
            }

            final RegionListener listener = mListener.get();

            if (listener != null) {
                listener.onRegionChanged(region);
            }
        }


    }


    public interface RegionListener {


        void onRegionChanged(final Region region);


    }


}
