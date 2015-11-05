package com.microsoft.services.orc.serialization.impl;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.services.orc.core.AbstractDependencyResolver;
import com.microsoft.services.orc.core.BaseOrcContainer;
import com.microsoft.services.orc.core.ChangesTrackingList;
import com.microsoft.services.orc.core.Constants;
import com.microsoft.services.orc.core.DependencyResolver;
import com.microsoft.services.orc.core.ODataBaseEntity;
import com.microsoft.services.orc.core.OrcList;
import com.microsoft.services.orc.serialization.ByteArrayTypeAdapterBase;
import com.microsoft.services.orc.serialization.JsonSerializer;

import org.joda.time.Period;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.microsoft.services.orc.core.Helpers.getReservedNames;

/**
 * The type Gson serializer.
 */
public abstract class GsonSerializerBase implements JsonSerializer {
    private static Map<String, Class<?>> cachedClassesFromOData = new ConcurrentHashMap<String, Class<?>>();
    private DependencyResolver resolver;

    private Gson createGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Calendar.class, new CalendarTypeAdapter())
                .registerTypeAdapter(GregorianCalendar.class, new CalendarTypeAdapter())
                .registerTypeAdapter(Period.class, new DurationTypeAdapter())
                .registerTypeAdapter(byte[].class, getByteArrayTypeAdapter())
                .create();
    }

    /**
     * Gets byte array type adapter.
     *
     * @return the byte array type adapter
     */
    protected abstract ByteArrayTypeAdapterBase getByteArrayTypeAdapter();

    @Override
    public String serialize(Object objectToSerialize) {
        Gson serializer = createGson();
        JsonElement json = serializer.toJsonTree(objectToSerialize);
        sanitizePostSerialization(json);

        return json.toString();
    }

    @Override
    public <E> E deserialize(String payload, Class<E> clazz) {
        Gson serializer = createGson();
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(payload);
        sanitizeForDeserialization(json);

        Package pkg = clazz.getPackage();
        Class overridenClass = getClassFromJson(json, pkg);

        if (overridenClass != null) {
            clazz = overridenClass;
        }

        E odataEntity = serializer.fromJson(json, clazz);

        wrapLists(odataEntity);

        return odataEntity;
    }

    /**
     * Wraps all the lists in an ODataBaseEntity into a ChangesTrackingList
     * @param obj
     */
    private void wrapLists(Object obj) {
        if(obj==null) return;
        if (obj instanceof ODataBaseEntity ) {
            ODataBaseEntity entity = (ODataBaseEntity)obj;
            for (Field field : entity.getAllFields()) {
                field.setAccessible(true);

                try {
                    Object fieldValue = field.get(obj);
                    if(fieldValue!=null) {
                        if (fieldValue instanceof List) {
                            field.set(entity, new ChangesTrackingList((List) fieldValue));
                        } else {
                            wrapLists(fieldValue);
                        }
                    }

                } catch (IllegalAccessException e) {
                }
            }
        } else if (obj instanceof List) {
            for (Object internal : (List)obj) {
                wrapLists(internal);
            }
        }
    }

    /**
     * Gets class from json.
     *
     * @param json the json
     * @param pkg  the pkg
     * @return the class from json
     */
    protected Class getClassFromJson(JsonElement json, Package pkg) {
        try {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();

                if (jsonObject.has(Constants.ODATA_TYPE_PROPERTY_NAME)) {
                    String dataType = jsonObject.get(Constants.ODATA_TYPE_PROPERTY_NAME).getAsString();
                    if (cachedClassesFromOData.containsKey(dataType)) {
                        return cachedClassesFromOData.get(dataType);
                    }

                    String[] parts = dataType.split("\\.");
                    String className = parts[parts.length - 1];

                    String classFullName = pkg.getName() + "." + className;
                    Class<?> derivedClass = Class.forName(classFullName);

                    ODataBaseEntity instance = (ODataBaseEntity) derivedClass.newInstance();

                    Field field = ODataBaseEntity.class.getDeclaredField(Constants.ODATA_TYPE_PROPERTY_NAME);
                    if (field != null) {
                        field.setAccessible(true);
                        String val = (String) field.get(instance);
                        if (val.equals(dataType)) {
                            cachedClassesFromOData.put(dataType, derivedClass);
                            return derivedClass;
                        }
                    }
                }
            }
        } catch (Throwable ignore) {
            // if, for any reason, the sub-class cannot be loaded, just continue and the base class will
            // be used for serialization
        }

        return null;
    }

    @Override
    public <E> OrcList<E> deserializeList(String payload, Class<E> clazz, BaseOrcContainer baseOrcContainer) {
        Gson serializer = createGson();

        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject) parser.parse(payload);

        JsonElement jsonArray = json.get("value");
        sanitizeForDeserialization(jsonArray);

        JsonElement odataNextLink = json.get("@odata.nextLink");

        Package pkg = clazz.getPackage();
        ArrayList<E> arrayList = new ArrayList<E>();

        for (JsonElement item : jsonArray.getAsJsonArray()) {
            Class currentClass = clazz;
            Class overridenClass = getClassFromJson(item, pkg);

            if (overridenClass != null) {
                currentClass = overridenClass;
            }

            E deserializedItem = (E) serializer.fromJson(item, currentClass);
            arrayList.add(deserializedItem);
        }

        String nextLink = null;
        if (odataNextLink != null) {
            nextLink = odataNextLink.getAsString();
        }

        OrcList<E> orcList = new OrcList<E>(arrayList, clazz, nextLink, resolver, baseOrcContainer);
        return orcList;
    }

    private void sanitizePostSerialization(JsonElement json) {
        if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            for (JsonElement subElement : jsonArray) {
                sanitizePostSerialization(subElement);
            }
        } else if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();

            Set<Map.Entry<String, JsonElement>> entries = new HashSet<Map.Entry<String, JsonElement>>(jsonObject.entrySet());

            for (Map.Entry<String, JsonElement> entry : entries) {
                String propertyName = entry.getKey();
                JsonElement subElement = entry.getValue();

                if (propertyName.startsWith(Constants.PROPERTY_NAME_IGNORE_PREFIX)) {
                    jsonObject.remove(propertyName);
                    continue;
                }

                String newName = propertyName;

                if (newName.startsWith(Constants.PROPERTY_NAME_RESERVED_PREFIX)) {
                    newName = newName.substring(Constants.PROPERTY_NAME_RESERVED_PREFIX.length());
                    if (getReservedNames().contains(newName)) {
                        jsonObject.remove(newName);
                        jsonObject.add(propertyName, subElement);
                    }
                } else if (propertyName.equals(Constants.ODATA_TYPE_PROPERTY_NAME)) {
                    jsonObject.remove(Constants.ODATA_TYPE_PROPERTY_NAME);
                    jsonObject.add(Constants.ODATA_TYPE_JSON_PROPERTY, subElement);
                }

                sanitizePostSerialization(subElement);
            }
        }

    }

    private void sanitizeForDeserialization(JsonElement json) {
        if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            for (JsonElement subElement : jsonArray) {
                sanitizeForDeserialization(subElement);
            }
        } else if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();

            Set<Map.Entry<String, JsonElement>> entries = new HashSet<Map.Entry<String, JsonElement>>(jsonObject.entrySet());

            for (Map.Entry<String, JsonElement> entry : entries) {
                String propertyName = entry.getKey();
                JsonElement subElement = entry.getValue();

                String newName = Constants.PROPERTY_NAME_RESERVED_PREFIX + propertyName;
                if (getReservedNames().contains(propertyName)) {
                    jsonObject.remove(propertyName);
                    jsonObject.add(newName, subElement);
                } else {
                    String oDataTypeName = Constants.ODATA_TYPE_PROPERTY_NAME;
                    if (propertyName.equals(Constants.ODATA_TYPE_JSON_PROPERTY)) {
                        jsonObject.remove(propertyName);
                        jsonObject.add(oDataTypeName, subElement);
                    }
                }

                sanitizePostSerialization(subElement);
            }
        }
    }

    public void setDependencyResolver(DependencyResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public String jsonObjectFromJsonMap(Map<String, String> map) {
        JsonObject object = new JsonObject();
        JsonParser parser = new JsonParser();

        for (String key : map.keySet()) {
            String jsonString = map.get(key);
            JsonElement element = parser.parse(jsonString);
            object.add(key, element);
        }

        return object.toString();
    }
}
