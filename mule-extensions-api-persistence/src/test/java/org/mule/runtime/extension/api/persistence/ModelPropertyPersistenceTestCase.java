/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static java.util.Collections.singletonMap;

import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.property.LayoutModelProperty;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.DisplayModelProperty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Test;

public class ModelPropertyPersistenceTestCase extends BasePersistenceTestCase
{

    private static final Type ANNOTATIONS_MAP_TYPE = new TypeToken<Map<Class<? extends ModelProperty>, ModelProperty>>()
    {
    }.getType();
    private final ImportedTypesModelProperty importedTypesModelProperty = new ImportedTypesModelProperty(singletonMap(typeLoader.load(String.class), typeLoader.load(Integer.class)));
    private final LayoutModelProperty layoutModelProperty = new LayoutModelProperty(true, true, 43, "Group Name", "Tab Name");
    private final DisplayModelProperty displayModelProperty = new DisplayModelProperty("Display Name", "This is the summary");

    @Test
    public void verifyImportedTypesModelPropertySerializationJson() throws IOException
    {
        final Gson gson = getGsonWithTypeAdapter(ImportedTypesModelProperty.class, new ImportedTypesModelPropertyTypeAdapter());
        assertSerializedJson(toJson(importedTypesModelProperty, gson), "property/imported-types-model-property.json");
    }

    @Test
    public void verifyLayoutModelPropertySerializationJson() throws IOException
    {
        assertSerializedJson(toJson(layoutModelProperty, getGson()), "property/layout-model-property.json");
    }

    @Test
    public void verifyDisplayModelPropertySerializationJson() throws IOException
    {
        assertSerializedJson(toJson(displayModelProperty, getGson()), "property/display-model-property.json");
    }

    private String toJson(ModelProperty modelProperty, Gson gson)
    {
        return gson.toJson(singletonMap(modelProperty.getClass(), modelProperty), ANNOTATIONS_MAP_TYPE);
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
}
