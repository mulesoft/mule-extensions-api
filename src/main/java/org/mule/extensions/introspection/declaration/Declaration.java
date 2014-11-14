/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class Declaration
{

    private final String name;
    private final String version;
    private String description;

    private List<ConfigurationDeclaration> configurations = new ArrayList<>();
    private List<OperationDeclaration> operations = new LinkedList<>();
    private Set<Object> capabilities = new HashSet<>();


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

    public List<ConfigurationDeclaration> getConfigurations()
    {
        return Collections.unmodifiableList(configurations);
    }

    public Declaration addConfig(ConfigurationDeclaration config)
    {
        if (config == null)
        {
            throw new IllegalArgumentException("Can't add a null config");
        }

        configurations.add(config);
        return this;
    }

    public List<OperationDeclaration> getOperations()
    {
        return Collections.unmodifiableList(operations);
    }

    public Declaration addOperation(OperationDeclaration operation)
    {
        if (operation == null)
        {
            throw new IllegalArgumentException("Can't add a null operation");
        }

        operations.add(operation);
        return this;
    }

    public Set<Object> getCapabilities()
    {
        return Collections.unmodifiableSet(capabilities);
    }

    public Declaration addCapability(Object capability)
    {
        if (capability == null)
        {
            throw new IllegalArgumentException("Can't add a null capability");
        }

        capabilities.add(capability);
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
