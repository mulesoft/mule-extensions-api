/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.test;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.api.persistence.metadata.ComponentResultJsonSerializer;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class OperationMetadataResultPersistenceTestCase extends AbstractMetadataPersistenceTestCase {

  private static final String METADATA_OPERATION_RESULT_JSON = "/metadata/success-result-operation-descriptor.json";

  private ComponentMetadataDescriptor<OperationModel> operationMetadataDescriptor;
  private final ComponentResultJsonSerializer<OperationModel> metadataDescriptorSerializer =
      new ComponentResultJsonSerializer<>(true, true);

  @Override
  @Before
  public void setUp() throws IOException {
    super.setUp();
    operationMetadataDescriptor = buildTestOperationMetadataDescriptor();
  }

  @Test
  public void serializeSuccessMetadataDescriptorResult() throws IOException {
    MetadataResult<ComponentMetadataDescriptor<OperationModel>> success = success(operationMetadataDescriptor);
    String serialized = metadataDescriptorSerializer.serialize(success);
    assertSerializedJson(serialized, METADATA_OPERATION_RESULT_JSON);
  }

  @Test
  public void deserializeOperationMetadataDescriptorResult() throws IOException {
    String resource = getResourceAsString(METADATA_OPERATION_RESULT_JSON);
    MetadataResult<ComponentMetadataDescriptor<OperationModel>> metadataResult =
        metadataDescriptorSerializer.deserialize(resource);

    assertThat(metadataResult.isSuccess(), is(true));
    assertThat(metadataResult.get().getModel(), is(getCarOperation));
    assertMetadataAttributes(metadataResult.get().getMetadataAttributes(), attributes);
  }

  private ComponentMetadataDescriptor<OperationModel> buildTestOperationMetadataDescriptor() {
    return ComponentMetadataDescriptor.builder(getCarOperation).withAttributes(attributes).build();
  }
}
