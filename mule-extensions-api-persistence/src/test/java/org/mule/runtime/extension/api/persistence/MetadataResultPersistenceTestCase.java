/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import org.junit.Before;
import org.junit.Test;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.api.JavaTypeLoader;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataKeysContainer;
import org.mule.runtime.api.metadata.MetadataKeysContainerBuilder;
import org.mule.runtime.api.metadata.descriptor.*;
import org.mule.runtime.api.metadata.resolving.ImmutableMetadataResult;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.api.persistence.metadata.MetadataDescriptorResultJsonSerializer;
import org.mule.runtime.extension.api.persistence.metadata.MetadataKeysResultJsonSerializer;
import org.mule.runtime.extension.api.persistence.metadata.MetadataTypeDescriptorResultJsonSerializer;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mule.runtime.api.metadata.MetadataKeyBuilder.newKey;
import static org.mule.runtime.api.metadata.resolving.FailureCode.*;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;

public class MetadataResultPersistenceTestCase extends BasePersistenceTestCase {

  private static final String METADATA_DESCRIPTOR_RESULT_JSON = "metadata/success-result-descriptor.json";
  private static final String METADATA_ENTITY_RESULT_JSON = "metadata/success-result-entity.json";
  private static final String METADATA_KEYS_RESULT_JSON = "metadata/success-result-keys.json";
  private static final String METADATA_MULTILEVEL_KEYS_RESULT_JSON = "metadata/success-result-multilevel-keys.json";
  private static final String METADATA_RESULT_FAILURE_JSON = "metadata/failure-result.json";
  private static final String METADATA_KEYS_RESULT_FAILURE_JSON = "metadata/failure-keys-result.json";
  private static final String METADATA_ENTITY_RESULT_FAILURE_JSON = "metadata/failure-entity-result.json";

  private static final String FIRST_KEY_ID = "firstKey";
  private static final String SECOND_KEY_ID = "secondKey";
  private static final String METADATA_RESULT_ERROR_MESSAGE = "Metadata Failure Error";
  private static final String FIRST_CHILD = "firstChild";
  private static final String SECOND_CHILD = "secondChild";
  private static final String METADATA_CONTENT_FAILURE = "Metadata Content Failure Error";
  private static final String CATEGORY_NAME = "categoryName";

  private ComponentMetadataDescriptor operationMetadataDescriptor;
  private TypeMetadataDescriptor typeMetadataDescriptor;
  private MetadataKeysResultJsonSerializer keysResultSerializer = new MetadataKeysResultJsonSerializer(true);
  private MetadataDescriptorResultJsonSerializer metadataDescriptorSerializer = new MetadataDescriptorResultJsonSerializer(true);
  private final MetadataTypeDescriptorResultJsonSerializer typeDescriptorResultJsonSerializer =
      new MetadataTypeDescriptorResultJsonSerializer(true);
  private MetadataKeysContainerBuilder builder;

  @Before
  public void setup() {
    operationMetadataDescriptor = buildTestOperationMetadataDescriptor();
    typeMetadataDescriptor = buildTestTypeMetadataDescriptor();
    builder = MetadataKeysContainerBuilder.getInstance();
  }

  @Test
  public void serializeSuccessEntityMetadataDescriptorResult() throws IOException {
    String serialized =
        typeDescriptorResultJsonSerializer.serialize(success((ImmutableTypeMetadataDescriptor) typeMetadataDescriptor));
    assertSerializedJson(serialized, METADATA_ENTITY_RESULT_JSON);
  }

  @Test
  public void serializeSuccessMetadataDescriptorResult() throws IOException {
    String serialized =
        metadataDescriptorSerializer.serialize(success((ImmutableComponentMetadataDescriptor) operationMetadataDescriptor));
    assertSerializedJson(serialized, METADATA_DESCRIPTOR_RESULT_JSON);
  }

  @Test
  public void serializeSuccessMetadataKeysResult() throws IOException {
    Set<MetadataKey> keys = new LinkedHashSet<>();
    keys.add(newKey(FIRST_KEY_ID).build());
    keys.add(newKey(SECOND_KEY_ID).build());

    String serialized = keysResultSerializer.serialize(success(builder.add(CATEGORY_NAME, keys).build()));
    assertSerializedJson(serialized, METADATA_KEYS_RESULT_JSON);
  }

  @Test
  public void serializeNullPayloadMetadataKeysResult() throws IOException {
    String serialized = keysResultSerializer.serialize(failure(null, METADATA_RESULT_ERROR_MESSAGE,
                                                               NOT_AUTHORIZED, METADATA_RESULT_ERROR_MESSAGE));
    assertSerializedJson(serialized, METADATA_KEYS_RESULT_FAILURE_JSON);
  }

  @Test
  public void serializeSuccessMultilevelMetadataKeyResult() throws IOException {
    Set<MetadataKey> keys = new LinkedHashSet<>();
    keys.add(newKey(FIRST_KEY_ID).withChild(newKey(FIRST_CHILD)).withChild(newKey(SECOND_CHILD)).build());
    keys.add(newKey(SECOND_KEY_ID).build());

    String serialized = keysResultSerializer.serialize(success(builder.add(CATEGORY_NAME, keys).build()));
    assertSerializedJson(serialized, METADATA_MULTILEVEL_KEYS_RESULT_JSON);
  }

  @Test
  public void serializeFailureMultilevelMetadataKeyResult() throws IOException {
    String serialized = keysResultSerializer.serialize(failure(builder.build(), METADATA_RESULT_ERROR_MESSAGE,
                                                               NOT_AUTHORIZED, METADATA_RESULT_ERROR_MESSAGE));
    assertSerializedJson(serialized, METADATA_KEYS_RESULT_FAILURE_JSON);
  }

  @Test
  public void serializeFailureMetadataResult() throws IOException {
    final JavaTypeLoader javaTypeLoader = new JavaTypeLoader(ExtensionModelPersistenceTestCase.class.getClassLoader());
    final MetadataType stringType = javaTypeLoader.load(String.class);
    final MetadataType intType = javaTypeLoader.load(Integer.class);
    List<MetadataResult<ParameterMetadataDescriptor>> parameters = new ArrayList<>();
    parameters.add(success(new ImmutableParameterMetadataDescriptor("stringParam1", stringType)));
    MetadataResult<OutputMetadataDescriptor> outputMetadataDescriptor =
        success(new ImmutableOutputMetadataDescriptor(success(new ImmutableTypeMetadataDescriptor(intType)),
                                                      success(new ImmutableTypeMetadataDescriptor(stringType))));
    MetadataResult<ParameterMetadataDescriptor> content = failure(
                                                                  new ImmutableParameterMetadataDescriptor("content",
                                                                                                           javaTypeLoader
                                                                                                               .load(Object.class)),
                                                                  METADATA_CONTENT_FAILURE, INVALID_METADATA_KEY, "");

    ImmutableComponentMetadataDescriptor metadataDescriptor =
        new ImmutableComponentMetadataDescriptor("testOperationMetadataDescriptor", parameters, outputMetadataDescriptor,
                                                 content);

    MetadataResult metadataResultFailure =
        failure(metadataDescriptor, METADATA_RESULT_ERROR_MESSAGE, CONNECTION_FAILURE, METADATA_RESULT_ERROR_MESSAGE);
    String serialized = metadataDescriptorSerializer.serialize(metadataResultFailure);
    assertSerializedJson(serialized, METADATA_RESULT_FAILURE_JSON);
  }

  @Test
  public void serializeFailureEntityMetadataResult() throws IOException {
    String serialized =
        typeDescriptorResultJsonSerializer
            .serialize(failure(null, METADATA_RESULT_ERROR_MESSAGE, CONNECTION_FAILURE, METADATA_RESULT_ERROR_MESSAGE));
    assertSerializedJson(serialized, METADATA_ENTITY_RESULT_FAILURE_JSON);
  }


  @Test
  public void deserializeMetadataKeysResult() throws IOException {
    String resource = getResourceAsString(METADATA_KEYS_RESULT_JSON);
    ImmutableMetadataResult<MetadataKeysContainer> metadataResult = keysResultSerializer.deserialize(resource);

    assertThat(metadataResult.isSuccess(), is(true));
    MetadataKeysContainer container = metadataResult.get();
    assertThat(container.getKeys(CATEGORY_NAME).isPresent(), is(true));
    Iterator<MetadataKey> iterator = container.getKeys(CATEGORY_NAME).get().iterator();
    assertThat(iterator.next().getDisplayName(), is(FIRST_KEY_ID));
    assertThat(iterator.next().getDisplayName(), is(SECOND_KEY_ID));
  }

  @Test
  public void deserializeOperationMetadataDescriptorResult() throws IOException {
    String resource = getResourceAsString(METADATA_DESCRIPTOR_RESULT_JSON);
    ImmutableMetadataResult<ImmutableComponentMetadataDescriptor> metadataResult =
        metadataDescriptorSerializer.deserialize(resource);

    assertThat(metadataResult.isSuccess(), is(true));
    assertThat(metadataResult.get().getName(), is(operationMetadataDescriptor.getName()));
    assertThat(metadataResult.get().getParametersMetadata(), hasSize(3));
    assertOutputMetadata(metadataResult);
    assertContentMetadata(metadataResult);
  }

  @Test
  public void deserializeFailureKeysResult() throws IOException {
    String resource = getResourceAsString(METADATA_KEYS_RESULT_FAILURE_JSON);
    ImmutableMetadataResult<MetadataKeysContainer> metadataResult = keysResultSerializer.deserialize(resource);
    assertThat(metadataResult.isSuccess(), is(false));
    assertThat(metadataResult.getFailure().isPresent(), is(true));

    Optional<MetadataFailure> metadataFailures = metadataResult.getFailure();
    assertThat(metadataFailures.get().getReason(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailures.get().getMessage(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailures.get().getFailureCode().getName(), is(NOT_AUTHORIZED.getName()));
  }

  @Test
  public void deserializeFailureResult() throws IOException {
    String resource = getResourceAsString(METADATA_RESULT_FAILURE_JSON);
    ImmutableMetadataResult<ImmutableComponentMetadataDescriptor> metadataResult =
        metadataDescriptorSerializer.deserialize(resource);

    assertThat(metadataResult.isSuccess(), is(false));
    assertThat(metadataResult.getFailure().isPresent(), is(true));

    Optional<MetadataFailure> metadataFailures = metadataResult.getFailure();
    assertThat(metadataFailures.get().getReason(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailures.get().getMessage(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailures.get().getFailureCode().getName(), is(CONNECTION_FAILURE.getName()));

    MetadataResult<ParameterMetadataDescriptor> contentMetadata = metadataResult.get().getContentMetadata().get();
    assertThat(contentMetadata.isSuccess(), is(false));
    assertThat(contentMetadata.getFailure().get().getMessage(), is(METADATA_CONTENT_FAILURE));
    assertThat(contentMetadata.getFailure().get().getFailureCode().getName(), is(INVALID_METADATA_KEY.getName()));

    assertThat(metadataResult.get().getOutputMetadata().isSuccess(), is(true));
  }

  private ComponentMetadataDescriptor buildTestOperationMetadataDescriptor() {
    final JavaTypeLoader javaTypeLoader = new JavaTypeLoader(ExtensionModelPersistenceTestCase.class.getClassLoader());
    final MetadataType stringType = javaTypeLoader.load(String.class);
    final MetadataType intType = javaTypeLoader.load(Integer.class);

    List<MetadataResult<ParameterMetadataDescriptor>> parameters = new ArrayList<>();
    parameters.add(success(new ImmutableParameterMetadataDescriptor("stringParam1", stringType)));
    parameters.add(success(new ImmutableParameterMetadataDescriptor("stringParam2", stringType)));
    parameters.add(success(new ImmutableParameterMetadataDescriptor("intParam1", intType)));

    MetadataResult<OutputMetadataDescriptor> outputMetadataDescriptor =
        success(new ImmutableOutputMetadataDescriptor(success(new ImmutableTypeMetadataDescriptor(stringType)),
                                                      success(new ImmutableTypeMetadataDescriptor(stringType))));
    MetadataResult<ParameterMetadataDescriptor> content =
        success(new ImmutableParameterMetadataDescriptor("content", javaTypeLoader.load(Object.class)));

    return new ImmutableComponentMetadataDescriptor("testOperationMetadataDescriptor", parameters, outputMetadataDescriptor,
                                                    content);
  }

  private TypeMetadataDescriptor buildTestTypeMetadataDescriptor() {
    final JavaTypeLoader javaTypeLoader = new JavaTypeLoader(ExtensionModelPersistenceTestCase.class.getClassLoader());
    return new ImmutableTypeMetadataDescriptor(javaTypeLoader.load(String.class));
  }

  private void assertOutputMetadata(MetadataResult<ImmutableComponentMetadataDescriptor> metadataResult) {
    MetadataResult<OutputMetadataDescriptor> outputMetadata = metadataResult.get().getOutputMetadata();
    MetadataResult<OutputMetadataDescriptor> expectedOutputMetadata = operationMetadataDescriptor.getOutputMetadata();
    assertThat(outputMetadata.get().getAttributesMetadata().get().getType(),
               is(expectedOutputMetadata.get().getAttributesMetadata().get().getType()));
    assertThat(outputMetadata.get().getPayloadMetadata().get().getType(),
               is(expectedOutputMetadata.get().getPayloadMetadata().get().getType()));
  }

  private void assertContentMetadata(MetadataResult<ImmutableComponentMetadataDescriptor> metadataResult) {
    MetadataResult<ParameterMetadataDescriptor> contentMetadata = metadataResult.get().getContentMetadata().get();
    MetadataResult<ParameterMetadataDescriptor> expectedContentMetadata = operationMetadataDescriptor.getContentMetadata().get();
    assertThat(contentMetadata.get().getType(), is(expectedContentMetadata.get().getType()));
  }
}
