/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata.dto;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import org.mule.runtime.api.metadata.descriptor.ImmutableComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ParameterMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Optional;

/**
 * DTO that represents a {@link MetadataResult} into a serializable format.
 *
 * @since 1.0
 */
class ComponentResult
{
    final static String OUTPUT_PAYLOAD = "OUTPUT_PAYLOAD";
    final static String OUTPUT_ATTRIBUTES = "OUTPUT_ATTRIBUTES";
    final static String OUTPUT = "OUTPUT";
    final static String CONTENT = "CONTENT";
    final static String COMPONENT = "COMPONENT";

    private final List<Failure> failures;
    private final boolean isSuccess;

    ComponentResult(MetadataResult<ImmutableComponentMetadataDescriptor> result)
    {
        this.isSuccess = result.isSuccess();
        this.failures = collectFailures(result);
    }

    private List<Failure> collectFailures(MetadataResult<ImmutableComponentMetadataDescriptor> result)
    {
        ImmutableMap.Builder<String, MetadataFailure> failures = ImmutableMap.builder();
        ImmutableComponentMetadataDescriptor descriptor = result.get();
        if (result.isSuccess())
        {
            return emptyList();
        }

        failures.put(COMPONENT, result.getFailure().get());

        if (descriptor != null)
        {
            Optional<MetadataResult<ParameterMetadataDescriptor>> contentMetadata = descriptor.getContentMetadata();
            if (contentMetadata.isPresent() && !contentMetadata.get().isSuccess())
            {
                failures.put(CONTENT, contentMetadata.get().getFailure().get());
            }

            MetadataResult<OutputMetadataDescriptor> outputMetadata = descriptor.getOutputMetadata();
            if (!outputMetadata.isSuccess())
            {
                MetadataResult<TypeMetadataDescriptor> payloadMetadata = outputMetadata.get().getPayloadMetadata();
                if (!payloadMetadata.isSuccess())
                {
                    failures.put(OUTPUT_PAYLOAD, payloadMetadata.getFailure().get());
                }

                MetadataResult<TypeMetadataDescriptor> attributesMetadata = outputMetadata.get().getAttributesMetadata();
                if (!attributesMetadata.isSuccess())
                {
                    failures.put(OUTPUT_ATTRIBUTES, attributesMetadata.getFailure().get());
                }

                failures.put(OUTPUT, payloadMetadata.getFailure().get());
            }
        }

        return failures.build()
                .entrySet()
                .stream()
                .map(e -> new Failure(e.getValue(), e.getKey()))
                .collect(toList());
    }

    boolean isSuccess()
    {
        return isSuccess;
    }

    List<Failure> getFailures()
    {
        return failures;
    }
}
