/*******************************************************************************
 * Copyright (c) Microsoft Open Technologies, Inc.
 * All Rights Reserved
 * See License.txt in the project root for license information.
 ******************************************************************************/
package com.microsoft.services.orc.core;

import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.services.orc.http.HttpVerb;
import com.microsoft.services.orc.http.OrcResponse;
import com.microsoft.services.orc.http.OrcURL;
import com.microsoft.services.orc.http.Request;

import java.util.List;

import static com.microsoft.services.orc.core.Helpers.*;

/**
 * The type OrcCollectionFetcher.
 *
 * @param <TEntity>     the type parameter
 * @param <TFetcher>    the type parameter
 * @param <TOperations> the type parameter
 */
public class OrcCollectionFetcher<TEntity, TFetcher extends OrcEntityFetcher, TOperations extends OrcOperations>
        extends OrcFetcher<TEntity>
        implements Readable<OrcList<TEntity>> {

    private int top = -1;
    private int skip = -1;
    private String selectedId = null;
    private TOperations operations;
    private String select = null;
    private String expand = null;
    private String filter = null;
    private String orderBy = null;

    /**
     * Instantiates a new OrcCollectionFetcher.
     *
     * @param urlComponent   the url component
     * @param parent         the parent
     * @param clazz          the clazz
     * @param operationClazz the operation clazz
     */
    public OrcCollectionFetcher(String urlComponent, OrcExecutable parent,
                                Class<TEntity> clazz, Class<TOperations> operationClazz) {
        super(urlComponent, parent, clazz);

        this.reset();

        try {
            this.operations = operationClazz.getConstructor(String.class,
                    OrcExecutable.class).newInstance("", this);
        } catch (Throwable ignored) {
        }
    }

    /**
     * Reset void.
     */
    public void reset() {
        this.top = -1;
        this.skip = -1;
        this.selectedId = null;
        this.select = null;
        this.expand = null;
        this.filter = null;
        this.orderBy = null;
    }

    /**
     * Top OrcCollectionFetcher.
     *
     * @param top the top
     * @return the o data collection fetcher
     */
    public OrcCollectionFetcher<TEntity, TFetcher, TOperations> top(int top) {
        this.top = top;
        return this;
    }

    /**
     * Skip OrcCollectionFetcher.
     *
     * @param skip the skip
     * @return the o data collection fetcher
     */
    public OrcCollectionFetcher<TEntity, TFetcher, TOperations> skip(int skip) {
        this.skip = skip;
        return this;
    }

    /**
     * Select OrcCollectionFetcher.
     *
     * @param select the select
     * @return the o data collection fetcher
     */
    public OrcCollectionFetcher<TEntity, TFetcher, TOperations> select(String select) {
        this.select = select;
        return this;
    }

    /**
     * Expand OrcCollectionFetcher.
     *
     * @param expand the expand
     * @return the o data collection fetcher
     */
    public OrcCollectionFetcher<TEntity, TFetcher, TOperations> expand(String expand) {
        this.expand = expand;
        return this;
    }

    /**
     * Filter OrcCollectionFetcher.
     *
     * @param filter the filter
     * @return the o data collection fetcher
     */
    public OrcCollectionFetcher<TEntity, TFetcher, TOperations> filter(String filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Order OrcCollectionFetcher.
     *
     * @param orderBy the orderBy
     * @return the o data collection fetcher
     */
    public OrcCollectionFetcher<TEntity, TFetcher, TOperations> orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    public TFetcher getById(String id) {
        this.selectedId = id;
        String packageName = operations.getClass().getPackage().getName();
        String[] classNameParts = (clazz.getCanonicalName() + "Fetcher").split("\\.");
        String className = packageName + "." + classNameParts[classNameParts.length - 1];

        try {
            Class entityQueryClass = Class.forName(className);
            OrcEntityFetcher odataEntityQuery = (OrcEntityFetcher) entityQueryClass
                    .getConstructor(String.class, OrcExecutable.class)
                    .newInstance("", this);

            return (TFetcher) odataEntityQuery;
        } catch (Throwable e) {
            // if this happens, we couldn't find the xxxQuery class at runtime.
            // this must NEVER happen
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ListenableFuture<OrcResponse> oDataExecute(Request request) {
        if (selectedId == null) {
            setPathForCollections(request.getUrl(), urlComponent, top, skip, select, expand, filter, orderBy);
        } else {
            setSelectorUrl(request.getUrl(), urlComponent, selectedId);
        }
        addCustomParametersToRequest(request, getParameters(), getHeaders());
        return parent.oDataExecute(request);
    }

    /**
     * Reads
     *
     * @return the listenable future
     */
    @Override
    public ListenableFuture<OrcList<TEntity>> read() {
        return Helpers.transformToEntityListListenableFuture(readRaw(), this.clazz, getResolver(), getParentOrcContainer());
    }

    private BaseOrcContainer getParentOrcContainer() {
        OrcExecutable current = parent;
        while (current != null &&
                current instanceof OrcFetcher &&
                !(current instanceof BaseOrcContainer)) {
            OrcFetcher fetcher = (OrcFetcher) current;
            current = fetcher.parent;
        }

        return (BaseOrcContainer) current;
    }

    /**
     * Reads raw
     *
     * @return the listenable future
     */
    @Override
    public ListenableFuture<String> readRaw() {
        return super.readRaw();
    }

    /**
     * Add listenable future.
     *
     * @param entity the entity
     * @return the listenable future
     */
    public ListenableFuture<TEntity> add(TEntity entity) {
        ListenableFuture<String> future = addRaw(getResolver().getJsonSerializer().serialize(entity));
        return transformToEntityListenableFuture(future, this.clazz, getResolver());
    }

    /**
     * Add raw.
     *
     * @param payload the payload
     * @return the listenable future
     */
    public ListenableFuture<String> addRaw(String payload) {
        byte[] payloadBytes = payload.getBytes(Constants.UTF8);

        Request request = getResolver().createRequest();
        request.setContent(payloadBytes);
        request.setVerb(HttpVerb.POST);

        ListenableFuture<OrcResponse> future = oDataExecute(request);
        return transformToStringListenableFuture(future);

    }

    /**
     * Gets operations.
     *
     * @return the operations
     */
    public TOperations getOperations() {
        return this.operations;
    }

    /**
     * Add parameter.
     *
     * @param name  the name
     * @param value the value
     * @return the OrcCollectionFetcher
     */
    public OrcCollectionFetcher<TEntity, TFetcher, TOperations> addParameter(String name, Object value) {
        addCustomParameter(name, value);
        return this;
    }

    /**
     * Add header.
     *
     * @param name  the name
     * @param value the value
     * @return the OrcCollectionFetcher
     */
    public OrcCollectionFetcher<TEntity, TFetcher, TOperations> addHeader(String name, String value) {
        addCustomHeader(name, value);
        return this;
    }


    /**
     * Sets path for collections.
     *
     * @param url          the url
     * @param urlComponent the url component
     * @param top          the top
     * @param skip         the skip
     * @param select       the select
     * @param expand       the expand
     * @param filter       the filter
     * @param orderBy      the order by
     */
    protected void setPathForCollections(OrcURL url, String urlComponent, int top, int skip, String select, String expand, String filter, String orderBy) {
        if (top > -1) {
            url.addQueryStringParameter("$top", Integer.valueOf(top).toString());
        }

        if (skip > -1) {
            url.addQueryStringParameter("$skip", Integer.valueOf(skip).toString());
        }

        if (select != null) {
            url.addQueryStringParameter("$select", select);
        }

        if (expand != null) {
            url.addQueryStringParameter("$expand", expand);
        }

        if (filter != null) {
            url.addQueryStringParameter("$filter", filter);
        }

        if (orderBy != null) {
            url.addQueryStringParameter("$orderby", orderBy);
        }

        url.prependPathComponent(urlComponent);
    }
}
