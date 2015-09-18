/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.api.extension.introspection.declaration.fluent;

import org.mule.api.extension.introspection.DataType;
import org.mule.api.extension.introspection.Described;
import org.mule.api.extension.introspection.ParameterModel;

/**
 * A declaration object for a {@link ParameterModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ParameterModel}.
 * <p/>
 * By default, {@link #isDynamic()} returns {@code true} and
 * {@link #getDescription()} returns an empty {@link String}.
 *
 * @since 1.0
 */
public final class ParameterDeclaration extends BaseDeclaration<ParameterDeclaration> implements Described
{
    private String name;
    private String description = "";
    private boolean required;
    private boolean dynamic = true;
    private DataType type;
    private Object defaultValue = null;

    ParameterDeclaration()
    {
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public boolean isDynamic()
    {
        return dynamic;
    }

    public void setDynamic(boolean dynamic)
    {
        this.dynamic = dynamic;
    }

    public DataType getType()
    {
        return type;
    }

    public void setType(DataType type)
    {
        this.type = type;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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
