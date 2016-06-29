/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static java.util.Collections.singletonMap;

import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

public class ModelPropertyPersistenceTestCase extends BasePersistenceTestCase
{

    private final ImportedTypesModelProperty importedTypesModelProperty = new ImportedTypesModelProperty(singletonMap(javaTypeLoader.load(String.class), javaTypeLoader.load(Integer.class)));

    @Test
    public void verifyImportedTypesModelPropertySerializationJson() throws IOException
    {
        final Gson gson = getGsonWithTypeAdapter(ImportedTypesModelProperty.class, new ImportedTypesModelPropertyTypeAdapter());
        assertSerializedJson(gson.toJson(importedTypesModelProperty, ImportedTypesModelProperty.class), "property/imported-types-model-property.json");
    }

    private Gson getGsonWithTypeAdapter(Type type, TypeAdapter typeAdapter)
    {
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(type, typeAdapter).create();
    }
}
