/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
 * @since 1.4
 */
public class ComponentMetadataTypesDescriptorResult implements Descriptable<MetadataResult<ComponentMetadataTypesDescriptor>> {

  private Map<String, MetadataType> inputMetadata = new HashMap<>();
  private MetadataType outputMetadata;
  private MetadataType outputAttributesMetadata;
  private List<MetadataFailure> failures;


  public ComponentMetadataTypesDescriptorResult(MetadataResult<ComponentMetadataTypesDescriptor> result) {
    ComponentMetadataTypesDescriptor descriptor = result.get();
    if (descriptor != null) {
      this.inputMetadata = descriptor.getInputMetadata();
      this.outputMetadata = descriptor.getOutputMetadata().orElse(null);
      this.outputAttributesMetadata = descriptor.getOutputAttributesMetadata().orElse(null);
    }

    this.failures = result.getFailures();
  }

  public Map<String, MetadataType> getInputMetadata() {
    return inputMetadata;
  }

  public MetadataType getOutputMetadata() {
    return outputMetadata;
  }

  public MetadataType getOutputAttributesMetadata() {
    return outputAttributesMetadata;
  }

  public List<MetadataFailure> getFailures() {
    return failures;
  }

  @Override
  public MetadataResult<ComponentMetadataTypesDescriptor> toDescriptor() {
    return failures.isEmpty()
        ? success(new ComponentMetadataTypesDescriptor(inputMetadata, outputMetadata, outputAttributesMetadata))
        : failure(failures);
  }

}
