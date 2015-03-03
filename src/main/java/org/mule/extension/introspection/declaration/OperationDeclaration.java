/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

import org.mule.extension.introspection.Operation;
import org.mule.extension.introspection.OperationImplementation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A declaration object for a {@link Operation}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link Operation}
 *
 * @since 1.0
 */
public final class OperationDeclaration extends CapableDeclaration<OperationDeclaration>
{

    private final String name;
    private String description = "";
    private List<ParameterDeclaration> parameters = new LinkedList<>();
    private OperationImplementation implementation;

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

    public OperationImplementation getImplementation()
    {
        return implementation;
    }

    public void setImplementation(OperationImplementation implementation)
    {
        this.implementation = implementation;
    }
}
