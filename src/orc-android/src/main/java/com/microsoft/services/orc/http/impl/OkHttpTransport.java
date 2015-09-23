package com.microsoft.services.orc.http.impl;

import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.services.orc.http.BaseHttpTransport;
import com.microsoft.services.orc.http.HttpTransport;
import com.microsoft.services.orc.http.NetworkRunnable;
import com.microsoft.services.orc.http.Request;
import com.microsoft.services.orc.http.Response;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;


/**
 * The type Ok http transport.
 */
public class OkHttpTransport extends BaseHttpTransport {

    private final static OkHttpClient client;

    static {
        client = new OkHttpClient();
    }

    @Override
    protected NetworkRunnable createNetworkRunnable(Request request, SettableFuture<Response> future) {
        return new OkHttpNetworkRunnable(client, request, future);
    }

    public OkHttpTransport setInterceptor(Interceptor interceptor) {
        client.interceptors().add(interceptor);
        return this;
    }
}

