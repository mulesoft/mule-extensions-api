/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata.dto;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import static org.mule.runtime.extension.internal.persistence.metadata.dto.OutputMetadata.OUTPUT;
import static org.mule.runtime.extension.internal.persistence.metadata.dto.OutputMetadata.OUTPUT_ATTRIBUTES;
import static org.mule.runtime.extension.internal.persistence.metadata.dto.OutputMetadata.OUTPUT_PAYLOAD;
import static org.mule.runtime.extension.internal.persistence.metadata.dto.ParameterMetadata.CONTENT;
import org.mule.runtime.api.metadata.descriptor.ImmutableComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ParameterMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Optional;

/**
 * Data transfer object that carries data that represents a {@link MetadataResult} of a {@link ImmutableComponentMetadataDescriptor}
 * and enables the ease of serialization an deserialization.
 *
 * @since 1.0
 */
public class ComponentMetadataResult implements Descriptable<ImmutableComponentMetadataDescriptor> {

  final static String COMPONENT = "COMPONENT";

  private final String componentName;
  private final List<ParameterMetadata> parameters;
  private final OutputMetadata output;
  private final List<Failure> failures;

  public ComponentMetadataResult(MetadataResult<ImmutableComponentMetadataDescriptor> result) {
    this.componentName = result.get().getName();
    this.output = new OutputMetadata(result.get());
    this.parameters = getParametersMetadata(result.get());
    this.failures = collectFailures(result);
  }

  private List<ParameterMetadata> getParametersMetadata(ImmutableComponentMetadataDescriptor componentResult) {
    ImmutableList.Builder<ParameterMetadata> parameterList = ImmutableList.builder();

    parameterList.addAll(componentResult.getParametersMetadata()
        .stream()
        .map(p -> new ParameterMetadata(p.get().getName(), p.get().getType(), false))
        .collect(toList()));

    Optional<MetadataResult<ParameterMetadataDescriptor>> contentMetadata = componentResult.getContentMetadata();
    if (contentMetadata.isPresent()) {
      ParameterMetadataDescriptor descriptor = contentMetadata.get().get();
      parameterList.add(new ParameterMetadata(descriptor.getName(), descriptor.getType(), true));
    }

    return parameterList.build();
  }

  public MetadataResult<ImmutableComponentMetadataDescriptor> toComponentMetadataResult() {
    return toDescriptorResult(failures);
  }

  private List<Failure> collectFailures(MetadataResult<ImmutableComponentMetadataDescriptor> result) {
    ImmutableMap.Builder<String, MetadataFailure> failures = ImmutableMap.builder();
    ImmutableComponentMetadataDescriptor descriptor = result.get();
    if (result.isSuccess()) {
      return emptyList();
    }

    failures.put(COMPONENT, result.getFailure().get());

    if (descriptor != null) {
      Optional<MetadataResult<ParameterMetadataDescriptor>> contentMetadata = descriptor.getContentMetadata();
      if (contentMetadata.isPresent() && !contentMetadata.get().isSuccess()) {
        failures.put(CONTENT, contentMetadata.get().getFailure().get());
      }

      MetadataResult<OutputMetadataDescriptor> outputMetadata = descriptor.getOutputMetadata();
      if (!outputMetadata.isSuccess()) {
        MetadataResult<TypeMetadataDescriptor> payloadMetadata = outputMetadata.get().getPayloadMetadata();
        if (!payloadMetadata.isSuccess()) {
          failures.put(OUTPUT_PAYLOAD, payloadMetadata.getFailure().get());
        }

        MetadataResult<TypeMetadataDescriptor> attributesMetadata = outputMetadata.get().getAttributesMetadata();
        if (!attributesMetadata.isSuccess()) {
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

  @Override
  public MetadataResult<ImmutableComponentMetadataDescriptor> toDescriptorResult(List<Failure> failures) {
    Optional<Failure> metadataFailure = getComponentFailure(failures, COMPONENT);
    Optional<ParameterMetadata> content = parameters.stream().filter(ParameterMetadata::isDynamic).findFirst();

    List<MetadataResult<ParameterMetadataDescriptor>> parameterDescriptors = parameters.stream()
        .filter(p -> !p.isDynamic())
        .map(p -> p.toDescriptorResult(failures))
        .collect(toList());


    MetadataResult<ParameterMetadataDescriptor> contentResult =
        content.isPresent() ? content.get().toDescriptorResult(failures) : null;

    ImmutableComponentMetadataDescriptor descriptor = new ImmutableComponentMetadataDescriptor(componentName,
                                                                                               parameterDescriptors,
                                                                                               output
                                                                                                   .toDescriptorResult(failures),
                                                                                               contentResult);
    if (metadataFailure.isPresent()) {
      return failure(descriptor,
                     metadataFailure.get().getMessage(),
                     metadataFailure.get().getFailureCode(),
                     metadataFailure.get().getReason());
    }

    return success(descriptor);
  }
}
