/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.type;

import static org.mule.extension.api.introspection.parameter.ExpressionSupport.SUPPORTED;
import static org.mule.extension.api.introspection.declaration.type.TypeUtils.getAlias;
import org.mule.extension.api.annotation.Expression;
import org.mule.metadata.api.annotation.DefaultValueAnnotation;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.java.handler.DefaultObjectFieldHandler;
import org.mule.metadata.java.handler.ObjectFieldHandler;
import org.mule.metadata.java.handler.TypeHandlerManager;
import org.mule.metadata.java.utils.ParsingContext;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

/**
 * An implementation of {@link ObjectFieldHandler} which navigates an object's {@link Field}s
 * by looking for, and following the rules, of the Extensions API annotations.
 * <p>
 * If the introspected class does not contain such annotations, then {@link Introspector#getBeanInfo(Class)}
 * is used to obtain the class properties. Those properties will be managed as optional parameters, without
 * a default value
 *
 * @since 1.0
 */
final class ExtensionsFieldHandler implements ObjectFieldHandler
{

    @Override
    public void handleFields(Class<?> clazz, TypeHandlerManager typeHandlerManager, ParsingContext context, ObjectTypeBuilder builder)
    {
        //TODO: MULE-9454
        if (clazz.getName().equals("org.mule.api.NestedProcessor"))
        {
            return;
        }

        Collection<Field> fields = TypeUtils.getParameterFields(clazz);
        if (fields.isEmpty())
        {
            fallbackToBeanProperties(clazz, typeHandlerManager, context, builder);
            return;
        }

        for (Field field : fields)
        {
            final ObjectFieldTypeBuilder<?> fieldBuilder = builder.addField();
            fieldBuilder.key(getAlias(field));

            setOptionalAndDefault(field, fieldBuilder);
            setExpressionSupport(field, fieldBuilder);
            setFieldType(typeHandlerManager, context, field, fieldBuilder);
        }
    }

    private void setFieldType(TypeHandlerManager typeHandlerManager, ParsingContext context, Field field, ObjectFieldTypeBuilder<?> fieldBuilder)
    {
        final Type fieldType = field.getGenericType();
        final Optional<TypeBuilder<?>> typeBuilder = context.getTypeBuilder(fieldType);

        if (typeBuilder.isPresent())
        {
            fieldBuilder.value(typeBuilder.get());
        }
        else
        {
            typeHandlerManager.handle(fieldType, context, fieldBuilder.value());
        }
    }

    private void setExpressionSupport(Field field, ObjectFieldTypeBuilder<?> fieldBuilder)
    {
        Expression expression = field.getAnnotation(Expression.class);
        fieldBuilder.with(new ExpressionSupportAnnotation(expression != null ? expression.value() : SUPPORTED));
    }

    private void setOptionalAndDefault(Field field, ObjectFieldTypeBuilder<?> fieldBuilder)
    {
        org.mule.extension.api.annotation.param.Optional optionalAnnotation = field.getAnnotation(org.mule.extension.api.annotation.param.Optional.class);
        if (optionalAnnotation != null)
        {
            fieldBuilder.required(false);
            if (optionalAnnotation.defaultValue() != org.mule.extension.api.annotation.param.Optional.NULL)
            {
                fieldBuilder.with(new DefaultValueAnnotation(optionalAnnotation.defaultValue()));
            }
        }
        else
        {
            fieldBuilder.required(true);
        }
    }

    private void fallbackToBeanProperties(Class<?> clazz, TypeHandlerManager typeHandlerManager, ParsingContext context, ObjectTypeBuilder builder)
    {
        new DefaultObjectFieldHandler().handleFields(clazz, typeHandlerManager, context, builder);
    }
}
