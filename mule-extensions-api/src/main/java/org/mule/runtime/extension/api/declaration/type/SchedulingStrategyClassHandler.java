/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
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
import org.mule.runtime.api.scheduler.SchedulingStrategy;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Implementation of {@link ClassHandler} for the {@link SchedulingStrategy} interface
 *
 * @since 1.4
 */
public class SchedulingStrategyClassHandler implements ClassHandler {

  @Override
  public boolean handles(Class<?> clazz) {
    return SchedulingStrategy.class.equals(clazz)
        || "org.mule.runtime.core.api.source.scheduler.Scheduler".equals(clazz.getName());
  }

  @Override
  public TypeBuilder<?> handleClass(Class<?> clazz, List<Type> genericTypes, TypeHandlerManager typeHandlerManager,
                                    ParsingContext context, BaseTypeBuilder typeBuilder) {

    return typeBuilder.objectType().id(SchedulingStrategy.class.getName())
        .with(new ExtensibleTypeAnnotation())
        .with(new TypeAliasAnnotation(SchedulingStrategy.class.getSimpleName()))
        .with(new ClassInformationAnnotation(SchedulingStrategy.class));
  }
}
