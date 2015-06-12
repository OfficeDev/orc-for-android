/*******************************************************************************
 Copyright (c) Microsoft Open Technologies, Inc. All Rights Reserved.
 Licensed under the MIT or Apache License; see LICENSE in the source repository
 root for authoritative license information.﻿

 **NOTE** This code was generated by a tool and will occasionally be
 overwritten. We welcome comments and issues regarding this code; they will be
 addressed in the generation tool. If you wish to submit pull requests, please
 do so for the templates in that tool.

 This code was generated by Vipr (https://github.com/microsoft/vipr) using
 the T4TemplateWriter (https://github.com/msopentech/vipr-t4templatewriter).
 ******************************************************************************/
package com.microsoft.sampleservice.orc;

import com.microsoft.sampleservice.*;
import com.google.common.util.concurrent.*;
import com.microsoft.services.orc.core.OrcExecutable;
import com.microsoft.services.orc.http.HttpVerb;
import com.microsoft.services.orc.http.OrcResponse;
import com.microsoft.services.orc.http.Request;
import static com.microsoft.services.orc.core.Helpers.*;

/**
 * The type SampleEntityCollectionOperations
 */
public class SampleEntityCollectionOperations extends EntityCollectionOperations{

    /**
     * Instantiates a new SampleEntityCollectionOperations.
     *
     * @param urlComponent the url component
     * @param parent the parent
     */
    public SampleEntityCollectionOperations(String urlComponent, OrcExecutable parent) {
        super(urlComponent, parent);
    }

     /**
     * Add parameter.
     *
     * @param name the name
     * @param value the value
     * @return the collection operations
     */
    public SampleEntityCollectionOperations addParameter(String name, Object value) {
        addCustomParameter(name, value);
        return this;
    }

     /**
     * Add header.
     *
     * @param name the name
     * @param value the value
     * @return the collection operations
     */
    public SampleEntityCollectionOperations addHeader(String name, String value) {
        addCustomHeader(name, value);
        return this;
    }
 
     
     /**
     * SomeFunction listenable future.
     * @param path the path 
     * @return the listenable future
     */         
    public ListenableFuture<SampleComplexType> someFunction(String path) { 

        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        map.put("path", path);
		
        Request request = getResolver().createRequest();
        request.setVerb(HttpVerb.POST);
        request.setContent(serializeToJsonByteArray(map, getResolver()));

        String parameters = getFunctionParameters(map);
        request.getUrl().appendPathComponent("SomeFunction(" + parameters + ")");
        ListenableFuture<OrcResponse> future = oDataExecute(request);
        
        return transformToEntityListenableFuture(transformToStringListenableFuture(future), SampleComplexType.class, getResolver());
        
        
   }
    
                
}
