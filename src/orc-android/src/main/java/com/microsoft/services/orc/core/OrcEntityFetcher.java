/**
 * ****************************************************************************
 * Copyright (c) Microsoft Open Technologies, Inc.
 * All Rights Reserved
 * See License.txt in the project root for license information.
 * ****************************************************************************
 */
package com.microsoft.services.orc.core;

import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.services.orc.http.HttpVerb;
import com.microsoft.services.orc.http.OrcResponse;
import com.microsoft.services.orc.http.OrcURL;
import com.microsoft.services.orc.http.Request;

import static com.microsoft.services.orc.core.Helpers.addCustomParametersToRequest;
import static com.microsoft.services.orc.core.Helpers.transformToEntityListenableFuture;
import static com.microsoft.services.orc.core.Helpers.transformToStringListenableFuture;
import static com.microsoft.services.orc.core.Helpers.transformToVoidListenableFuture;

/**
 * The type OrcEntityFetcher.
 *
 * @param <TEntity>      the type parameter
 * @param <TOperations>  the type parameter
 */
public abstract class OrcEntityFetcher<TEntity extends ODataBaseEntity, TOperations extends OrcOperations>
        extends OrcFetcher<TEntity>
        implements Readable<TEntity> {

    private TOperations operations;
    private String select;
    private String expand;

    /**
     * Instantiates a new OrcEntityFetcher.
     *
     * @param urlComponent the url component
     * @param parent the parent
     * @param clazz the clazz
     * @param operationClazz the operation clazz
     */
    public OrcEntityFetcher(String urlComponent, OrcExecutable parent, Class<TEntity> clazz, Class<TOperations> operationClazz) {
        super(urlComponent, parent, clazz);

        try {
            this.operations = operationClazz.getConstructor(String.class,
                    OrcExecutable.class).newInstance("", this);
        } catch (Throwable ignored) {
        }
    }




    /**
     * Updates the given entity.
     *
     * @param updatedEntity the updated entity
     * @return the listenable future
     */
    public ListenableFuture<TEntity> update(TEntity updatedEntity) {
        return update(updatedEntity, true);
    }

    /**
     * Updates the given entity.
     *
     * @param updatedEntity the updated entity
     * @param update override
     * @return the listenable future
     */
    public ListenableFuture<TEntity> update(TEntity updatedEntity, boolean update) {
        Object updatedValues = updatedEntity.getUpdatedValues();
        if (!update) {
            updatedValues = updatedEntity;
        }
        ListenableFuture<String> future = updateRaw(getResolver().getJsonSerializer()
                                                                 .serialize(updatedValues), update);
        return transformToEntityListenableFuture(future, this.clazz, getResolver());
    }

    /**
     * Updates the given entity.
     *
     * @param payload the updated entity
     * @param update the update
     * @return the listenable future
     */
    public ListenableFuture<String> updateRaw(String payload, boolean update) {
        HttpVerb verb = HttpVerb.PUT;

        if (update) {
            verb = HttpVerb.PATCH;
        }

        byte[] payloadBytes = payload.getBytes(Constants.UTF8);

        Request request = getResolver().createRequest();
        request.setContent(payloadBytes);
        request.setVerb(verb);

        ListenableFuture<OrcResponse> future = oDataExecute(request);

        return transformToStringListenableFuture(future);
    }

    /**
     * Deletes
     *
     * @return the listenable future
     */
    public ListenableFuture delete() {
        Request request = getResolver().createRequest();
        request.setVerb(HttpVerb.DELETE);

        ListenableFuture<OrcResponse> future = oDataExecute(request);
        return transformToVoidListenableFuture(future);
    }

    /**
     * Reads
     *
     * @return the listenable future
     */
    public ListenableFuture<TEntity> read() {
        return transformToEntityListenableFuture(readRaw(), this.clazz, getResolver());
    }

    /**
     * Reads raw
     *
     * @return the listenable future
     */
    public ListenableFuture<String> readRaw() {
        return super.readRaw();
    }


    /**
     * Select OrcCollectionFetcher.
     *
     * @param select the select
     * @return the o data collection fetcher
     */
    public OrcEntityFetcher<TEntity, TOperations> select(String select) {
        this.select = select;
        return this;
    }

    /**
     * Expand OrcCollectionFetcher.
     *
     * @param expand the expand
     * @return the o data collection fetcher
     */
    public OrcEntityFetcher<TEntity, TOperations> expand(String expand) {
        this.expand = expand;
        return this;
    }

    /**
     * Gets operations.
     *
     * @return the operations
     */
    public TOperations getOperations() {
        return this.operations;
    }
}
