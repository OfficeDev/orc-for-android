package com.microsoft.services.orc.serialization.impl;


import com.microsoft.services.orc.serialization.ByteArrayTypeAdapterBase;

public class GsonSerializer extends GsonSerializerBase {
    @Override
    protected ByteArrayTypeAdapterBase getByteArrayTypeAdapter() {
        return new ByteArrayTypeAdapterImpl();
    }
}
