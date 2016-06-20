/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.parameter;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.introspection.AbstractNamedImmutableModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;

import java.util.Set;

/**
 * Immutable implementation of {@link ParameterModel}
 *
 * @since 1.0
 */
public final class ImmutableParameterModel extends AbstractNamedImmutableModel implements ParameterModel
{

    private final MetadataType type;
    private boolean ofDynamicType;
    private final boolean required;
    private final ExpressionSupport expressionSupport;
    private final Object defaultValue;

    /**
     * Creates a new instance with the given state
     *
     * @param name              the parameter's name. Cannot be blank and cannot be one of the values in {@link #RESERVED_NAMES}
     * @param description       the parameter's description
     * @param type              the parameter's {@link MetadataType}. Cannot be {@code null}
     * @param required          whether this parameter is required or not
     * @param expressionSupport the {@link ExpressionSupport} that applies to {@code this} {@link ParameterModel}
     * @param defaultValue      this parameter's default value
     * @param modelProperties   A {@link Set} of custom properties which extend this model
     * @throws IllegalArgumentException if {@code required} is {@code true} and {@code defaultValue} is not {@code null} at the same time
     */
    public ImmutableParameterModel(String name,
                                   String description,
                                   MetadataType type,
                                   boolean ofDynamicType,
                                   boolean required,
                                   ExpressionSupport expressionSupport,
                                   Object defaultValue,
                                   Set<ModelProperty> modelProperties)
    {
        super(name, description, modelProperties);

        this.type = type;
        this.required = required;
        this.expressionSupport = expressionSupport;
        this.defaultValue = defaultValue;
        this.ofDynamicType = ofDynamicType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataType getType()
    {
        return type;
    }

    @Override
    public boolean hasDynamicType()
    {
        return ofDynamicType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRequired()
    {
        return required;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpressionSupport getExpressionSupport()
    {
        return expressionSupport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue()
    {
        return defaultValue;
    }

}
