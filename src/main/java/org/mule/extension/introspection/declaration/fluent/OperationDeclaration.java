/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration.fluent;

import org.mule.extension.introspection.OperationModel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A declaration object for a {@link OperationModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link OperationModel}
 *
 * @since 1.0
 */
public final class OperationDeclaration extends BaseDeclaration<OperationDeclaration>
{

    private final String name;
    private String description = "";
    private List<ParameterDeclaration> parameters = new LinkedList<>();
    private OperationExecutorFactory executorFactory;

    OperationDeclaration(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return an unmodifiable {@link List} with the available
     * {@link ParameterDeclaration}s
     */
    public List<ParameterDeclaration> getParameters()
    {
        return Collections.unmodifiableList(parameters);
    }

    /**
     * Adds a {@link ParameterDeclaration}
     *
     * @param parameter a not {@code null} {@link ParameterDeclaration}
     * @return this declaration
     * @throws {@link IllegalArgumentException} if {@code parameter} is {@code null}
     */
    public OperationDeclaration addParameter(ParameterDeclaration parameter)
    {
        if (parameter == null)
        {
            throw new IllegalArgumentException("Can't add a null parameter");
        }

        parameters.add(parameter);
        return this;
    }

    public OperationExecutorFactory getExecutorFactory()
    {
        return executorFactory;
    }

    public void setExecutorFactory(OperationExecutorFactory executorFactory)
    {
        this.executorFactory = executorFactory;
    }
}
