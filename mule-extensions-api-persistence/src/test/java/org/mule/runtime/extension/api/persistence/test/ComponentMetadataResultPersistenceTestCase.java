/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.persistence.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mule.runtime.api.metadata.resolving.FailureCode.CONNECTION_FAILURE;
import static org.mule.runtime.api.metadata.resolving.FailureCode.INVALID_METADATA_KEY;
import static org.mule.runtime.api.metadata.resolving.FailureCode.NO_DYNAMIC_METADATA_AVAILABLE;
import static org.mule.runtime.api.metadata.resolving.MetadataFailure.Builder.newFailure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.api.persistence.metadata.ComponentResultJsonSerializer;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ComponentMetadataResultPersistenceTestCase extends AbstractMetadataPersistenceTestCase {

  private static final String METADATA_RESULT_FAILURE_JSON = "/metadata/failure-result.json";
  private static final String METADATA_RESULT_FAILURE_NO_DESCRIPTOR_JSON = "/metadata/failure-no-descriptor-result.json";
  private static final String METADATA_WITHOUT_INPUT_FAILURE_JSON = "/metadata/failure-no-dynamic-metadata-available.json";
  private static final String METADATA_RESULT_ERROR_MESSAGE = "Metadata Failure Error";
  private static final String FIRST_ERROR_MESSAGE = "FIRST_ERROR";
  private static final String SECOND_ERROR_MESSAGE = "SECOND_ERROR";


  private ComponentResultJsonSerializer<ComponentModel> metadataDescriptorSerializer = new ComponentResultJsonSerializer<>(true);

  @Before
  public void setUp() throws IOException {
    super.setUp();
  }

  @Test
  public void serializeFailureMetadataResult() throws IOException {

    MetadataResult<ComponentMetadataDescriptor<ComponentModel>> failureResult =
        failure(ComponentMetadataDescriptor.builder(null).withAttributes(attributes).build(), newFailure()
            .withMessage(FIRST_ERROR_MESSAGE)
            .withFailureCode(CONNECTION_FAILURE)
            .withReason(METADATA_RESULT_ERROR_MESSAGE)
            .onComponent(),
                newFailure()
                    .withMessage(SECOND_ERROR_MESSAGE)
                    .withFailureCode(INVALID_METADATA_KEY)
                    .withReason(METADATA_RESULT_ERROR_MESSAGE)
                    .onComponent());

    String serialized = metadataDescriptorSerializer.serialize(failureResult);
    assertSerializedJson(serialized, METADATA_RESULT_FAILURE_JSON);
  }

  @Test
  public void serializeFailureMetadataResultNoDescriptor() throws IOException {

    MetadataResult<ComponentMetadataDescriptor<ComponentModel>> failureResult =
        failure(newFailure()
            .withMessage(FIRST_ERROR_MESSAGE)
            .withFailureCode(CONNECTION_FAILURE)
            .withReason(METADATA_RESULT_ERROR_MESSAGE)
            .onComponent(),
                newFailure()
                    .withMessage(SECOND_ERROR_MESSAGE)
                    .withFailureCode(INVALID_METADATA_KEY)
                    .withReason(METADATA_RESULT_ERROR_MESSAGE)
                    .onComponent());

    String serialized = metadataDescriptorSerializer.serialize(failureResult);
    assertSerializedJson(serialized, METADATA_RESULT_FAILURE_NO_DESCRIPTOR_JSON);
  }

  @Test
  public void deserializeNoDynamicMetadataAvailable() throws IOException {
    String resource = getResourceAsString(METADATA_WITHOUT_INPUT_FAILURE_JSON);
    MetadataResult<ComponentMetadataDescriptor<ComponentModel>> metadataResult =
        metadataDescriptorSerializer.deserialize(resource);
    assertThat(metadataResult.isSuccess(), is(false));
    assertThat(metadataResult.getFailures().isEmpty(), is(false));

    MetadataFailure metadataFailure = metadataResult.getFailures().get(0);
    assertThat(metadataFailure.getReason(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailure.getMessage(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailure.getFailureCode().getName(), is(NO_DYNAMIC_METADATA_AVAILABLE.getName()));
  }

  @Test
  public void deserializeFailureResult() throws IOException {
    String resource = getResourceAsString(METADATA_RESULT_FAILURE_JSON);
    MetadataResult<ComponentMetadataDescriptor<ComponentModel>> metadataResult =
        metadataDescriptorSerializer.deserialize(resource);

    assertThat(metadataResult.isSuccess(), is(false));
    assertThat(metadataResult.getFailures().isEmpty(), is(false));

    MetadataFailure metadataFailure = metadataResult.getFailures().get(0);
    assertThat(metadataFailure.getReason(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailure.getMessage(), is(FIRST_ERROR_MESSAGE));
    assertThat(metadataFailure.getFailureCode().getName(), is(CONNECTION_FAILURE.getName()));

    metadataFailure = metadataResult.getFailures().get(1);
    assertThat(metadataFailure.getMessage(), is(SECOND_ERROR_MESSAGE));
    assertThat(metadataFailure.getFailureCode().getName(), is(INVALID_METADATA_KEY.getName()));

  }
}
