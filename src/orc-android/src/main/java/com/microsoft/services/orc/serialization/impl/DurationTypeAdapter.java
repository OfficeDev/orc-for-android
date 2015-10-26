package com.microsoft.services.orc.serialization.impl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import org.joda.time.Period;

import java.lang.reflect.Type;
import java.text.ParseException;

/**
 * The type Duration type adapter.
 */
public class DurationTypeAdapter implements com.google.gson.JsonSerializer<Period>, JsonDeserializer<Period> {

    @Override
    public Period deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String strVal = json.getAsString();

        try {
            return DurationSerializer.deserialize(strVal);
        }
        catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Period src, Type typeOfSrc, JsonSerializationContext context) {
        String formatted = DurationSerializer.serialize(src);

        return new JsonPrimitive(formatted);
    }
}
