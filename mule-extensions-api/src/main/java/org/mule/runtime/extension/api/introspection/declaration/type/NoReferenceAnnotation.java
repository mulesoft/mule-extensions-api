/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type;

import org.mule.metadata.api.annotation.TypeAnnotation;

/**
 * Marks that the annotated type does not accepts
 * references to objects in the mule registry.
 * <p>
 * All static String values should be mapped to the actual String value
 *
 * @since 1.0
 */
public class NoReferenceAnnotation implements TypeAnnotation
{

    @Override
    public String getName()
    {
        return "noRef";
    }
}
