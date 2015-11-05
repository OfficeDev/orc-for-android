/*******************************************************************************
 * Copyright (c) Microsoft Open Technologies, Inc.
 * All Rights Reserved
 * See License.txt in the project root for license information.
 ******************************************************************************/
package com.microsoft.services.orc.core;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.services.orc.http.HttpVerb;
import com.microsoft.services.orc.http.OrcResponse;
import com.microsoft.services.orc.http.OrcURL;
import com.microsoft.services.orc.http.Request;

import java.io.InputStream;

import static com.microsoft.services.orc.core.Helpers.transformToVoidListenableFuture;


/**
 * The type OrcMediaEntityFetcher.
 *
 * @param <TEntity>      the type parameter
 * @param <TOperations>  the type parameter
 */
public abstract class OrcMediaEntityFetcher<TEntity extends ODataBaseEntity, TOperations extends OrcOperations>
        extends OrcEntityFetcher<TEntity, TOperations>
        implements Readable<TEntity> {

    /**
     * Instantiates a new OrcMediaEntityFetcher.
     *
     * @param urlComponent the url component
     * @param parent the parent
     * @param clazz the clazz
     * @param operationClazz the operation clazz
     */

    public OrcMediaEntityFetcher(String urlComponent, OrcExecutable parent, Class<TEntity> clazz,
                                 Class<TOperations> operationClazz) {
        super(urlComponent, parent, clazz, operationClazz);
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public ListenableFuture<byte[]> getContent() {

        Request request = getResolver().createRequest();
        request.setVerb(HttpVerb.GET);
        OrcURL url = request.getUrl();
        url.appendPathComponent("$value");

        ListenableFuture<OrcResponse> future = oDataExecute(request);

        return Futures.transform(future, new AsyncFunction<OrcResponse, byte[]>() {
            @Override
            public ListenableFuture<byte[]> apply(OrcResponse response) throws Exception {
                SettableFuture<byte[]> result = SettableFuture.create();
                result.set(response.getPayload());
                return result;
            }
        });
    }

    /**
     * Gets streamed content.
     *
     * @return the streamed content
     */
    public ListenableFuture<InputStream> getStreamedContent() {

        Request request = getResolver().createRequest();
        request.setVerb(HttpVerb.GET);
        request.addOption(Request.MUST_STREAM_RESPONSE_CONTENT, "true");
        OrcURL url = request.getUrl();
        url.appendPathComponent("$value");

        ListenableFuture<OrcResponse> future = oDataExecute(request);

        return Futures.transform(future, new AsyncFunction<OrcResponse, InputStream>() {
            @Override
            public ListenableFuture<InputStream> apply(OrcResponse response) throws Exception {
                SettableFuture<InputStream> result = SettableFuture.create();
                result.set(new MediaEntityInputStream(response.openStreamedResponse(), response));
                return result;
            }
        });
    }


    /**
     * Put content.
     *
     * @param content the content
     * @return the listenable future
     */
    public ListenableFuture<Void> putContent(byte[] content) {

        Request request = getResolver().createRequest();
        request.setContent(content);
        request.setVerb(HttpVerb.PUT);
        OrcURL url = request.getUrl();
        url.appendPathComponent("$value");

        ListenableFuture<OrcResponse> future = oDataExecute(request);

        return transformToVoidListenableFuture(future);
    }

    /**
     * Put content.
     *
     * @param stream the stream
     * @param streamSize the stream size
     * @return the listenable future
     */
    public ListenableFuture<Void> putContent(InputStream stream, long streamSize) {
        Request request = getResolver().createRequest();
        request.setStreamedContent(stream, streamSize);
        request.setVerb(HttpVerb.PUT);
        OrcURL url = request.getUrl();
        url.appendPathComponent("$value");

        ListenableFuture<OrcResponse> future = oDataExecute(request);

        return transformToVoidListenableFuture(future);
    }

}