/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.property;

import org.mule.extension.api.introspection.property.StudioModelProperty;

/**
 * Immutable implementation of {@link StudioModelProperty}
 *
 * @since 1.0
 */
public final class ImmutableStudioModelProperty implements StudioModelProperty
{

    private final String editorFileName;
    private final boolean derived;

    public ImmutableStudioModelProperty(String editorFileName, boolean derived)
    {
        this.editorFileName = editorFileName;
        this.derived = derived;
    }

    @Override
    public String getEditorFileName()
    {
        return editorFileName;
    }

    @Override
    public boolean isDerived()
    {
        return derived;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append("ImmutableStudioModelProperty{")
                .append("editorFileName='").append(editorFileName).append('\'')
                .append(", derived=").append(derived)
                .append('}').toString();
    }
}
