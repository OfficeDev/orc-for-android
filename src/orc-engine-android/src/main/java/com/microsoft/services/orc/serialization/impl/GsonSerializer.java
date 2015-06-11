package com.microsoft.services.orc.serialization.impl;

import com.microsoft.services.orc.serialization.GsonSerializerBase;

/**
 * The type Gson serializer.
 */
public class GsonSerializer extends GsonSerializerBase {

    @Override
    protected ByteArrayTypeAdapterImpl getByteArrayTypeAdapter() {
        return new ByteArrayTypeAdapterImpl();
    }
}
