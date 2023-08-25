/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type;

import static org.mule.runtime.internal.dsl.DslConstants.CORE_NAMESPACE;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_PREFIX;
import static org.mule.runtime.internal.dsl.DslConstants.SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER;

import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.metadata.java.api.handler.ClassHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.scheduler.SchedulingStrategy;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.QNameTypeAnnotation;

import java.lang.reflect.Type;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * Implementation of {@link ClassHandler} for the {@link SchedulingStrategy} interface
 *
 * @since 1.4
 */
public class SchedulingStrategyClassHandler implements ClassHandler {

  @Override
  public boolean handles(Class<?> clazz) {
    return SchedulingStrategy.class.equals(clazz)
        // Need the fqcn as a string because this deprecated type is in mule-core that is not and cannot be a dependency of this
        // module.
        // Apart from that, this only exists for supporting the usage of a deprecated way of doing things.
        || "org.mule.runtime.core.api.source.scheduler.Scheduler".equals(clazz.getName());
  }

  @Override
  public TypeBuilder<?> handleClass(Class<?> clazz, List<Type> genericTypes, TypeHandlerManager typeHandlerManager,
                                    ParsingContext context, BaseTypeBuilder typeBuilder) {

    return typeBuilder.objectType().id(SchedulingStrategy.class.getName())
        .with(new QNameTypeAnnotation(new QName(CORE_NAMESPACE, SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER, CORE_PREFIX)))
        .with(new ExtensibleTypeAnnotation())
        .with(new TypeAliasAnnotation(SchedulingStrategy.class.getSimpleName()))
        .with(new ClassInformationAnnotation(SchedulingStrategy.class));
  }
}
