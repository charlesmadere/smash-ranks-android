package com.garpr.android.misc;


import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * This class acts as a bridge between Google's Volley library and Square's OkHTTP library.
 * Generally using OkHTTP would be unnecessary, but the native {@link java.net.HttpURLConnection}
 * class doesn't support HTTP PATCH...
 *
 * This is talked about in a bit more detail on Stack Overflow:
 * https://stackoverflow.com/questions/24375043/how-to-implement-android-volley-with-okhttp-2-0
 */
public final class OkHttpStack extends HurlStack {


    private final OkUrlFactory mFactory;




    public OkHttpStack() {
        mFactory = new OkUrlFactory(new OkHttpClient());
    }


    @Override
    protected HttpURLConnection createConnection(final URL url) throws IOException {
        return mFactory.open(url);
    }


}
