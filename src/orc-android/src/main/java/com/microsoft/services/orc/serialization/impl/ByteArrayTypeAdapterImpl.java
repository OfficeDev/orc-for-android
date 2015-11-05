package com.microsoft.services.orc.serialization.impl;

import android.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import com.microsoft.services.orc.http.Base64Encoder;
import com.microsoft.services.orc.http.impl.Base64EncoderImpl;
import com.microsoft.services.orc.serialization.ByteArrayTypeAdapterBase;

/**
 * The type Byte array type adapter impl.
 */
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
