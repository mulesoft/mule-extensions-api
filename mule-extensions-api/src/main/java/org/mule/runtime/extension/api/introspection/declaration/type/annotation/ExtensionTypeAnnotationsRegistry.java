/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type.annotation;

import org.mule.metadata.api.annotation.AnnotationsRegistry;
import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.ExtensionObjectTypeHandler;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Contributes to the {@link AnnotationsRegistry} service to provide a friendly name for the custom annotations used by the
 * {@link ExtensionObjectTypeHandler}
 *
 * @since 1.0
 */
public class ExtensionTypeAnnotationsRegistry implements AnnotationsRegistry {

  @Override
  public Map<String, Class<? extends TypeAnnotation>> getRegistry() {
    // TODO MULE-10209 customize how custom type annotations are serialized
    return ImmutableMap.<String, Class<? extends TypeAnnotation>>builder()
        .put(XmlHintsAnnotation.NAME, XmlHintsAnnotation.class)
        .put(TypeAliasAnnotation.NAME, TypeAliasAnnotation.class)
        .put(ExtensibleTypeAnnotation.NAME, ExtensibleTypeAnnotation.class)
        .put(ExpressionSupportAnnotation.NAME, ExpressionSupportAnnotation.class)
        .put(FlattenedTypeAnnotation.NAME, FlattenedTypeAnnotation.class)
        .put(LayoutTypeAnnotation.NAME, LayoutTypeAnnotation.class)
        .build();
  }
}
