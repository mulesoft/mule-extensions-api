/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

import org.mule.extensions.introspection.DataType;

public class ParameterConstruct<T extends ParameterConstruct> implements Construct
{

    private final DeclarationConstruct declaration;
    private final ParameterDeclaration parameter;
    private final HasParameters owner;

    ParameterConstruct(HasParameters owner, ParameterDeclaration parameter, DeclarationConstruct declaration)
    {
        this.declaration = declaration;
        this.owner = owner;
        this.parameter = parameter;
    }

    public T ofType(Class<?> type, Class<?>... parametrizedTypes)
    {
        return ofType(DataType.of(type, parametrizedTypes));
    }

    public T ofType(DataType dataType)
    {
        parameter.setType(dataType);
        return (T) this;
    }

    public T describedAs(String description)
    {
        parameter.setDescription(description);
        return (T) this;
    }

    public T whichIsNotDynamic()
    {
        parameter.setDynamic(false);
        return (T) this;
    }

    public T whichIsDynamic()
    {
        parameter.setDynamic(true);
        return (T) this;
    }

    public ConfigurationConstruct withConfig(String name)
    {
        return getRootConstruct().withConfig(name);
    }

    public OperationConstruct withOperation(String name)
    {
        return getRootConstruct().withOperation(name);
    }

    public WithParameters with()
    {
        return new WithParameters(owner, getRootConstruct());
    }

    @Override
    public DeclarationConstruct getRootConstruct()
    {
        return declaration;
    }

    ParameterDeclaration getParameter()
    {
        return parameter;
    }
}
