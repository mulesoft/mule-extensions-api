/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class OperationDeclaration
{

    private final String name;
    private String description;
    private List<ParameterDeclaration> parameters = new LinkedList<>();

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

    public List<ParameterDeclaration> getParameters()
    {
        return Collections.unmodifiableList(parameters);
    }

    public OperationDeclaration addParameter(ParameterDeclaration parameter)
    {
        if (parameter == null)
        {
            throw new IllegalArgumentException("Can't add a null parameter");
        }

        parameters.add(parameter);
        return this;
    }
}
