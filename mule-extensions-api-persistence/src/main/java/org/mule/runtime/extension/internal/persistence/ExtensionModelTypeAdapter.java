/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static java.util.Collections.min;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;

import static java.lang.String.format;
import static java.util.Collections.emptySet;

import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.persistence.JsonMetadataTypeLoader;
import org.mule.metadata.persistence.JsonMetadataTypeWriter;
import org.mule.metadata.persistence.SerializationContext;
import org.mule.runtime.api.artifact.ArtifactCoordinates;
import org.mule.runtime.api.meta.Category;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ExternalLibraryModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.notification.NotificationModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.model.ImmutableExtensionModel;
import org.mule.runtime.extension.api.util.HierarchyClassMap;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * A {@link TypeAdapter} to handle {@link ExtensionModel} instances
 *
 * @since 1.0
 */
public final class ExtensionModelTypeAdapter extends TypeAdapter<ExtensionModel> {

  private static final String CONFIGURATIONS = "configurations";
  private static final String OPERATIONS = "operations";
  private static final String FUNCTIONS = "functions";
  private static final String CONSTRUCTS = "constructs";
  private static final String CONNECTION_PROVIDERS = "connectionProviders";
  private static final String MESSAGE_SOURCES = "messageSources";
  private static final String MODEL_PROPERTIES = "modelProperties";
  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String VERSION = "version";
  private static final String VENDOR = "vendor";
  private static final String MIN_MULE_VERSION = "minMuleVersion";
  private static final String CATEGORY = "category";
  private static final String TYPES = "types";
  private static final String RESOURCES = "resources";
  private static final String PRIVILEGED_PACKAGES = "privilegedPackages";
  private static final String PRIVILEGED_ARTIFACTS = "privilegedArtifacts";
  private static final String XML_DSL = "xmlDsl";
  private static final String SUB_TYPES = "subTypes";
  private static final String EXTERNAL_LIBRARIES = "externalLibraries";
  private static final String DISPLAY_MODEL = "displayModel";
  private static final String IMPORTED_TYPES = "importedTypes";
  private static final String DEPRECATION_MODEL = "deprecationModel";
  static final String ERRORS = "errors";
  static final String NOTIFICATIONS = "notifications";
  private static final String ARTIFACT_COORDINATES = "artifactCoordinates";

  private final Gson gsonDelegate;
  private final JsonMetadataTypeLoader typeLoader = new JsonMetadataTypeLoader();
  private final JsonMetadataTypeWriter typeWriter = new JsonMetadataTypeWriter();
  private final SerializationContext serializationContext;
  private final ErrorModelSerializerDelegate errorModelDelegate;
  private final NotificationModelSerializerDelegate notificationModelDelegate;

  public ExtensionModelTypeAdapter(Gson gsonDelegate, SerializationContext serializationContext,
                                   Map<String, ErrorModel> errorModelMap, Map<String, NotificationModel> notificationModelMap) {
    this.gsonDelegate = gsonDelegate;
    this.serializationContext = serializationContext;
    this.errorModelDelegate = new ErrorModelSerializerDelegate(errorModelMap);
    this.notificationModelDelegate = new NotificationModelSerializerDelegate(notificationModelMap, gsonDelegate);
  }

  @Override
  public void write(JsonWriter out, ExtensionModel model) throws IOException {
    out.beginObject();

    out.name(NAME).value(model.getName());
    out.name(DESCRIPTION).value(model.getDescription());
    out.name(VERSION).value(model.getVersion());
    out.name(VENDOR).value(model.getVendor());
    if (model.getMinMuleVersion().isPresent()) {
      out.name(MIN_MULE_VERSION).value(model.getMinMuleVersion().get().toCompleteNumericVersion());
    }

    writeWithDelegate(model.getCategory(), CATEGORY, out, new TypeToken<Category>() {});
    writeWithDelegate(model.getXmlDslModel(), XML_DSL, out, new TypeToken<XmlDslModel>() {});
    writeWithDelegate(model.getResources(), RESOURCES, out, new TypeToken<Set<String>>() {});
    writeWithDelegate(model.getSubTypes(), SUB_TYPES, out, new TypeToken<Set<SubTypesModel>>() {});
    writeWithDelegate(model.getPrivilegedPackages(), PRIVILEGED_PACKAGES, out, new TypeToken<Set<String>>() {});
    writeWithDelegate(model.getPrivilegedArtifacts(), PRIVILEGED_ARTIFACTS, out, new TypeToken<Set<String>>() {});
    writeWithDelegate(model.getExternalLibraryModels(), EXTERNAL_LIBRARIES, out, new TypeToken<Set<ExternalLibraryModel>>() {});

    writeImportedTypes(out, model.getImportedTypes());

    writeWithDelegate(model.getDisplayModel().orElse(null), DISPLAY_MODEL, out, new TypeToken<DisplayModel>() {});
    writeWithDelegate(model.getConfigurationModels(), CONFIGURATIONS, out, new TypeToken<List<ConfigurationModel>>() {});
    writeWithDelegate(model.getOperationModels(), OPERATIONS, out, new TypeToken<List<OperationModel>>() {});
    writeWithDelegate(model.getFunctionModels(), FUNCTIONS, out, new TypeToken<List<FunctionModel>>() {});
    writeWithDelegate(model.getConstructModels(), CONSTRUCTS, out, new TypeToken<List<ConstructModel>>() {});
    writeWithDelegate(model.getConnectionProviders(), CONNECTION_PROVIDERS, out,
                      new TypeToken<List<ConnectionProviderModel>>() {});
    writeWithDelegate(model.getSourceModels(), MESSAGE_SOURCES, out, new TypeToken<List<SourceModel>>() {});

    if (model.getArtifactCoordinates().isPresent()) {
      writeWithDelegate(model.getArtifactCoordinates().get(), ARTIFACT_COORDINATES, out, new TypeToken<ArtifactCoordinates>() {});
    }

    notificationModelDelegate.writeNotifications(model.getNotificationModels(), out);
    errorModelDelegate.writeErrors(model.getErrorModels(), out);
    writeExtensionLevelModelProperties(out, model);
    writeTypes(TYPES, out, model.getTypes());
    out.endObject();
  }

  @Override
  public ExtensionModel read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();

    Set<ObjectType> types = parseTypes(TYPES, json);
    MuleVersion minMuleVersion = null;
    if (json.has(MIN_MULE_VERSION)) {
      minMuleVersion = new MuleVersion(json.get(MIN_MULE_VERSION).getAsString());
    }

    Map<String, NotificationModel> parsedNotifications;
    if (json.has(NOTIFICATIONS)) {
      JsonArray notifications = json.get(NOTIFICATIONS).getAsJsonArray();
      parsedNotifications = notificationModelDelegate.parseNotifications(notifications);
    } else {
      parsedNotifications = Collections.emptyMap();
    }

    JsonArray errors = json.get(ERRORS).getAsJsonArray();

    Map<String, ErrorModel> parsedErrors = errorModelDelegate.parseErrors(errors);

    Set<ImportedTypeModel> importedTypes = parseImportedTypes(json);
    Set<String> resources = parseWithDelegate(json, RESOURCES, new TypeToken<Set<String>>() {});
    Set<SubTypesModel> subTypes = parseWithDelegate(json, SUB_TYPES, new TypeToken<Set<SubTypesModel>>() {});
    Set<String> privilegedPackages = parseWithDelegate(json, PRIVILEGED_PACKAGES, new TypeToken<Set<String>>() {});
    Set<String> privilegedArtifacts = parseWithDelegate(json, PRIVILEGED_ARTIFACTS, new TypeToken<Set<String>>() {});
    Set<ExternalLibraryModel> externalLibraries =
        parseWithDelegate(json, EXTERNAL_LIBRARIES, new TypeToken<Set<ExternalLibraryModel>>() {});
    List<ConfigurationModel> configs = parseWithDelegate(json, CONFIGURATIONS, new TypeToken<List<ConfigurationModel>>() {});
    List<OperationModel> operations = parseWithDelegate(json, OPERATIONS, new TypeToken<List<OperationModel>>() {});
    List<ConnectionProviderModel> providers =
        parseWithDelegate(json, CONNECTION_PROVIDERS, new TypeToken<List<ConnectionProviderModel>>() {});
    List<SourceModel> sources = parseWithDelegate(json, MESSAGE_SOURCES, new TypeToken<List<SourceModel>>() {});
    List<FunctionModel> functions = parseWithDelegate(json, FUNCTIONS, new TypeToken<List<FunctionModel>>() {});
    List<ConstructModel> constructs = parseWithDelegate(json, CONSTRUCTS, new TypeToken<List<ConstructModel>>() {});
    ArtifactCoordinates coordinates = parseWithDelegate(json, ARTIFACT_COORDINATES, new TypeToken<ArtifactCoordinates>() {});

    return new ImmutableExtensionModel(json.get(NAME).getAsString(),
                                       json.get(DESCRIPTION).getAsString(),
                                       json.get(VERSION).getAsString(),
                                       json.get(VENDOR).getAsString(),
                                       gsonDelegate.fromJson(json.get(CATEGORY), Category.class),
                                       configs,
                                       operations,
                                       providers,
                                       sources,
                                       functions,
                                       constructs,
                                       gsonDelegate.fromJson(json.get(DISPLAY_MODEL), DisplayModel.class),
                                       gsonDelegate.fromJson(json.get(XML_DSL), XmlDslModel.class),
                                       subTypes,
                                       types,
                                       resources,
                                       importedTypes,
                                       new LinkedHashSet<>(parsedErrors.values()),
                                       externalLibraries,
                                       privilegedPackages, privilegedArtifacts, parseExtensionLevelModelProperties(json),
                                       new LinkedHashSet<>(parsedNotifications.values()),
                                       null,
                                       coordinates,
                                       minMuleVersion);
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

  private Set<ObjectType> parseTypes(String label, JsonObject json) {
    final Set<ObjectType> types = new LinkedHashSet<>();
    final JsonElement jsonElement = json.get(label);
    JsonArray typesArray = jsonElement != null ? jsonElement.getAsJsonArray() : null;

    if (typesArray == null) {
      return emptySet();
    }

    typesArray.forEach(typeElement -> typeLoader.load(typeElement).ifPresent(type -> {
      if (!(type instanceof ObjectType)) {
        throw new IllegalArgumentException(format("Was expecting an object type but %s was found instead",
                                                  type.getClass().getSimpleName()));
      }
      getId(type)
          .orElseThrow(() -> new IllegalArgumentException("Invalid json element found in 'types', only ObjectTypes "
              + "with a 'typeId' can be part of the 'types' catalog"));

      final ObjectType objectType = (ObjectType) type;
      serializationContext.registerObjectType(objectType);
      types.add(objectType);
    }));

    return types;
  }

  private Set<ImportedTypeModel> parseImportedTypes(JsonObject json) {
    return parseTypes(IMPORTED_TYPES, json)
        .stream().map(ImportedTypeModel::new)
        .collect(Collectors.toSet());
  }

  private void writeTypes(String label, JsonWriter out, Set<ObjectType> additionalTypes) throws IOException {
    out.name(label);
    out.beginArray();
    final Set<ObjectType> objectTypes = new LinkedHashSet<>();
    objectTypes.addAll(additionalTypes);
    for (ObjectType type : objectTypes) {
      typeWriter.write(type, out);
    }
    out.endArray();
  }

  private void writeImportedTypes(JsonWriter out, Set<ImportedTypeModel> importedTypeModels) throws IOException {
    writeTypes(IMPORTED_TYPES, out, importedTypeModels
        .stream()
        .map(ImportedTypeModel::getImportedType)
        .collect(Collectors.toCollection(LinkedHashSet::new)));
  }

  private void writeExtensionLevelModelProperties(JsonWriter out, ExtensionModel model) throws IOException {
    out.name(MODEL_PROPERTIES);
    HierarchyClassMap<ModelProperty> properties = new HierarchyClassMap<>();
    model.getModelProperties().forEach(p -> properties.put(p.getClass(), p));
    gsonDelegate.toJson(properties, new TypeToken<HierarchyClassMap<ModelProperty>>() {

    }.getType(), out);
  }

  private Set<ModelProperty> parseExtensionLevelModelProperties(JsonObject json) {
    Map<Class<? extends ModelProperty>, ModelProperty> properties =
        gsonDelegate.fromJson(json.get(MODEL_PROPERTIES), new TypeToken<Map<Class<? extends ModelProperty>, ModelProperty>>() {

        }.getType());
    return new LinkedHashSet<>(properties.values());
  }
}
