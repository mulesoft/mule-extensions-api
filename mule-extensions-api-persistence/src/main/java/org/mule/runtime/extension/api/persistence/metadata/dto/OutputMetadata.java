/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata.dto;

import static java.util.Collections.emptyList;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import static org.mule.runtime.extension.api.persistence.metadata.dto.ComponentResult.OUTPUT;
import static org.mule.runtime.extension.api.persistence.metadata.dto.ComponentResult.OUTPUT_ATTRIBUTES;
import static org.mule.runtime.extension.api.persistence.metadata.dto.ComponentResult.OUTPUT_PAYLOAD;
import org.mule.runtime.api.metadata.descriptor.ImmutableComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableOutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * DTO that represents a {@link OutputMetadataDescriptor} into a serializable format.
 *
 * @since 1.0
 */
public class OutputMetadata implements Descriptable<OutputMetadataDescriptor>
{

    private final TypeMetadata content;
    private final TypeMetadata attributes;

    OutputMetadata(ImmutableComponentMetadataDescriptor result)
    {
        OutputMetadataDescriptor outputDescriptor = result.getOutputMetadata().get();
        this.content = new TypeMetadata(outputDescriptor.getPayloadMetadata().get().getType(), true);
        this.attributes = new TypeMetadata(outputDescriptor.getAttributesMetadata().get().getType(), true);
    }

    TypeMetadata getContent()
    {
        return content;
    }

    TypeMetadata getAttributes()
    {
        return attributes;
    }

    @Override
    public MetadataResult<OutputMetadataDescriptor> toDescriptorResult(List<Failure> failures)
    {
        Optional<Failure> metadataFailure = getComponentFailure(failures, OUTPUT);
        List<Failure> attributesFailure = getComponentFailure(failures, OUTPUT_ATTRIBUTES).map(Collections::singletonList).orElse(emptyList());
        List<Failure> payloadFailure = getComponentFailure(failures, OUTPUT_PAYLOAD).map(Collections::singletonList).orElse(emptyList());

        ImmutableOutputMetadataDescriptor descriptor = new ImmutableOutputMetadataDescriptor(content.toDescriptorResult(payloadFailure),
                                                                                             attributes.toDescriptorResult(attributesFailure));
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
