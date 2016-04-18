/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.runtime.api.temporary.MuleMessage;
import org.mule.extension.api.introspection.operation.OperationModel;
import org.mule.extension.api.introspection.parameter.ParametrizedModel;
import org.mule.extension.api.introspection.source.SourceModel;
import org.mule.metadata.api.model.MetadataType;

/**
 * A definition of an component in a {@link ExtensionModel}. This model groups all the common contracts between extension
 * components like {@link OperationModel}, {@link SourceModel}, etc.
 *
 * @since 1.0
 */
public interface ComponentModel extends Described, EnrichableModel, ParametrizedModel
{

    /**
     * Returns a {@link MetadataType} for the value that this component sets
     * on the output {@link MuleMessage#getPayload()} field.
     *
     * @return a {@link MetadataType} representing the payload type for the output messages
     */
    MetadataType getReturnType();

    /**
     * Returns a {@link MetadataType} for the value that this component sets
     * on the output {@link MuleMessage#getAttributes()} field.
     *
     * @return a {@link MetadataType} representing the attribute types for the output messages
     */
    MetadataType getAttributesType();
}
