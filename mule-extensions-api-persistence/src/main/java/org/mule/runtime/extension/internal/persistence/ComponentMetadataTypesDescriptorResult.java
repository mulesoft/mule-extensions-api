/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataTypesDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO for serializing a {@link MetadataResult<ComponentMetadataTypesDescriptor>}.
 *
 * @since 4.4
 */
public class ComponentMetadataTypesDescriptorResult implements Descriptable<MetadataResult<ComponentMetadataTypesDescriptor>> {

  private Map<String, MetadataType> input = new HashMap<>();
  private MetadataType output;
  private MetadataType outputAttributes;
  private List<MetadataFailure> failures;


  public ComponentMetadataTypesDescriptorResult(MetadataResult<ComponentMetadataTypesDescriptor> result) {
    ComponentMetadataTypesDescriptor descriptor = result.get();
    if (descriptor != null) {
      this.input = descriptor.getInputMetadata();
      this.output = descriptor.getOutputMetadata().orElse(null);
      this.outputAttributes = descriptor.getOutputAttributesMetadata().orElse(null);
    }

    this.failures = result.getFailures();
  }

  public Map<String, MetadataType> getInput() {
    return input;
  }

  public MetadataType getOutput() {
    return output;
  }

  public MetadataType getOutputAttributes() {
    return outputAttributes;
  }

  public List<MetadataFailure> getFailures() {
    return failures;
  }

  @Override
  public MetadataResult<ComponentMetadataTypesDescriptor> toDescriptor() {
    return failures.isEmpty() ? success(new ComponentMetadataTypesDescriptor(input, output, outputAttributes))
        : failure(failures);
  }

}
