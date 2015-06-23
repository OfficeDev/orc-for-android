/*******************************************************************************
 * Copyright (c) Microsoft Open Technologies, Inc.
 * All Rights Reserved
 * See License.txt in the project root for license information.
 ******************************************************************************/
package com.microsoft.services.orc.http.impl;

import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.services.orc.http.BaseHttpTransport;
import com.microsoft.services.orc.http.NetworkRunnable;
import com.microsoft.services.orc.http.Request;
import com.microsoft.services.orc.http.Response;

/**
 * The type Android http transport.
 */
public class AndroidHttpTransport extends BaseHttpTransport {

    @Override
    protected NetworkRunnable createNetworkRunnable(Request request, SettableFuture<Response> future) {
        return new AndroidNetworkRunnable(request, future);
    }
}
