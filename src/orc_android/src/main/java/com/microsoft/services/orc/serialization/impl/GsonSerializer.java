package com.microsoft.services.orc.serialization.impl;

/**
 * The type Gson serializer.
 */
public class GsonSerializer extends GsonSerializerBase {

    @Override
    protected ByteArrayTypeAdapterImpl getByteArrayTypeAdapter() {
        return new ByteArrayTypeAdapterImpl();
    }
}
