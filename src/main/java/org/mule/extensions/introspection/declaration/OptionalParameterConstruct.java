/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

public class OptionalParameterConstruct extends ParameterConstruct<OptionalParameterConstruct>
{

    OptionalParameterConstruct(HasParameters owner, ParameterDeclaration parameter, DeclarationConstruct declaration)
    {
        super(owner, parameter, declaration);
    }

    public OptionalParameterConstruct defaultingTo(Object defaultValue)
    {
        getParameter().setDefaultValue(defaultValue);
        return this;
    }
}
