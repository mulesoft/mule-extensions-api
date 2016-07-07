/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata.dto;

import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import static org.mule.runtime.extension.api.persistence.metadata.dto.ComponentResult.COMPONENT;
import org.mule.runtime.api.metadata.descriptor.ImmutableComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ParameterMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

/**
 * Data transfer object that carries data that represents a {@link MetadataResult} of a {@link ImmutableComponentMetadataDescriptor}
 * and enables the ease of serialization an deserialization.
 *
 * @since 1.0
 */
public class ComponentMetadataResult implements Descriptable<ImmutableComponentMetadataDescriptor>
{

    private final String componentName;
    private final List<ParameterMetadata> parameters;
    private final OutputMetadata output;
    private final ComponentResult result;

    public ComponentMetadataResult(MetadataResult<ImmutableComponentMetadataDescriptor> result)
    {
        this.componentName = result.get().getName();
        this.output = new OutputMetadata(result.get());
        this.parameters = getParametersMetadata(result.get());
        this.result = new ComponentResult(result);
    }

    private List<ParameterMetadata> getParametersMetadata(ImmutableComponentMetadataDescriptor componentResult)
    {
        ImmutableList.Builder<ParameterMetadata> parameterList = ImmutableList.builder();

        parameterList.addAll(componentResult.getParametersMetadata()
                                     .stream()
                                     .map(p -> new ParameterMetadata(p.get().getName(), p.get().getType(), false))
                                     .collect(toList()));

        Optional<MetadataResult<ParameterMetadataDescriptor>> contentMetadata = componentResult.getContentMetadata();
        if (contentMetadata.isPresent())
        {
            ParameterMetadataDescriptor descriptor = contentMetadata.get().get();
            parameterList.add(new ParameterMetadata(descriptor.getName(), descriptor.getType(), true));
        }

        return parameterList.build();
    }

    public MetadataResult<ImmutableComponentMetadataDescriptor> toComponentMetadataResult()
    {
        List<Failure> failures = result.getFailures();
        return toDescriptorResult(failures);
    }

    @Override
    public MetadataResult<ImmutableComponentMetadataDescriptor> toDescriptorResult(List<Failure> failures)
    {
        Optional<Failure> metadataFailure = getComponentFailure(failures, COMPONENT);
        Optional<ParameterMetadata> content = parameters.stream().filter(ParameterMetadata::isDynamic).findFirst();

        List<MetadataResult<ParameterMetadataDescriptor>> parameterDescriptors = parameters.stream()
                .filter(p -> !p.isDynamic())
                .map(p -> p.toDescriptorResult(failures))
                .collect(toList());


        MetadataResult<ParameterMetadataDescriptor> contentResult = content.isPresent() ? content.get().toDescriptorResult(failures) : null;

        ImmutableComponentMetadataDescriptor descriptor = new ImmutableComponentMetadataDescriptor(componentName,
                                                                                                   parameterDescriptors,
                                                                                                   output.toDescriptorResult(failures),
                                                                                                   contentResult);
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
