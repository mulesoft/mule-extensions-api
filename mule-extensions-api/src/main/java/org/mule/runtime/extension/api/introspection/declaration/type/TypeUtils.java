/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type;

import static com.google.common.base.Predicates.not;
import static org.mule.metadata.utils.MetadataTypeUtils.getSingleAnnotation;
import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Parameter;
import org.mule.runtime.extension.api.annotation.param.Ignore;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Utility class to handle Java types and their relationship with the {@link MetadataType} model
 *
 * @since 1.0
 */
public final class TypeUtils
{

    private TypeUtils()
    {
    }

    /**
     * Returns all the {@link Field}s in the given {@code extensionType} which are annotated
     * with {@link Parameter} but also do not have the {@link Ignore} one.
     * <p>
     * The introspection also includes parent classes.
     *
     * @param extensionType the class to introspect.
     * @return a {@link Collection} of {@link Field fields}. May be empty but will never be {@code null}
     */
    public static Collection<Field> getParameterFields(Class<?> extensionType)
    {
        return getAllFields(extensionType, withAnnotation(Parameter.class), not(withAnnotation(Ignore.class)));
    }

    /**
     * Checks the given {@code field} for the {@link Alias} annotation. If present, {@link Alias#value()}
     * is returned. Otherwise, {@link Field#getName()} is returned.
     *
     * @param field a {@link Field}
     * @return the field's alias
     */
    public static String getAlias(Field field)
    {
        Alias alias = field.getAnnotation(Alias.class);
        String name = alias != null ? alias.value() : null;
        return name == null || name.length() == 0 ? field.getName() : name;
    }

    /**
     * Checks the given {@code metadataType} for the {@link ExpressionSupportAnnotation}.
     * <p>
     * If present, the {@link ExpressionSupportAnnotation#getExpressionSupport()}
     * value is returned. Otherwise, it defaults to {@link ExpressionSupport#SUPPORTED}
     *
     * @param metadataType a {@link MetadataType}
     * @return a {@link ExpressionSupport}
     */
    public static ExpressionSupport getExpressionSupport(MetadataType metadataType)
    {
        return getSingleAnnotation(metadataType, ExpressionSupportAnnotation.class)
                .map(ExpressionSupportAnnotation::getExpressionSupport)
                .orElse(ExpressionSupport.SUPPORTED);
    }

    public static boolean acceptsReferences(MetadataType metadataType)
    {
        return getSingleAnnotation(metadataType, XmlHintsAnnotation.class)
                .map(XmlHintsAnnotation::isAllowReferences)
                .orElse(true);
    }
}
