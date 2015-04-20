/*******************************************************************************
 * Copyright (c) Microsoft Open Technologies, Inc.
 * All Rights Reserved
 * See License.txt in the project root for license information.
 ******************************************************************************/
package com.microsoft.services.android.impl.http;

import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.services.orc.interfaces.Request;
import com.microsoft.services.orc.interfaces.Response;
import com.microsoft.services.orc.impl.http.BaseHttpTransport;
import com.microsoft.services.orc.impl.http.NetworkRunnable;


/**
 * The type Android http transport.
 */
public class AndroidHttpTransport extends BaseHttpTransport {

    @Override
    protected NetworkRunnable createNetworkRunnable(Request request, SettableFuture<Response> future) {
        return new AndroidNetworkRunnable(request, future);
    }
}
