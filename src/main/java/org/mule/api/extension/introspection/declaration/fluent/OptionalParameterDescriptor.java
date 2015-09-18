/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.api.extension.introspection.declaration.fluent;

/**
 * A specialization of {@link ParameterDescriptor} for optional parameters.
 * It allows adding properties that only apply to optional parameters
 *
 * @since 1.0
 */
public class OptionalParameterDescriptor extends ParameterDescriptor<OptionalParameterDescriptor>
{

    OptionalParameterDescriptor(HasParameters owner, ParameterDeclaration parameter, DeclarationDescriptor declaration)
    {
        super(owner, parameter, declaration);
    }

    /**
     * Adds a default value for the parameter
     *
     * @param defaultValue a default value
     * @return this descriptor
     */
    public OptionalParameterDescriptor defaultingTo(Object defaultValue)
    {
        getDeclaration().setDefaultValue(defaultValue);
        return this;
    }
}
