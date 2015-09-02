package com.garpr.android.calls;


import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.Utils;

import java.lang.ref.WeakReference;


/**
 * The result of an API call will be delivered here. Activities / Fragments / whatever that want
 * to receive the result of an API call must instantiate one of these (or one of its child classes).
 */
public abstract class Response<T> implements Heartbeat {


    private final String mTag;
    protected final WeakReference<Heartbeat> mHeartbeat;




    /**
     * constructor
     *
     * @param tag
     * The name of your Activity / Fragment / whatever. This should probably have been created
     * using "Blah.class.getSimpleName()".
     *
     * @param heartbeat
     * A {@link Heartbeat} so that we can tell if the class that wants to receive the result
     * of the API response is still alive.
     *
     * @throws IllegalArgumentException
     * If tag is an invalid String or heartbeat is null, then this Exception will be thrown.
     */
    public Response(final String tag, final Heartbeat heartbeat) throws IllegalArgumentException {
        if (!Utils.validStrings(tag) || heartbeat == null) {
            throw new IllegalArgumentException("tag (" + tag + ") and Heartbeat (" +
                    heartbeat + ") cannot be null!");
        }

        mTag = tag;
        mHeartbeat = new WeakReference<>(heartbeat);
    }


    public abstract void error(final Exception e);


    public final Heartbeat getHeartbeat() {
        return mHeartbeat.get();
    }


    /**
     * @return
     * Returns true if the class that wants to receive the result of the API response is still
     * alive
     */
    @Override
    public final boolean isAlive() {
        final Heartbeat heartbeat = getHeartbeat();
        return heartbeat != null && heartbeat.isAlive();
    }


    /**
     * Success! The API call has completed without any issue
     */
    public abstract void success(final T object);


    @Override
    public final String toString() {
        return mTag;
    }


}
