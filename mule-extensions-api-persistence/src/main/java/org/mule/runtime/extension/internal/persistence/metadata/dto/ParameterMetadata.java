/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata.dto;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.descriptor.ImmutableParameterMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ParameterMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.List;
import java.util.Optional;

/**
 * DTO that represents a {@link ParameterMetadataDescriptor} into a serializable format.
 *
 * @since 1.0
 */
class ParameterMetadata implements Descriptable<ParameterMetadataDescriptor>
{
    final static String CONTENT = "CONTENT";

    private final String name;
    private final MetadataType type;
    private final boolean isDynamic;

    ParameterMetadata(String name, MetadataType type, boolean isDynamic)
    {
        this.isDynamic = isDynamic;
        this.type = type;
        this.name = name;
    }

    String getName()
    {
        return name;
    }

    MetadataType getType()
    {
        return type;
    }

    boolean isDynamic()
    {
        return isDynamic;
    }

    @Override
    public MetadataResult<ParameterMetadataDescriptor> toDescriptorResult(List<Failure> failures)
    {
        Optional<Failure> metadataFailure = getComponentFailure(failures, isDynamic ? CONTENT : name);
        ImmutableParameterMetadataDescriptor descriptor = new ImmutableParameterMetadataDescriptor(name, type);
        if (metadataFailure.isPresent())
        {
            return failure(descriptor,
                           metadataFailure.get().getMessage(),
                           metadataFailure.get().getFailureCode(),
                           metadataFailure.get().getReason());
        }
        return success(descriptor);
    }
}
