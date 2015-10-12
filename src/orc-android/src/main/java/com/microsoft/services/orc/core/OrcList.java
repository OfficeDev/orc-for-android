package com.microsoft.services.orc.core;

import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.services.orc.http.HttpVerb;
import com.microsoft.services.orc.http.OrcResponse;
import com.microsoft.services.orc.http.OrcURL;
import com.microsoft.services.orc.http.Request;

import java.util.ArrayList;
import java.util.Collection;

public class OrcList<E> extends ArrayList<E> {

    private Class<E> clazz;
    private String nextLink;
    private DependencyResolver resolver;
    private BaseOrcContainer baseOrcContainer;

    public OrcList(Collection<? extends E> collection,
                   Class<E> clazz, String nextLink,
                   DependencyResolver resolver,
                   BaseOrcContainer baseOrcContainer) {
        super(collection);
        this.clazz = clazz;
        this.nextLink = nextLink;
        this.resolver = resolver;
        this.baseOrcContainer = baseOrcContainer;
    }

    public boolean hasNext() {
        return nextLink != null;
    }

    public ListenableFuture<OrcList<E>> followNextLink() {
        Request request = resolver.createRequest();

        request.setVerb(HttpVerb.GET);
        OrcURL url = resolver.getOrcURL();
        url.setBaseUrl(nextLink);
        request.setUrl(url);

        ListenableFuture<OrcResponse> future = baseOrcContainer.oDataExecute(request);
        ListenableFuture<String> stringFuture = Helpers.transformToStringListenableFuture(future);
        ListenableFuture<OrcList<E>> list = Helpers.transformToEntityListListenableFuture(stringFuture, this.clazz, resolver, baseOrcContainer);

        return list;
    }


}
