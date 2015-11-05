package com.microsoft.services.orc.core;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.services.orc.http.HttpVerb;
import com.microsoft.services.orc.http.OrcResponse;
import com.microsoft.services.orc.http.OrcURL;
import com.microsoft.services.orc.http.Request;

import static com.microsoft.services.orc.core.Helpers.addCustomParametersToRequest;


/**
 * The type O data fetcher.
 *
 * @param <TEntity> the type parameter
 */
public abstract class OrcFetcher<TEntity> extends OrcExecutable {

    /**
     * The Clazz.
     */
    protected Class<TEntity> clazz;
    /**
     * The Url component.
     */
    protected String urlComponent;
    /**
     * The Parent.
     */
    protected OrcExecutable parent;

    private String select;
    private String expand;


    /**
     * Instantiates a new O data fetcher.
     *
     * @param urlComponent the url component
     * @param parent       the parent
     * @param clazz        the clazz
     */
    public OrcFetcher(String urlComponent, OrcExecutable parent, Class<TEntity> clazz) {
        this.clazz = clazz;
        this.urlComponent = urlComponent;
        this.parent = parent;
    }

    @Override
    protected DependencyResolver getResolver() {
        return parent.getResolver();
    }

    @Override
    protected ListenableFuture<OrcResponse> oDataExecute(Request request) {

        OrcURL orcURL = request.getUrl();

        if (select != null) {
            orcURL.addQueryStringParameter("$select", select);
        }

        if (expand != null) {
            orcURL.addQueryStringParameter("$expand", expand);
        }

        orcURL.prependPathComponent(urlComponent);

        addCustomParametersToRequest(request, getParameters(), getHeaders());
        return parent.oDataExecute(request);
    }

    /**
     * Read raw.
     *
     * @return the listenable future
     */
    protected ListenableFuture<String> readRaw() {
        Request request = getResolver().createRequest();
        request.setVerb(HttpVerb.GET);
        ListenableFuture<OrcResponse> future = oDataExecute(request);
        return Helpers.transformToStringListenableFuture(future);
    }


    /**
     * Add byte array result callback.
     *
     * @param result the result
     * @param future the future
     */
    protected void addByteArrayResultCallback(final SettableFuture<byte[]> result,
                                              ListenableFuture<byte[]> future) {

        // TODO: Review usage

        Futures.addCallback(future, new FutureCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] payload) {
                try {
                    result.set(payload);
                } catch (Throwable e) {
                    result.setException(e);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                result.setException(throwable);
            }
        });
    }

    /**
     * Sets selector url.
     *
     * @param url          the url
     * @param urlComponent the url component
     * @param selector   the selected id
     */
    protected void setSelectorUrl(OrcURL url, String urlComponent, String selector) {
        url.prependPathComponent(urlComponent + selector);
    }
}
