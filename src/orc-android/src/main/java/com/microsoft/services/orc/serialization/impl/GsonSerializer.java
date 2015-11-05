package com.microsoft.services.orc.serialization.impl;


import com.microsoft.services.orc.serialization.ByteArrayTypeAdapterBase;

/**
 * The type Gson serializer.
 */
public class GsonSerializer extends GsonSerializerBase {
    @Override
    protected ByteArrayTypeAdapterBase getByteArrayTypeAdapter() {
        return new ByteArrayTypeAdapterImpl();
    }
}
