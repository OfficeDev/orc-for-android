/*******************************************************************************
 * Copyright (c) Microsoft Open Technologies, Inc.
 * All Rights Reserved
 * See License.txt in the project root for license information.
 ******************************************************************************/
package com.microsoft.services.orc.core;

import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.services.orc.http.OrcResponse;
import com.microsoft.services.orc.http.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * The type OrcExecutable.
 */
public abstract class OrcExecutable {

    /**
     * OData execute.
     *
     * @param request the request
     * @return the listenable future
     */
    protected abstract ListenableFuture<OrcResponse> oDataExecute(Request request);

    /**
     * Gets resolver.
     *
     * @return the resolver
     */
    protected abstract DependencyResolver getResolver();

    /**
     * The Custom parameters.
     */
	Map<String, Object> customParameters = new HashMap<String, Object>();

    /**
     * The Custom headers.
     */
    Map<String, String> customHeaders = new HashMap<String, String>();

    /**
     * Add custom parameter.
     *
     * @param name the name
     * @param value the value
     */
    public void addCustomParameter(String name, Object value) {
	   this.customParameters.put(name, value);
	}

    /**
     * Gets custom parameters.
     *
     * @return the custom parameters
     */
	public Map<String, Object> getParameters() {
		return new HashMap<String, Object>(this.customParameters);
	}

    /**
     * Add custom headers.
     *
     * @param name the name
     * @param value the value
     */
    public void addCustomHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    /**
     * Gets custom headers.
     *
     * @return the custom headers
     */
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>(this.customHeaders);
    }

    /**
     * Casts the object to another
     *
     * @param <T>  the type parameter
     * @param inference the inference
     * @return the Cast
     */
    public <T extends OrcExecutable> T as(Class<T> inference) {
        return (T)this;
    }
}
