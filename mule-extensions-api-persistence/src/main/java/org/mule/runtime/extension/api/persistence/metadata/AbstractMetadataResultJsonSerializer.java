/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;
import org.mule.metadata.persistence.reduced.ReducedMetadataTypeGsonTypeAdapter;
import org.mule.metadata.persistence.type.adapter.OptionalTypeAdapterFactory;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.error.ImmutableErrorModel;
import org.mule.runtime.extension.api.model.deprecated.ImmutableDeprecationModel;
import org.mule.runtime.extension.api.model.notification.ImmutableNotificationModel;
import org.mule.runtime.api.meta.model.notification.NotificationModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ExclusiveParametersModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.stereotype.ImmutableStereotypeModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.api.model.ImmutableOutputModel;
import org.mule.runtime.extension.api.model.operation.ImmutableOperationModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableExclusiveParametersModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterGroupModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterModel;
import org.mule.runtime.extension.api.model.source.ImmutableSourceCallbackModel;
import org.mule.runtime.extension.api.model.source.ImmutableSourceModel;
import org.mule.runtime.extension.internal.persistence.DefaultImplementationTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.ModelPropertyMapTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.metadata.ComponentResultTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.metadata.FailureCodeTypeAdapterFactory;
import org.mule.runtime.extension.internal.persistence.metadata.MetadataKeyTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

/**
 * Abstract implementation of a serializer that can convert a {@link MetadataResult} of some payload type into a readable and
 * processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
abstract class AbstractMetadataResultJsonSerializer<T> {

  protected final Gson gson;

  AbstractMetadataResultJsonSerializer(boolean prettyPrint) {
    this(prettyPrint, false);
  }

  AbstractMetadataResultJsonSerializer(boolean prettyPrint, boolean reduced) {

    final DefaultImplementationTypeAdapterFactory operationModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(OperationModel.class, ImmutableOperationModel.class);
    final DefaultImplementationTypeAdapterFactory sourceModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(SourceModel.class, ImmutableSourceModel.class);
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
    final DefaultImplementationTypeAdapterFactory<ErrorModel, ImmutableErrorModel> errorModelTypeAdapter =
        new DefaultImplementationTypeAdapterFactory<>(ErrorModel.class, ImmutableErrorModel.class);
    final DefaultImplementationTypeAdapterFactory<StereotypeModel, ImmutableStereotypeModel> stereotypeModelTypeAdapter =
        new DefaultImplementationTypeAdapterFactory<>(StereotypeModel.class, ImmutableStereotypeModel.class);
    final DefaultImplementationTypeAdapterFactory stereoTypeModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(StereotypeModel.class, ImmutableStereotypeModel.class);
    final DefaultImplementationTypeAdapterFactory notificationModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(NotificationModel.class, ImmutableNotificationModel.class);
    final DefaultImplementationTypeAdapterFactory<DeprecationModel, ImmutableDeprecationModel> deprecationModelTypeAdapterFactory =
        new DefaultImplementationTypeAdapterFactory<>(DeprecationModel.class, ImmutableDeprecationModel.class);

    final GsonBuilder gsonBuilder = new GsonBuilder()
        .registerTypeAdapterFactory(new FailureCodeTypeAdapterFactory())
        .registerTypeAdapter(MetadataType.class, getMetadataTypeAdapterFactory(reduced))
        .registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
        .registerTypeAdapterFactory(new ModelPropertyMapTypeAdapterFactory())
        .registerTypeAdapterFactory(new ComponentResultTypeAdapterFactory())
        .registerTypeAdapter(MetadataKey.class, new MetadataKeyTypeAdapter())
        .registerTypeAdapterFactory(sourceModelTypeAdapterFactory)
        .registerTypeAdapterFactory(sourceCallbackModelTypeAdapterFactory)
        .registerTypeAdapterFactory(parameterModelTypeAdapterFactory)
        .registerTypeAdapterFactory(parameterGroupModelTypeAdapterFactory)
        .registerTypeAdapterFactory(exclusiveParametersTypeAdapterFactory)
        .registerTypeAdapterFactory(operationModelTypeAdapterFactory)
        .registerTypeAdapterFactory(outputModelTypeAdapterFactory)
        .registerTypeAdapterFactory(stereotypeModelTypeAdapter)
        .registerTypeAdapterFactory(errorModelTypeAdapter)
        .registerTypeAdapterFactory(stereoTypeModelTypeAdapterFactory)
        .registerTypeAdapterFactory(notificationModelTypeAdapterFactory)
        .registerTypeAdapterFactory(deprecationModelTypeAdapterFactory);

    if (prettyPrint) {
      gsonBuilder.setPrettyPrinting();
    }

    this.gson = gsonBuilder.create();
  }

  private TypeAdapter getMetadataTypeAdapterFactory(boolean reduced) {
    return reduced ? new ReducedMetadataTypeGsonTypeAdapter() : new MetadataTypeGsonTypeAdapter();
  }

  /**
   * @param result the {@link MetadataResult} to be serialized
   * @return {@link String} JSON representation of the {@link MetadataResult}
   */
  public abstract String serialize(MetadataResult<T> result);

  /**
   * Deserializes a JSON representation of an {@link MetadataResult}, to an actual instance of it.
   *
   * @param result the serialized {@link MetadataResult} in a {@link String} JSON representation
   * @return an instance of {@link MetadataResult} based on the serialized JSON
   */
  public abstract MetadataResult<T> deserialize(String result);
}
