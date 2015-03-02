/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

import org.mule.extension.introspection.Extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A declaration object for a {@link Extension}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link Extension}
 *
 * @since 1.0
 */
public final class Declaration extends CapableDeclaration<Declaration>
{

    private final String name;
    private final String version;
    private String description;

    private List<ConfigurationDeclaration> configurations = new ArrayList<>();
    private List<OperationDeclaration> operations = new LinkedList<>();

    Declaration(String name, String version)
    {
        this.name = name;
        this.version = version;
    }

    public String getVersion()
    {
        return version;
    }

    public String getName()
    {
        return name;
    }

    /**
     * @return
     */
    public List<ConfigurationDeclaration> getConfigurations()
    {
        return Collections.unmodifiableList(configurations);
    }

    /**
     * Adds a {@link ConfigurationDeclaration}
     *
     * @param config a not {@code null} {@link ConfigurationDeclaration}
     * @return this declaration
     * @throws {@link IllegalArgumentException} if {@code config} is {@code null}
     */
    public Declaration addConfig(ConfigurationDeclaration config)
    {
        if (config == null)
        {
            throw new IllegalArgumentException("Can't add a null config");
        }

        configurations.add(config);
        return this;
    }

    /**
     * @return an unmodifiable {@link List} with
     * the available {@link OperationDeclaration}s
     */
    public List<OperationDeclaration> getOperations()
    {
        return Collections.unmodifiableList(operations);
    }

    /**
     * Adds a {@link OperationDeclaration}
     *
     * @param operation a not {@code null} {@link OperationDeclaration}
     * @return this declaration
     * @throws {@link IllegalArgumentException} if {@code operation} is {@code null}
     */
    public Declaration addOperation(OperationDeclaration operation)
    {
        if (operation == null)
        {
            throw new IllegalArgumentException("Can't add a null operation");
        }

        operations.add(operation);
        return this;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
