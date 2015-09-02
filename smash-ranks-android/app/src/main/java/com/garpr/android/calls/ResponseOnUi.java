package com.garpr.android.calls;


import com.garpr.android.misc.HeartbeatWithUi;


/**
 * The result of an API call that must respond on the UI thread will be delivered here
 */
public abstract class ResponseOnUi<T> extends Response<T> implements HeartbeatWithUi {


    /**
     * see the super constructor
     */
    public ResponseOnUi(final String tag, final HeartbeatWithUi heartbeat) throws
            IllegalArgumentException {
        super(tag, heartbeat);
    }


    @Override
    public final void error(final Exception e) {
        final Runnable action = new Runnable() {
            @Override
            public void run() {
                if (isAlive()) {
                    errorOnUi(e);
                }
            }
        };

        runOnUi(action);
    }


    /**
     * a major error has occurred
     */
    public abstract void errorOnUi(final Exception e);


    /**
     * Runs the given {@link Runnable} on the UI thread if the {@link HeartbeatWithUi} is still
     * alive
     */
    @Override
    public final void runOnUi(final Runnable action) {
        final HeartbeatWithUi heartbeat = (HeartbeatWithUi) mHeartbeat.get();

        if (heartbeat != null && heartbeat.isAlive()) {
            heartbeat.runOnUi(action);
        }
    }


    @Override
    public final void success(final T object) {
        final Runnable action = new Runnable() {
            @Override
            public void run() {
                if (isAlive()) {
                    successOnUi(object);
                }
            }
        };

        runOnUi(action);
    }


    /**
     * Success! The API call has completed without any issue
     */
    public abstract void successOnUi(final T object);


}
