/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mule.metadata.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.extension.api.persistence.JsonSerializationConstants.DISPLAY_MODEL_PROPERTY;
import static org.mule.runtime.extension.api.persistence.JsonSerializationConstants.IMPORTED_TYPES_MODEL_PROPERTY;
import static org.mule.runtime.extension.api.persistence.JsonSerializationConstants.LAYOUT_MODEL_PROPERTY;
import static org.mule.runtime.extension.api.persistence.JsonSerializationConstants.SUB_TYPES_MODEL_PROPERTY;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.property.DisplayModelProperty;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.LayoutModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.api.persistence.model.ComplexFieldsType;
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

public class ModelPropertyPersistenceTestCase extends BasePersistenceTestCase {

  public static final String SUB_TYPES_MODEL_PROPERTY_JSON = "property/sub-types-model-property.json";
  public static final String IMPORTED_TYPES_MODEL_PROPERTY_JSON = "property/imported-types-model-property.json";
  public static final String LAYOUT_MODEL_PROPERTY_JSON = "property/layout-model-property.json";
  public static final String DISPLAY_MODEL_PROPERTY_JSON = "property/display-model-property.json";
  public static final String OTHER_EXTENSION_NAME = "OtherExtension";

  private static final Type ANNOTATIONS_MAP_TYPE =
      new TypeToken<Map<Class<? extends ModelProperty>, ModelProperty>>() {}.getType();
  private final ImportedTypesModelProperty importedTypes =
      new ImportedTypesModelProperty(singletonMap(typeLoader.load(ComplexFieldsType.class), OTHER_EXTENSION_NAME));
  private final LayoutModelProperty layout = new LayoutModelProperty(true, true, 43, "Group Name", "Tab Name");
  private final DisplayModelProperty display = new DisplayModelProperty("Display Name", "This is the summary");
  private final SubTypesModelProperty subTypes = new SubTypesModelProperty(singletonMap(typeLoader.load(String.class),
                                                                                        asList(typeLoader.load(Integer.class),
                                                                                               typeLoader.load(Double.class))));

  @Test
  public void verifyImportedTypesModelPropertySerializationJson() throws IOException {
    final Gson gson = getGsonWithTypeAdapter(ImportedTypesModelProperty.class, new ImportedTypesModelPropertyTypeAdapter());
    assertSerializedJson(toJson(importedTypes, gson), IMPORTED_TYPES_MODEL_PROPERTY_JSON);

    final ImportedTypesModelProperty deserializedModelProperty =
        (ImportedTypesModelProperty) getDeserializedModelProperty(gson, IMPORTED_TYPES_MODEL_PROPERTY_JSON,
                                                                  IMPORTED_TYPES_MODEL_PROPERTY,
                                                                  ImportedTypesModelProperty.class);

    assertThat(importedTypes.getImportedTypes().size(), is(deserializedModelProperty.getImportedTypes().size()));

    //FIXME MetadataType equals and hashcode are different for equals serialized type
    Map<String, String> deserialized = deserializedModelProperty.getImportedTypes().entrySet().stream()
        .collect(toMap(e -> getTypeId(e.getKey()).get(), Map.Entry::getValue));

    importedTypes.getImportedTypes().entrySet().stream()
        .collect(toMap(e -> getTypeId(e.getKey()).get(), Map.Entry::getValue)).entrySet()
        .forEach(e -> assertThat(e.getValue(), is(deserialized.get(e.getKey()))));
  }

  @Test
  public void verifySubTypesModelPropertySerializationJson() throws IOException {
    final Gson gson = getGsonWithTypeAdapter(SubTypesModelProperty.class, new SubTypesModelPropertyTypeAdapter());
    assertSerializedJson(toJson(subTypes, gson), SUB_TYPES_MODEL_PROPERTY_JSON);
    SubTypesModelProperty deserializedModelProperty =
        (SubTypesModelProperty) getDeserializedModelProperty(gson, SUB_TYPES_MODEL_PROPERTY_JSON, SUB_TYPES_MODEL_PROPERTY,
                                                             SubTypesModelProperty.class);
    assertEquals(subTypes.getSubTypesMapping(), deserializedModelProperty.getSubTypesMapping());
  }

  @Test
  public void verifyLayoutModelPropertySerializationJson() throws IOException {
    assertSerializedJson(toJson(layout, getGson()), LAYOUT_MODEL_PROPERTY_JSON);
    LayoutModelProperty deserializedModelProperty =
        (LayoutModelProperty) getDeserializedModelProperty(getGson(), LAYOUT_MODEL_PROPERTY_JSON, LAYOUT_MODEL_PROPERTY,
                                                           LayoutModelProperty.class);
    assertEquals(layout.getGroupName(), deserializedModelProperty.getGroupName());
    assertEquals(layout.getOrder(), deserializedModelProperty.getOrder());
    assertEquals(layout.getTabName(), deserializedModelProperty.getTabName());
  }

  @Test
  public void verifyDisplayModelPropertySerializationJson() throws IOException {
    assertSerializedJson(toJson(display, getGson()), DISPLAY_MODEL_PROPERTY_JSON);
    DisplayModelProperty deserializedModelProperty =
        (DisplayModelProperty) getDeserializedModelProperty(getGson(), DISPLAY_MODEL_PROPERTY_JSON, DISPLAY_MODEL_PROPERTY,
                                                            DisplayModelProperty.class);
    assertEquals(display.getDisplayName().get(), deserializedModelProperty.getDisplayName().get());
    assertEquals(display.getSummary().get(), deserializedModelProperty.getSummary().get());
  }

  private String toJson(ModelProperty modelProperty, Gson gson) {
    return gson.toJson(new HierarchyClassMap<>(singletonMap(modelProperty.getClass(), modelProperty)), ANNOTATIONS_MAP_TYPE);
  }

  private Gson getGson() {
    return getBaseGsonBuilder().create();
  }

  private Gson getGsonWithTypeAdapter(Type type, TypeAdapter typeAdapter) {
    return getBaseGsonBuilder()
        .registerTypeAdapter(type, typeAdapter)
        .create();
  }

  private GsonBuilder getBaseGsonBuilder() {
    return new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(new ModelPropertyMapTypeAdapterFactory());
  }

  private ModelProperty getDeserializedModelProperty(Gson gson, String jsonFileName, String modelPropertyName,
                                                     Class<? extends ModelProperty> modelProperty)
      throws IOException {
    return gson.fromJson(new JsonParser().parse(getResourceAsString(jsonFileName)).getAsJsonObject().get(modelPropertyName),
                         modelProperty);
  }
}
