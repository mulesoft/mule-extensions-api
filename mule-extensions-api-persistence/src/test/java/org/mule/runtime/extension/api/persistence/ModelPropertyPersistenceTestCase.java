/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.mule.runtime.extension.api.persistence.JsonSerializationConstants.*;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.property.DisplayModelProperty;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.LayoutModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.internal.util.HierarchyClassMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Test;

public class ModelPropertyPersistenceTestCase extends BasePersistenceTestCase
{

    public static final String SUB_TYPES_MODEL_PROPERTY_JSON = "property/sub-types-model-property.json";
    public static final String IMPORTED_TYPES_MODEL_PROPERTY_JSON = "property/imported-types-model-property.json";
    public static final String LAYOUT_MODEL_PROPERTY_JSON = "property/layout-model-property.json";
    public static final String DISPLAY_MODEL_PROPERTY_JSON = "property/display-model-property.json";

    private static final Type ANNOTATIONS_MAP_TYPE = new TypeToken<Map<Class<? extends ModelProperty>, ModelProperty>>()
    {
    }.getType();
    private final ImportedTypesModelProperty importedTypesModelProperty = new ImportedTypesModelProperty(singletonMap(typeLoader.load(String.class), typeLoader.load(Integer.class)));
    private final LayoutModelProperty layoutModelProperty = new LayoutModelProperty(true, true, 43, "Group Name", "Tab Name");
    private final DisplayModelProperty displayModelProperty = new DisplayModelProperty("Display Name", "This is the summary");
    private final SubTypesModelProperty subTypesModelProperty = new SubTypesModelProperty(singletonMap(typeLoader.load(String.class), asList(typeLoader.load(Integer.class), typeLoader.load(Double.class))));

    @Test
    public void verifyImportedTypesModelPropertySerializationJson() throws IOException
    {
        final Gson gson = getGsonWithTypeAdapter(ImportedTypesModelProperty.class, new ImportedTypesModelPropertyTypeAdapter());
        assertSerializedJson(toJson(importedTypesModelProperty, gson), IMPORTED_TYPES_MODEL_PROPERTY_JSON);
        ImportedTypesModelProperty deserializedModelProperty = (ImportedTypesModelProperty) getDeserializedModelProperty(gson, IMPORTED_TYPES_MODEL_PROPERTY_JSON, IMPORTED_TYPES_MODEL_PROPERTY, ImportedTypesModelProperty.class);
        assertEquals(importedTypesModelProperty.getImportedTypes(), deserializedModelProperty.getImportedTypes());
    }

    @Test
    public void verifySubTypesModelPropertySerializationJson() throws IOException
    {
        final Gson gson = getGsonWithTypeAdapter(SubTypesModelProperty.class, new SubTypesModelPropertyTypeAdapter());
        assertSerializedJson(toJson(subTypesModelProperty, gson), SUB_TYPES_MODEL_PROPERTY_JSON);
        SubTypesModelProperty deserializedModelProperty = (SubTypesModelProperty) getDeserializedModelProperty(gson, SUB_TYPES_MODEL_PROPERTY_JSON, SUB_TYPES_MODEL_PROPERTY, SubTypesModelProperty.class);
        assertEquals(subTypesModelProperty.getSubTypesMapping(), deserializedModelProperty.getSubTypesMapping());
    }

    @Test
    public void verifyLayoutModelPropertySerializationJson() throws IOException
    {
        assertSerializedJson(toJson(layoutModelProperty, getGson()), LAYOUT_MODEL_PROPERTY_JSON);
        LayoutModelProperty deserializedModelProperty = (LayoutModelProperty) getDeserializedModelProperty(getGson(), LAYOUT_MODEL_PROPERTY_JSON, LAYOUT_MODEL_PROPERTY, LayoutModelProperty.class);
        assertEquals(layoutModelProperty.getGroupName(), deserializedModelProperty.getGroupName());
        assertEquals(layoutModelProperty.getOrder(), deserializedModelProperty.getOrder());
        assertEquals(layoutModelProperty.getTabName(), deserializedModelProperty.getTabName());
    }

    @Test
    public void verifyDisplayModelPropertySerializationJson() throws IOException
    {
        assertSerializedJson(toJson(displayModelProperty, getGson()), DISPLAY_MODEL_PROPERTY_JSON);
        DisplayModelProperty deserializedModelProperty = (DisplayModelProperty) getDeserializedModelProperty(getGson(), DISPLAY_MODEL_PROPERTY_JSON, DISPLAY_MODEL_PROPERTY, DisplayModelProperty.class);
        assertEquals(displayModelProperty.getDisplayName().get(), deserializedModelProperty.getDisplayName().get());
        assertEquals(displayModelProperty.getSummary().get(), deserializedModelProperty.getSummary().get());
    }

    private String toJson(ModelProperty modelProperty, Gson gson)
    {
        return gson.toJson(new HierarchyClassMap<>(singletonMap(modelProperty.getClass(), modelProperty)), ANNOTATIONS_MAP_TYPE);
    }

    private Gson getGson()
    {
        return getBaseGsonBuilder().create();
    }

    private Gson getGsonWithTypeAdapter(Type type, TypeAdapter typeAdapter)
    {
        return getBaseGsonBuilder()
                .registerTypeAdapter(type, typeAdapter)
                .create();
    }

    private GsonBuilder getBaseGsonBuilder()
    {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapterFactory(new ModelPropertyMapTypeAdapterFactory());
    }

    private ModelProperty getDeserializedModelProperty(Gson gson, String jsonFileName, String modelPropertyName, Class<? extends ModelProperty> modelProperty) throws IOException
    {
        return gson.fromJson(new JsonParser().parse(getResourceAsString(jsonFileName)).getAsJsonObject().get(modelPropertyName), modelProperty);
    }
}
