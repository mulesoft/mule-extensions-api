/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.api.temporary.MuleMessage;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.NullType;

/**
 * A definition of an operation in a {@link ExtensionModel}
 *
 * @since 1.0
 */
public interface OperationModel extends ComponentModel
{

    /**
     * Returns a {@link MetadataType} for the value that this operation sets
     * on the output {@link MuleMessage#getAttributes()} field.
     * <p>
     * If this operation does not modify that value, then a {@link NullType} instance
     * will be returned. Notice however that this <b>does not</b> mean that the property
     * will be set to {@code null} on the message, it means that whatever value it had
     * before the operation was executed will be preserved after it returns.
     *
     * @return a {@link MetadataType} representing the attribute types for the output messages
     */
    @Override
    MetadataType getAttributesType();
}
