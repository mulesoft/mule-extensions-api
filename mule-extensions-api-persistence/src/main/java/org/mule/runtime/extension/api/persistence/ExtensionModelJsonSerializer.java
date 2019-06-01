/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static java.util.Collections.emptySet;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;
import org.mule.metadata.persistence.ObjectTypeReferenceHandler;
import org.mule.metadata.persistence.SerializationContext;
import org.mule.metadata.persistence.type.adapter.OptionalTypeAdapterFactory;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.extension.api.connectivity.oauth.ClientCredentialsGrantType;
import org.mule.runtime.extension.api.model.deprecated.ImmutableDeprecationModel;
import org.mule.runtime.api.meta.model.notification.NotificationModel;
import org.mule.runtime.api.meta.model.parameter.ExclusiveParametersModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.api.meta.model.stereotype.ImmutableStereotypeModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.connectivity.oauth.AuthorizationCodeGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthGrantType;
import org.mule.runtime.extension.api.model.ImmutableExtensionModel;
import org.mule.runtime.extension.api.model.ImmutableOutputModel;
import org.mule.runtime.extension.api.model.config.ImmutableConfigurationModel;
import org.mule.runtime.extension.api.model.connection.ImmutableConnectionProviderModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableExclusiveParametersModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterGroupModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterModel;
import org.mule.runtime.extension.api.model.source.ImmutableSourceCallbackModel;
import org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils;
import org.mule.runtime.extension.internal.persistence.ConstructModelTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.DefaultImplementationTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.ElementDslModelTypeAdapter;
import org.mule.runtime.extension.internal.persistence.ErrorModelToIdentifierTypeAdapter;
import org.mule.runtime.extension.internal.persistence.ExtensionModelTypeAdapter;
import org.mule.runtime.extension.internal.persistence.FunctionModelTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.ImportedTypesModelTypeAdapter;
import org.mule.runtime.extension.internal.persistence.ModelPropertyMapTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.MuleVersionTypeAdapter;
import org.mule.runtime.extension.internal.persistence.NestableElementModelTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.NotificationModelToIdentifierTypeAdapter;
import org.mule.runtime.extension.internal.persistence.OperationModelTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.RestrictedTypesObjectTypeReferenceHandler;
import org.mule.runtime.extension.internal.persistence.SourceModelTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.SubTypesModelTypeAdapter;
import org.mule.runtime.extension.internal.persistence.XmlDslModelTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Serializer that can convert a {@link ExtensionModel} into a readable and processable JSON representation and from a JSON
 * {@link String} to an {@link ExtensionModel} instance
 * <p>
 * <b>Considerations:</b>
 * <ul>
 * <li>Only {@link ModelProperty}s that are considered as <b>externalizable</b>, the ones that {@link ModelProperty#isPublic()}
 * returns {@code true}, will be serialized</li>
 * <li>Due to the nature of {@link ModelProperty}, that can be dynamically attached to any {@link EnrichableModel}, only the
 * already know set of {@link ModelProperty} will be tagged with a friendly name, example: {@link LayoutModel} is going to be
 * identified with the {@code display} name. Otherwise, the {@link ModelProperty} will be serialized tagging it with the full
 * qualifier name of the class.</li>
 * <li>When deserializing {@link ModelProperty}s, their full qualified name will be used, if the class is not found in the
 * ClassLoader the {@link ModelProperty} object will be discarded</li>
 * </ul>
 *
 * @since 1.0
 */
public class ExtensionModelJsonSerializer {

  private final boolean prettyPrint;
  private Set<ObjectType> registeredTypes = emptySet();
  private Set<ObjectType> importedTypes = emptySet();

  /**
   * Creates a new instance of the {@link ExtensionModelJsonSerializer}. This serializer is capable of serializing and
   * deserializing {@link ExtensionModel} from JSON ({@link #deserialize(String)} and to JSON ( {@link #serialize(ExtensionModel)}
   */
  public ExtensionModelJsonSerializer() {
    this(false);
  }

  /**
   * Creates a new instance of the {@link ExtensionModelJsonSerializer}.
   *
   * @param prettyPrint boolean indicating if the serialization of the {@link ExtensionModel} should be printed in a human
   *        readable or into compact and more performable format
   */
  public ExtensionModelJsonSerializer(boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
  }

  private Gson buildGson() {
    final SerializationContext serializationContext = new SerializationContext();

    Map<String, ErrorModel> errorModelRepository = new HashMap<>();
    Map<String, NotificationModel> notificationModelRepository = new HashMap<>();
    Gson gsonDelegate =
        gsonBuilder(serializationContext, prettyPrint, errorModelRepository, notificationModelRepository).create();

    return gsonBuilder(serializationContext, prettyPrint, errorModelRepository, notificationModelRepository)
        .registerTypeAdapterFactory(new TypeAdapterFactory() {

          @Override
          public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (ExtensionModel.class.isAssignableFrom(type.getRawType())) {
              return (TypeAdapter<T>) new ExtensionModelTypeAdapter(gsonDelegate, serializationContext, errorModelRepository,
                                                                    notificationModelRepository);
            }

            return null;
          }
        })
        .create();
  }

  private GsonBuilder gsonBuilder(SerializationContext serializationContext, boolean prettyPrint,
                                  Map<String, ErrorModel> errorModelRepository,
                                  Map<String, NotificationModel> notificationModelRepository) {
    Set<String> registeredTypeIds = registeredTypes.stream()
        .map(ExtensionMetadataTypeUtils::getId)
        .filter(Optional::isPresent)
        .map(Optional::get).collect(Collectors.toSet());

    importedTypes.forEach(type -> {
      getId(type).ifPresent(registeredTypeIds::add);
      serializationContext.registerObjectType(type);
    });

    final ObjectTypeReferenceHandler referenceHandler =
        new RestrictedTypesObjectTypeReferenceHandler(serializationContext, registeredTypeIds);

    final DefaultImplementationTypeAdapterFactory configurationModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(ConfigurationModel.class, ImmutableConfigurationModel.class);
    final DefaultImplementationTypeAdapterFactory connectionProviderModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(ConnectionProviderModel.class, ImmutableConnectionProviderModel.class);
    final DefaultImplementationTypeAdapterFactory sourceCallbackModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(SourceCallbackModel.class, ImmutableSourceCallbackModel.class);
    final DefaultImplementationTypeAdapterFactory parameterModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(ParameterModel.class, ImmutableParameterModel.class);
    final DefaultImplementationTypeAdapterFactory parameterGroupModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(ParameterGroupModel.class, ImmutableParameterGroupModel.class);
    final DefaultImplementationTypeAdapterFactory exclusiveParametersTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(ExclusiveParametersModel.class, ImmutableExclusiveParametersModel.class);
    final DefaultImplementationTypeAdapterFactory outputModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(OutputModel.class, ImmutableOutputModel.class);
    final DefaultImplementationTypeAdapterFactory<StereotypeModel, ImmutableStereotypeModel> stereotypeModelTypeAdapter =
        new DefaultImplementationTypeAdapterFactory<>(StereotypeModel.class, ImmutableStereotypeModel.class);
    final DefaultImplementationTypeAdapterFactory<OAuthGrantType, AuthorizationCodeGrantType> authCodeOAuthGrantTypeAdapter =
        new DefaultImplementationTypeAdapterFactory<>(OAuthGrantType.class, AuthorizationCodeGrantType.class);
    final DefaultImplementationTypeAdapterFactory<OAuthGrantType, ClientCredentialsGrantType> clientCredentialsOAuthGrantTypeAdapter =
        new DefaultImplementationTypeAdapterFactory<>(OAuthGrantType.class, ClientCredentialsGrantType.class);
    final DefaultImplementationTypeAdapterFactory<DeprecationModel, ImmutableDeprecationModel> deprecationModelTypeAdapter =
        new DefaultImplementationTypeAdapterFactory<>(DeprecationModel.class, ImmutableDeprecationModel.class);

    final GsonBuilder gsonBuilder = new GsonBuilder()
        .registerTypeAdapter(MetadataType.class, new MetadataTypeGsonTypeAdapter(referenceHandler))
        .registerTypeAdapter(MuleVersion.class, new MuleVersionTypeAdapter())
        .registerTypeAdapter(ImportedTypeModel.class, new ImportedTypesModelTypeAdapter())
        .registerTypeAdapter(SubTypesModel.class, new SubTypesModelTypeAdapter(referenceHandler))
        .registerTypeAdapter(XmlDslModel.class, new XmlDslModelTypeAdapter())
        .registerTypeAdapter(ParameterDslConfiguration.class, new ElementDslModelTypeAdapter())
        .registerTypeAdapter(ErrorModel.class, new ErrorModelToIdentifierTypeAdapter(errorModelRepository))
        .registerTypeAdapter(NotificationModel.class, new NotificationModelToIdentifierTypeAdapter(notificationModelRepository))
        .registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
        .registerTypeAdapterFactory(new ModelPropertyMapTypeAdapterFactory())
        .registerTypeAdapterFactory(new SourceModelTypeAdapterFactory())
        .registerTypeAdapterFactory(sourceCallbackModelTypeAdapterFactory)
        .registerTypeAdapterFactory(parameterModelTypeAdapterFactory)
        .registerTypeAdapterFactory(parameterGroupModelTypeAdapterFactory)
        .registerTypeAdapterFactory(exclusiveParametersTypeAdapterFactory)
        .registerTypeAdapterFactory(configurationModelTypeAdapterFactory)
        .registerTypeAdapterFactory(connectionProviderModelTypeAdapterFactory)
        .registerTypeAdapterFactory(new OperationModelTypeAdapterFactory())
        .registerTypeAdapterFactory(new ConstructModelTypeAdapterFactory())
        .registerTypeAdapterFactory(new FunctionModelTypeAdapterFactory())
        .registerTypeAdapterFactory(new NestableElementModelTypeAdapterFactory())
        .registerTypeAdapterFactory(outputModelTypeAdapterFactory)
        .registerTypeAdapterFactory(stereotypeModelTypeAdapter)
        .registerTypeAdapterFactory(authCodeOAuthGrantTypeAdapter)
        .registerTypeAdapterFactory(clientCredentialsOAuthGrantTypeAdapter)
        .registerTypeAdapterFactory(deprecationModelTypeAdapter);

    if (prettyPrint) {
      gsonBuilder.setPrettyPrinting();
    }
    return gsonBuilder;
  }

  /**
   * Serializes an {@link ExtensionModel} into JSON
   *
   * @param extensionModel {@link ExtensionModel} to be serialized
   * @return {@link String} JSON representation of the {@link ExtensionModel}
   */
  public String serialize(ExtensionModel extensionModel) {
    registeredTypes = extensionModel.getTypes();
    importedTypes =
        extensionModel.getImportedTypes().stream()
            .map(ImportedTypeModel::getImportedType)
            .collect(Collectors.toSet());
    return buildGson().toJson(extensionModel);
  }

  /**
   * @param extensionModelList List of {@link ExtensionModel} to be serialized
   * @return {@link String} JSON representation of the {@link List} of {@link ExtensionModel}
   */
  public String serializeList(List<ExtensionModel> extensionModelList) {
    return buildGson().toJson(extensionModelList);
  }

  /**
   * Deserializes a JSON representation of an {@link ExtensionModel}, to an actual instance of it.
   *
   * @param extensionModel serialized {@link ExtensionModel}
   * @return an instance of {@link ExtensionModel} based in the JSON
   */
  public ExtensionModel deserialize(String extensionModel) {
    return buildGson().fromJson(extensionModel, ImmutableExtensionModel.class);
  }

  /**
   * Deserializes a JSON representation of a {@link List} of {@link ExtensionModel}, to an actual instance of it.
   *
   * @param extensionModelList serialized {@link List} {@link ExtensionModel}
   * @return an instance of {@link ExtensionModel} based in the JSON
   */
  public List<ExtensionModel> deserializeList(String extensionModelList) {
    return buildGson().fromJson(extensionModelList, new TypeToken<List<ImmutableExtensionModel>>() {}.getType());
  }
}
