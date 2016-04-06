/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;


import org.mule.metadata.api.model.MetadataType;

/**
 * A definition of a message source in an {@link ExtensionModel}
 *
 * @since 1.0
 */
public interface SourceModel extends Described, EnrichableModel, ParametrizedModel
{

    /**
     * @return a {@link MetadataType} representing the payload types for the generated messages
     */
    MetadataType getReturnType();

    /**
     * @return a {@link MetadataType} representing the attribute types for the generated messages
     */
    MetadataType getAttributesType();
}
