/*******************************************************************************
**NOTE** This code was generated by a tool and will occasionally be
overwritten. We welcome comments and issues regarding this code; they will be
addressed in the generation tool. If you wish to submit pull requests, please
do so for the templates in that tool.

This code was generated by Vipr (https://github.com/microsoft/vipr) using
the T4TemplateWriter (https://github.com/msopentech/vipr-t4templatewriter).

Copyright (c) Microsoft Open Technologies, Inc. All Rights Reserved.
Licensed under the Apache License 2.0; see LICENSE in the source repository
root for authoritative license information.﻿
******************************************************************************/
package com.microsoft.sampleservice;


import com.microsoft.services.orc.core.ODataStream;

/**
 * The type Sample Entity.
*/
public class SampleEntity extends Entity {

    public SampleEntity(){
        setODataType("#Microsoft.SampleService.SampleEntity");
    }
            
    private String DisplayName;
     
    /**
    * Gets the Display Name.
    *
    * @return the String
    */
    public String getDisplayName() {
        return this.DisplayName; 
    }

    /**
    * Sets the Display Name.
    *
    * @param value the String
    */
    public void setDisplayName(String value) { 
        this.DisplayName = value; 
        valueChanged("DisplayName", value);

    }
            
    private String entityKey;
     
    /**
    * Gets the entity Key.
    *
    * @return the String
    */
    public String getEntityKey() {
        return this.entityKey; 
    }

    /**
    * Sets the entity Key.
    *
    * @param value the String
    */
    public void setEntityKey(String value) { 
        this.entityKey = value; 
        valueChanged("entityKey", value);

    }
            
    private SampleEntity nestedSampleEntity;
     
    /**
    * Gets the nested Sample Entity.
    *
    * @return the SampleEntity
    */
    public SampleEntity getNestedSampleEntity() {
        return this.nestedSampleEntity; 
    }

    /**
    * Sets the nested Sample Entity.
    *
    * @param value the SampleEntity
    */
    public void setNestedSampleEntity(SampleEntity value) { 
        this.nestedSampleEntity = value; 
        valueChanged("nestedSampleEntity", value);

    }
            
    private ODataStream Content;
     
    /**
    * Gets the Content.
    *
    * @return the ODataStream
    */
    public ODataStream getContent() {
        return this.Content; 
    }

    /**
    * Sets the Content.
    *
    * @param value the ODataStream
    */
    public void setContent(ODataStream value) { 
        this.Content = value; 
        valueChanged("Content", value);

    }
    
        
    private java.util.List<AnotherEntity> Navigations = null;
    
    
     
    /**
    * Gets the Navigations.
    *
    * @return the java.util.List<AnotherEntity>
    */
    public java.util.List<AnotherEntity> getNavigations() {
        return this.Navigations; 
    }

    /**
    * Sets the Navigations.
    *
    * @param value the java.util.List<AnotherEntity>
    */
    public void setNavigations(java.util.List<AnotherEntity> value) { 
        this.Navigations = value; 
        valueChanged("Navigations", value);

    }
    
        
    private java.util.List<Item> Items = null;
    
    
     
    /**
    * Gets the Items.
    *
    * @return the java.util.List<Item>
    */
    public java.util.List<Item> getItems() {
        return this.Items; 
    }

    /**
    * Sets the Items.
    *
    * @param value the java.util.List<Item>
    */
    public void setItems(java.util.List<Item> value) { 
        this.Items = value; 
        valueChanged("Items", value);

    }
    
        
    private java.util.List<SampleEntity> nestedSampleEntityCollection = null;
    
    
     
    /**
    * Gets the nested Sample Entity Collection.
    *
    * @return the java.util.List<SampleEntity>
    */
    public java.util.List<SampleEntity> getNestedSampleEntityCollection() {
        return this.nestedSampleEntityCollection; 
    }

    /**
    * Sets the nested Sample Entity Collection.
    *
    * @param value the java.util.List<SampleEntity>
    */
    public void setNestedSampleEntityCollection(java.util.List<SampleEntity> value) { 
        this.nestedSampleEntityCollection = value; 
        valueChanged("nestedSampleEntityCollection", value);

    }
}

