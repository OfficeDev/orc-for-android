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


public class OrcODataStreamFetcher {

    private String urlComponent;
    private OrcExecutable parent;
    private DependencyResolver dependencyResolver;

    public OrcODataStreamFetcher(String urlComponent, OrcExecutable parent, DependencyResolver dependencyResolver) {
        this.urlComponent = urlComponent;
        this.parent = parent;
        this.dependencyResolver = dependencyResolver;
    }

    /**
     * Gets streamed content.
     *
     * @return the streamed content
     */
    public ListenableFuture<InputStream> getStream() {

        Request request = dependencyResolver.createRequest();
        request.setVerb(HttpVerb.GET);
        request.addOption(Request.MUST_STREAM_RESPONSE_CONTENT, "true");
        OrcURL url = request.getUrl();
        url.appendPathComponent(urlComponent);
        url.appendPathComponent("$value");

        ListenableFuture<OrcResponse> future = oDataExecute(request);

        return Futures.transform(future, new AsyncFunction<OrcResponse, InputStream>() {
            @Override
            public ListenableFuture<InputStream> apply(OrcResponse response) throws Exception {
                SettableFuture<InputStream> result = SettableFuture.create();
                result.set(new ODataStream(response.openStreamedResponse(), response));
                return result;
            }
        });
    }

    protected ListenableFuture<OrcResponse> oDataExecute(Request request) {

        OrcURL orcURL = request.getUrl();
        orcURL.prependPathComponent(urlComponent);
        return parent.oDataExecute(request);
    }

    protected DependencyResolver getResolver() {
        return dependencyResolver;
    }
}
