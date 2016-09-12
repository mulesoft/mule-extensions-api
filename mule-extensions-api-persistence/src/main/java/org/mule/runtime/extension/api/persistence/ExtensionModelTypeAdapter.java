/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.persistence.JsonMetadataTypeLoader;
import org.mule.metadata.persistence.JsonMetadataTypeWriter;
import org.mule.metadata.persistence.SerializationContext;
import org.mule.runtime.api.MuleVersion;
import org.mule.runtime.extension.api.Category;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.ImmutableExtensionModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.internal.util.HierarchyClassMap;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

/**
 * A {@link TypeAdapter} to handle {@link ExtensionModel} instances
 *
 * @since 1.0
 */
class ExtensionModelTypeAdapter extends TypeAdapter<ExtensionModel> {

  private static final String CONFIGURATIONS = "configurations";
  private static final String OPERATIONS = "operations";
  private static final String CONNECTION_PROVIDERS = "connectionProviders";
  private static final String MESSAGE_SOURCES = "messageSources";
  private static final String MODEL_PROPERTIES = "modelProperties";
  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String VERSION = "version";
  private static final String VENDOR = "vendor";
  private static final String CATEGORY = "category";
  private static final String TYPES = "types";

  private final Gson gsonDelegate;
  private final JsonMetadataTypeLoader typeLoader = new JsonMetadataTypeLoader();
  private final JsonMetadataTypeWriter typeWriter = new JsonMetadataTypeWriter();
  private final SerializationContext serializationContext;

  ExtensionModelTypeAdapter(Gson gsonDelegate, SerializationContext serializationContext) {
    this.gsonDelegate = gsonDelegate;
    this.serializationContext = serializationContext;
  }

  @Override
  public void write(JsonWriter out, ExtensionModel model) throws IOException {
    out.beginObject();

    out.name(NAME).value(model.getName());
    out.name(DESCRIPTION).value(model.getDescription());
    out.name(VERSION).value(model.getVersion());
    out.name(VENDOR).value(model.getVendor());
    writeWithDelegate(model.getCategory(), CATEGORY, out, new TypeToken<Category>() {});
    out.name(VERSION).value(model.getVersion());

    writeWithDelegate(model.getConfigurationModels(), CONFIGURATIONS, out, new TypeToken<List<ConfigurationModel>>() {});
    writeWithDelegate(model.getOperationModels(), OPERATIONS, out, new TypeToken<List<OperationModel>>() {});
    writeWithDelegate(model.getConnectionProviders(), CONNECTION_PROVIDERS, out,
                      new TypeToken<List<ConnectionProviderModel>>() {});
    writeWithDelegate(model.getSourceModels(), MESSAGE_SOURCES, out, new TypeToken<List<SourceModel>>() {});

    writeExtensionLevelModelProperties(out, model);

    writeTypes(out, model.getTypes());
    out.endObject();
  }

  @Override
  public ExtensionModel read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();

    Set<ObjectType> types = parseTypes(json);

    List<ConfigurationModel> configs = parseWithDelegate(json, CONFIGURATIONS, new TypeToken<List<ConfigurationModel>>() {});
    List<OperationModel> operations = parseWithDelegate(json, OPERATIONS, new TypeToken<List<OperationModel>>() {});
    List<ConnectionProviderModel> providers =
        parseWithDelegate(json, CONNECTION_PROVIDERS, new TypeToken<List<ConnectionProviderModel>>() {});
    List<SourceModel> sources = parseWithDelegate(json, MESSAGE_SOURCES, new TypeToken<List<SourceModel>>() {});

    return new ImmutableExtensionModel(json.get(NAME).getAsString(),
                                       json.get(DESCRIPTION).getAsString(),
                                       json.get(VERSION).getAsString(),
                                       json.get(VENDOR).getAsString(),
                                       gsonDelegate.fromJson(json.get(CATEGORY), Category.class),
                                       gsonDelegate.fromJson(json.get(VERSION), MuleVersion.class),
                                       configs,
                                       operations,
                                       providers,
                                       sources,
                                       types,
                                       parseExtensionLevelModelProperties(json));
  }

  private <T> T parseWithDelegate(JsonObject json, String elementName, TypeToken<T> typeToken) {
    JsonElement element = json.get(elementName);
    if (element != null) {
      return gsonDelegate.fromJson(element, typeToken.getType());
    }

    return null;
  }

  private <T> void writeWithDelegate(T value, String elementName, JsonWriter out, TypeToken<T> typeToken) throws IOException {
    out.name(elementName);
    gsonDelegate.toJson(value, typeToken.getType(), out);
  }

  private Set<ObjectType> parseTypes(JsonObject json) {
    final Set<ObjectType> types = new HashSet<>();
    JsonArray typesArray = json.get(TYPES).getAsJsonArray();

    if (typesArray == null) {
      return emptySet();
    }

    typesArray.forEach(typeElement -> typeLoader.load(typeElement).ifPresent(type -> {
      if (!(type instanceof ObjectType)) {
        throw new IllegalArgumentException(format("Was expecting an object type but %s was found instead",
                                                  type.getClass().getSimpleName()));
      }

      final ObjectType objectType = (ObjectType) type;
      serializationContext.registerObjectType(objectType);
      types.add(objectType);
    }));

    return types;
  }

  private void writeTypes(JsonWriter out, Set<ObjectType> additionalTypes) throws IOException {
    out.name(TYPES);
    out.beginArray();
    final Set<ObjectType> objectTypes = new LinkedHashSet<>();
    objectTypes.addAll(serializationContext.getRegisteredObjectTypes());
    objectTypes.addAll(additionalTypes);
    for (ObjectType type : objectTypes) {
      typeWriter.write(type, out);
    }
    out.endArray();
  }

  private void writeExtensionLevelModelProperties(JsonWriter out, ExtensionModel model) throws IOException {
    out.name(MODEL_PROPERTIES);
    HierarchyClassMap<ModelProperty> properties = new HierarchyClassMap<>();
    model.getModelProperties().forEach(p -> properties.put(p.getClass(), p));
    gsonDelegate.toJson(properties, new TypeToken<HierarchyClassMap<ModelProperty>>() {}.getType(), out);
  }

  private Set<ModelProperty> parseExtensionLevelModelProperties(JsonObject json) {
    HierarchyClassMap<ModelProperty> properties =
        gsonDelegate.fromJson(json.get(MODEL_PROPERTIES), new TypeToken<HierarchyClassMap<ModelProperty>>() {}.getType());
    return properties.values().stream().collect(toSet());
  }
}
