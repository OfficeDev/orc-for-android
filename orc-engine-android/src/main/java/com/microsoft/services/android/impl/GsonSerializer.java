package com.microsoft.services.android.impl;


import com.microsoft.services.orc.impl.GsonSerializerBase;

/**
 * The type Gson serializer.
 */
public class GsonSerializer extends GsonSerializerBase {

    @Override
    protected ByteArrayTypeAdapterImpl getByteArrayTypeAdapter() {
        return new ByteArrayTypeAdapterImpl();
    }
}
