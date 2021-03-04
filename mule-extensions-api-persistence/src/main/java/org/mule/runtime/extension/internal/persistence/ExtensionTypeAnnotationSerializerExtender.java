/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.persistence.api.TypeAnnotationSerializerExtender;
import org.mule.runtime.api.meta.model.stereotype.ImmutableStereotypeModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.declaration.type.ExtensionObjectTypeHandler;
import org.mule.runtime.extension.api.declaration.type.annotation.DefaultImplementingTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.DisplayTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExclusiveOptionalsTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExpressionSupportAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.QNameTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.StereotypeTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeDslAnnotation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.Map;

/**
 * Contributes to the {@link TypeAnnotationSerializerExtender} service to provide a friendly name for the custom annotations used
 * by the {@link ExtensionObjectTypeHandler} and being able to serialize a {@link StereotypeModel}
 *
 * @since 1.0
 */
public class ExtensionTypeAnnotationSerializerExtender implements TypeAnnotationSerializerExtender {

  @Override
  public Map<String, Class<? extends TypeAnnotation>> getNameClassMapping() {
    return ImmutableMap.<String, Class<? extends TypeAnnotation>>builder()
        .put(ParameterDslAnnotation.NAME, ParameterDslAnnotation.class)
        .put(TypeDslAnnotation.NAME, TypeDslAnnotation.class)
        .put(ExtensibleTypeAnnotation.NAME, ExtensibleTypeAnnotation.class)
        .put(ExpressionSupportAnnotation.NAME, ExpressionSupportAnnotation.class)
        .put(FlattenedTypeAnnotation.NAME, FlattenedTypeAnnotation.class)
        .put(ExclusiveOptionalsTypeAnnotation.NAME, ExclusiveOptionalsTypeAnnotation.class)
        .put(LayoutTypeAnnotation.NAME, LayoutTypeAnnotation.class)
        .put(DefaultImplementingTypeAnnotation.NAME, DefaultImplementingTypeAnnotation.class)
        .put(StereotypeTypeAnnotation.NAME, StereotypeTypeAnnotation.class)
        .put(QNameTypeAnnotation.NAME, QNameTypeAnnotation.class)
        .put(DisplayTypeAnnotation.NAME, DisplayTypeAnnotation.class)
        .build();
  }

  @Override
  public Collection<Object> getAdditionalFeatures() {
    return ImmutableList.builder()
        .add(new DefaultImplementationTypeAdapterFactory(StereotypeModel.class, ImmutableStereotypeModel.class))
        .add(new TypeAdapterFactory() {

          @Override
          public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (DefaultImplementingTypeAnnotation.class.isAssignableFrom(type.getRawType())) {
              return (TypeAdapter<T>) new DefaultImplementingTypeAnnotationTypeAdapter();
            }

            return null;
          }
        })
        .build();
  }
}
