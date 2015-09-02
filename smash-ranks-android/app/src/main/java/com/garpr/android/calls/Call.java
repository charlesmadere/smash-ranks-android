package com.garpr.android.calls;


import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.garpr.android.App;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.NetworkCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class Call<T> implements ErrorListener, Listener<JSONObject> {


    private static final ExecutorService EXECUTOR_SERVICE;

    private boolean mPulledFromNetworkCache;
    private final boolean mIgnoreCache;
    private String mUrl;
    protected final Response<T> mResponse;




    static {
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(3);
    }


    Call(final Response<T> response, final boolean ignoreCache) throws IllegalArgumentException {
        if (response == null) {
            throw new IllegalArgumentException("Response can't be null");
        }

        mResponse = response;
        mIgnoreCache = ignoreCache;
    }


    abstract String getCallName();


    String getUrl() {
        return Constants.API_URL + '/';
    }


    final void make() {
        if (!mResponse.isAlive()) {
            return;
        }

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Heartbeat heartbeat = mResponse.getHeartbeat();

                if (heartbeat != null && !heartbeat.isAlive()) {
                    return;
                }

                mUrl = getUrl();

                if (mIgnoreCache) {
                    makeNetworkRequest(heartbeat);
                } else {
                    final JSONObject response = NetworkCache.get(mUrl);
                    mPulledFromNetworkCache = response != null;

                    if (mPulledFromNetworkCache) {
                        Console.d(getCallName(), "Pulled call response from cache to " + mUrl);
                        onResponse(response);
                    } else {
                        makeNetworkRequest(heartbeat);
                    }
                }
            }
        };

        EXECUTOR_SERVICE.submit(runnable);
    }


    private void makeNetworkRequest(final Heartbeat heartbeat) {
        final JsonObjectRequest request = new JsonObjectRequest(mUrl, Call.this, Call.this);
        request.setTag(heartbeat);

        Console.d(getCallName(), "Making call to " + mUrl);

        final RequestQueue requestQueue = App.getRequestQueue();
        requestQueue.add(request);
    }


    @Override
    public final void onErrorResponse(final VolleyError error) {
        Console.e(getCallName(), "Network error", error);

        if (!mResponse.isAlive()) {
            return;
        }

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mResponse.isAlive()) {
                    mResponse.error(error);
                }
            }
        };

        EXECUTOR_SERVICE.submit(runnable);
    }


    abstract void onJSONResponse(final JSONObject json) throws JSONException;


    @Override
    public final void onResponse(final JSONObject response) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!mPulledFromNetworkCache) {
                    NetworkCache.set(mUrl, response);
                }

                if (mResponse.isAlive()) {
                    try {
                        onJSONResponse(response);
                    } catch (final JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        EXECUTOR_SERVICE.submit(runnable);
    }


    @Override
    public final String toString() {
        return getCallName();
    }


}
