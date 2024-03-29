/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.metadata.java.api.handler.ClassHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.streaming.CursorProvider;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Implementation of {@link ClassHandler} for the {@link CursorProvider cursor providers}
 *
 * @since 1.2.0
 */
public class CursorProviderTypeHandler implements ClassHandler {

  @Override
  public boolean handles(Class<?> clazz) {
    return CursorProvider.class.isAssignableFrom(clazz);
  }

  @Override
  public TypeBuilder<?> handleClass(Class<?> clazz, List<Type> genericTypes, TypeHandlerManager typeHandlerManager,
                                    ParsingContext context, BaseTypeBuilder typeBuilder) {
    return typeBuilder.binaryType().id(clazz.getName())
        .with(new TypeAliasAnnotation(clazz.getSimpleName()))
        .with(new ClassInformationAnnotation(clazz));
  }
}
