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
 * Contributes to the {@link AnnotationsRegistry} service to provide a friendly name
 * for the custom annotations used by the {@link ExtensionObjectTypeHandler}
 *
 * @since 1.0
 */
public class ExtensionTypeAnnotationsRegistry implements AnnotationsRegistry
{

    @Override
    public Map<String, Class<? extends TypeAnnotation>> getRegistry()
    {
        return ImmutableMap.of(XmlHintsAnnotation.NAME, XmlHintsAnnotation.class,
                               TypeAliasAnnotation.NAME, TypeAliasAnnotation.class,
                               ExtensibleTypeAnnotation.NAME, ExtensibleTypeAnnotation.class,
                               ExpressionSupportAnnotation.NAME, ExpressionSupportAnnotation.class);
    }
}
