/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

import org.mule.extensions.introspection.Extension;

/**
 * A construct is a flat, intermediate representation
 * of an extension model. It's used to describe an extension
 * though a fluent API without dealing with the specific rules
 * and details of the final {@link Extension} model.
 *
 * @since 1.0
 */
public interface Construct
{

    /**
     * Returns the root element of this construct
     *
     * @return a {@link DeclarationConstruct}
     */
    DeclarationConstruct getRootConstruct();
}
