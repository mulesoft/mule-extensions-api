/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.api.persistence.metadata.ComponentResultJsonSerializer;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class SourceMetadataResultPersistenceTestCase extends AbstractMetadataPersistenceTestCase {

  private static final String METADATA_SOURCE_RESULT_JSON = "metadata/success-result-source-descriptor.json";

  private ComponentMetadataDescriptor<SourceModel> sourceMetadataDescriptor;
  private ComponentResultJsonSerializer<SourceModel> metadataDescriptorSerializer = new ComponentResultJsonSerializer<>(true);

  @Before
  public void setUp() {
    super.setUp();
    sourceMetadataDescriptor = buildTestSourceMetadataDescriptor();
  }

  @Test
  public void serializeSuccessMetadataDescriptorResult() throws IOException {
    MetadataResult<ComponentMetadataDescriptor<SourceModel>> success = success(sourceMetadataDescriptor);
    String serialized = metadataDescriptorSerializer.serialize(success);
    assertSerializedJson(serialized, METADATA_SOURCE_RESULT_JSON);
  }

  @Test
  public void deserializeSourceMetadataDescriptorResult() throws IOException {
    String resource = getResourceAsString(METADATA_SOURCE_RESULT_JSON);
    MetadataResult<ComponentMetadataDescriptor<SourceModel>> metadataResult =
        metadataDescriptorSerializer.deserialize(resource);

    assertThat(metadataResult.isSuccess(), is(true));
    assertThat(metadataResult.get().getModel(), is(sourceModel));
    assertMetadataAttributes(metadataResult.get().getMetadataAttributes(), attributes);
  }

  private ComponentMetadataDescriptor<SourceModel> buildTestSourceMetadataDescriptor() {
    return ComponentMetadataDescriptor.builder(sourceModel).withAttributes(attributes).build();
  }
}
