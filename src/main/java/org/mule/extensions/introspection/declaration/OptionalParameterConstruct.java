/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

/**
 * A specialization of {@link ParameterConstruct} for optional parameters.
 * It allows adding properties that only apply to optional parameters
 *
 * @since 1.0
 */
public class OptionalParameterConstruct extends ParameterConstruct<OptionalParameterConstruct>
{

    OptionalParameterConstruct(HasParameters owner, ParameterDeclaration parameter, DeclarationConstruct declaration)
    {
        super(owner, parameter, declaration);
    }

    /**
     * Adds a default value for the parameter
     *
     * @param defaultValue a default value
     * @return this construct
     */
    public OptionalParameterConstruct defaultingTo(Object defaultValue)
    {
        getDeclaration().setDefaultValue(defaultValue);
        return this;
    }
}
