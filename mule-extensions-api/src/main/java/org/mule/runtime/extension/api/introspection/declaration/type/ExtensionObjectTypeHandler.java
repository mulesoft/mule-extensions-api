/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.java.api.handler.ObjectFieldHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.metadata.java.internal.handler.ObjectHandler;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Extensible;
import org.mule.runtime.extension.api.annotation.dsl.xml.NoGlobalDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.NoGlobalTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.TypeAliasAnnotation;

import java.lang.reflect.Type;
import java.util.List;

/**
 * An implementation of {@link ObjectHandler} which allows the type to me enriched with custom
 * type annotations of the Extensions API.
 *
 * @since 1.0
 */
public class ExtensionObjectTypeHandler extends ObjectHandler {

  public ExtensionObjectTypeHandler(ObjectFieldHandler fieldHandler) {
    super(fieldHandler);
  }

  @Override
  public TypeBuilder<?> handleClass(Class<?> clazz, List<Type> genericTypes, TypeHandlerManager typeHandlerManager,
                                    ParsingContext context, BaseTypeBuilder<?> typeBuilder) {
    final ObjectTypeBuilder<?> objectType = (ObjectTypeBuilder<?>) super.handleClass(clazz, genericTypes,
                                                                                     typeHandlerManager, context, typeBuilder);

    if (clazz.isAnnotationPresent(Extensible.class)) {
      objectType.with(new ExtensibleTypeAnnotation());
    }

    if (clazz.isAnnotationPresent(NoGlobalDeclaration.class)) {
      objectType.with(new NoGlobalTypeAnnotation());
    }

    Alias alias = clazz.getAnnotation(Alias.class);
    objectType.with(new TypeAliasAnnotation(alias != null ? alias.value() : clazz.getSimpleName()));

    return objectType;
  }
}
