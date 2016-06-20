/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataManager;

/**
 * Represents the output of a {@link ComponentModel Component}
 *
 * @since 1.0
 */
public interface OutputModel extends Described, EnrichableModel
{

    /**
     * Returns the type of the {@link ComponentModel Component}'s {@link OutputModel output}
     *
     * @return a not {@code null} {@link MetadataType}
     */
    MetadataType getType();

    /**
     * Returns {@code true} if the type of the {@link ComponentModel Component}'s
     * {@link OutputModel output} is of dynamic kind, and has to be discovered
     * during design time using the {@link MetadataManager} service.
     *
     * @return {@code true} if {@code this} element type is of dynamic kind
     */
    boolean hasDynamicType();

}
