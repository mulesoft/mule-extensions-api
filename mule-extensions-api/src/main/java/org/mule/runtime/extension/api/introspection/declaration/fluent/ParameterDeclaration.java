/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.SUPPORTED;
import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.metadata.api.model.MetadataType;

/**
 * A declaration object for a {@link ParameterModel}. It contains raw,
 * unvalidated data which is used to declare the structure of a
 * {@link ParameterModel}.
 * <p>
 * By default, {@link #getExpressionSupport()} ()} returns
 * {@link ExpressionSupport#SUPPORTED} and {@link #getDescription()}
 * returns an empty {@link String}.
 *
 * @since 1.0
 */
public class ParameterDeclaration extends BaseDeclaration<ParameterDeclaration>
{

    private boolean required;
    private ExpressionSupport expressionSupport = SUPPORTED;
    private MetadataType type;
    private Object defaultValue = null;

    /**
     * {@inheritDoc}
     */
    public ParameterDeclaration(String name)
    {
        super(name);
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public ExpressionSupport getExpressionSupport()
    {
        return expressionSupport;
    }

    public void setExpressionSupport(ExpressionSupport expressionSupport)
    {
        this.expressionSupport = expressionSupport;
    }

    public MetadataType getType()
    {
        return type;
    }

    public void setType(MetadataType type)
    {
        this.type = type;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue)
    {
        this.defaultValue = defaultValue;
    }
}
