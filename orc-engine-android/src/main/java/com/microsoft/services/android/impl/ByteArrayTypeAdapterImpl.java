package com.microsoft.services.android.impl;

import android.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.microsoft.services.orc.impl.ByteArrayTypeAdapterBase;
import com.microsoft.services.orc.interfaces.Base64Encoder;

import java.lang.reflect.Type;

public class ByteArrayTypeAdapterImpl extends ByteArrayTypeAdapterBase {

    @Override
    public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Base64.decode(json.getAsString(), Base64.DEFAULT);
    }

    @Override
    public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Base64.encodeToString(src, Base64.DEFAULT));
    }

    @Override
    protected Base64Encoder getBase64Encoder() {
        return Base64EncoderImpl.getInstance();
    }
}
