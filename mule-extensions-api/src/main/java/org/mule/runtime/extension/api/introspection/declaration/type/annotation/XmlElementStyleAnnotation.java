/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type.annotation;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import org.mule.metadata.api.annotation.TypeAnnotation;

/**
 * Contains directives regarding syntax and semantics of the generated XML DSL
 *
 * @since 1.0
 */
public class XmlElementStyleAnnotation implements TypeAnnotation
{

    private final boolean allowInlineDefinition;
    private final boolean allowReferences;

    public XmlElementStyleAnnotation(boolean allowInlineDefinition, boolean allowReferences)
    {
        this.allowInlineDefinition = allowInlineDefinition;
        this.allowReferences = allowReferences;
    }

    @Override
    public String getName()
    {
        return "xmlElementStyle";
    }

    public boolean isAllowInlineDefinition()
    {
        return allowInlineDefinition;
    }

    public boolean isAllowReferences()
    {
        return allowReferences;
    }

    @Override
    public int hashCode()
    {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof XmlElementStyleAnnotation)
        {
            XmlElementStyleAnnotation other = (XmlElementStyleAnnotation) obj;
            return allowInlineDefinition == other.isAllowInlineDefinition() && allowReferences == other.isAllowReferences();
        }

        return false;
    }
}
