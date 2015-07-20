package com.microsoft.services.orc.http.impl;

import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.services.orc.http.NetworkRunnable;
import com.microsoft.services.orc.http.Request;
import com.microsoft.services.orc.http.Response;


public class OkHttpNetworkRunnable extends NetworkRunnable {
    public OkHttpNetworkRunnable(Request request, SettableFuture<Response> future) {
        super(request, future);
    }

    @Override
    public void run() {

    }
}
