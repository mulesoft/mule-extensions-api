/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mule.runtime.api.metadata.MetadataKeyBuilder.newKey;
import static org.mule.runtime.api.metadata.resolving.FailureCode.CONNECTION_FAILURE;
import static org.mule.runtime.api.metadata.resolving.FailureCode.INVALID_METADATA_KEY;
import static org.mule.runtime.api.metadata.resolving.FailureCode.NOT_AUTHORIZED;
import static org.mule.runtime.api.metadata.resolving.FailureCode.NO_DYNAMIC_METADATA_AVAILABLE;
import static org.mule.runtime.api.metadata.resolving.MetadataFailure.Builder.newFailure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataKeysContainer;
import org.mule.runtime.api.metadata.MetadataKeysContainerBuilder;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableInputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableOutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableParameterMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.InputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ParameterMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.persistence.metadata.ComponentResultJsonSerializer;
import org.mule.runtime.extension.api.persistence.metadata.EntityMetadataResultJsonSerializer;
import org.mule.runtime.extension.api.persistence.metadata.MetadataKeysResultJsonSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MetadataResultPersistenceTestCase extends BasePersistenceTestCase {

  private static final String METADATA_DESCRIPTOR_RESULT_JSON = "metadata/success-result-descriptor.json";
  private static final String METADATA_ENTITY_RESULT_JSON = "metadata/success-result-entity.json";
  private static final String METADATA_KEYS_RESULT_JSON = "metadata/success-result-keys.json";
  private static final String METADATA_MULTILEVEL_KEYS_RESULT_JSON = "metadata/success-result-multilevel-keys.json";
  private static final String METADATA_RESULT_FAILURE_JSON = "metadata/failure-result.json";
  private static final String METADATA_KEYS_RESULT_FAILURE_JSON = "metadata/failure-keys-result.json";
  private static final String METADATA_ENTITY_RESULT_FAILURE_JSON = "metadata/failure-entity-result.json";
  private static final String METADATA_WITHOUT_INPUT_FAILURE_JSON = "metadata/failure-no-dynamic-metadata-available.json";

  private static final String FIRST_KEY_ID = "firstKey";
  private static final String SECOND_KEY_ID = "secondKey";
  private static final String METADATA_RESULT_ERROR_MESSAGE = "Metadata Failure Error";
  private static final String FIRST_CHILD = "firstChild";
  private static final String SECOND_CHILD = "secondChild";
  private static final String FIRST_ERROR_MESSAGE = "FIRST_ERROR";
  private static final String SECOND_ERROR_MESSAGE = "SECOND_ERROR";
  private static final String METADATA_CONTENT_FAILURE = "Metadata Content Failure Error";
  private static final String CATEGORY_NAME = "categoryName";

  private ComponentMetadataDescriptor operationMetadataDescriptor;
  private TypeMetadataDescriptor typeMetadataDescriptor;
  private MetadataKeysResultJsonSerializer keysResultSerializer = new MetadataKeysResultJsonSerializer(true);
  private ComponentResultJsonSerializer metadataDescriptorSerializer = new ComponentResultJsonSerializer(true);
  private final EntityMetadataResultJsonSerializer typeDescriptorResultJsonSerializer =
      new EntityMetadataResultJsonSerializer(true);
  private MetadataKeysContainerBuilder builder;

  @Before
  public void setup() {
    operationMetadataDescriptor = buildTestOperationMetadataDescriptor();
    typeMetadataDescriptor = buildTestTypeMetadataDescriptor();
    builder = MetadataKeysContainerBuilder.getInstance();
  }

  @Test
  public void serializeSuccessEntityMetadataDescriptorResult() throws IOException {
    MetadataResult<ImmutableTypeMetadataDescriptor> success = success((ImmutableTypeMetadataDescriptor) typeMetadataDescriptor);
    String serialized = typeDescriptorResultJsonSerializer.serialize(success);
    assertSerializedJson(serialized, METADATA_ENTITY_RESULT_JSON);
  }

  @Test
  public void serializeSuccessMetadataDescriptorResult() throws IOException {
    MetadataResult<?> success = success((ImmutableComponentMetadataDescriptor) operationMetadataDescriptor);
    String serialized = metadataDescriptorSerializer.serialize(success);
    assertSerializedJson(serialized, METADATA_DESCRIPTOR_RESULT_JSON);
  }

  @Test
  public void serializeSuccessMetadataKeysResult() throws IOException {
    Set<MetadataKey> keys = new LinkedHashSet<>();
    keys.add(newKey(FIRST_KEY_ID).build());
    keys.add(newKey(SECOND_KEY_ID).build());
    MetadataResult<MetadataKeysContainer> successResult = success(builder.add(CATEGORY_NAME, keys).build());
    String serialized = keysResultSerializer.serialize(successResult);
    assertSerializedJson(serialized, METADATA_KEYS_RESULT_JSON);
  }

  @Test
  public void serializeNullPayloadMetadataKeysResult() throws IOException {
    MetadataResult<Object> failure = failure(newFailure()
        .withMessage(METADATA_RESULT_ERROR_MESSAGE)
        .withFailureCode(NOT_AUTHORIZED)
        .withReason(METADATA_RESULT_ERROR_MESSAGE)
        .onKeys());
    String serialized = keysResultSerializer.serialize(failure);
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
    MetadataResult<MetadataKeysContainer> failureResult = failure(builder.build(), newFailure()
        .withMessage(METADATA_RESULT_ERROR_MESSAGE)
        .withFailureCode(NOT_AUTHORIZED)
        .withReason(METADATA_RESULT_ERROR_MESSAGE)
        .onKeys());
    String serialized = keysResultSerializer.serialize(failureResult);
    assertSerializedJson(serialized, METADATA_KEYS_RESULT_FAILURE_JSON);
  }

  @Test
  public void serializeFailureMetadataResult() throws IOException {
    final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
    final MetadataType stringType = typeLoader.load(String.class);
    final MetadataType intType = typeLoader.load(Integer.class);
    final MetadataType objectType = typeLoader.load(Object.class);
    ParameterMetadataDescriptor param1 = new ImmutableParameterMetadataDescriptor("stringParam1", stringType, true);
    ParameterMetadataDescriptor content = new ImmutableParameterMetadataDescriptor("content", objectType, false);

    Map<String, ParameterMetadataDescriptor> parameters = new HashMap<>();
    parameters.put("stringParam1", param1);
    parameters.put("content", content);

    ImmutableTypeMetadataDescriptor intDescriptor = new ImmutableTypeMetadataDescriptor(intType, true);
    ImmutableTypeMetadataDescriptor stringDescriptor = new ImmutableTypeMetadataDescriptor(stringType, false);
    OutputMetadataDescriptor output = new ImmutableOutputMetadataDescriptor(intDescriptor, stringDescriptor);

    ImmutableInputMetadataDescriptor input = new ImmutableInputMetadataDescriptor(parameters);
    ImmutableComponentMetadataDescriptor descriptor =
        new ImmutableComponentMetadataDescriptor("testOperationMetadataDescriptor", input, output);

    MetadataResult<Object> failureResult = failure(descriptor, newFailure()
        .withMessage(FIRST_ERROR_MESSAGE)
        .withFailureCode(CONNECTION_FAILURE)
        .withReason(METADATA_RESULT_ERROR_MESSAGE)
        .onComponent(), newFailure()
            .withMessage(SECOND_ERROR_MESSAGE)
            .withFailureCode(INVALID_METADATA_KEY)
            .withReason(METADATA_RESULT_ERROR_MESSAGE)
            .onComponent());

    String serialized = metadataDescriptorSerializer.serialize(failureResult);
    assertSerializedJson(serialized, METADATA_RESULT_FAILURE_JSON);
  }

  @Test
  public void serializeFailureEntityMetadataResult() throws IOException {
    MetadataResult<?> failureResult = failure(newFailure()
        .withMessage(METADATA_RESULT_ERROR_MESSAGE)
        .withFailureCode(CONNECTION_FAILURE)
        .withReason(METADATA_RESULT_ERROR_MESSAGE)
        .onComponent());
    String serialized = typeDescriptorResultJsonSerializer.serialize(failureResult);
    assertSerializedJson(serialized, METADATA_ENTITY_RESULT_FAILURE_JSON);
  }

  @Test
  public void deserializeMetadataKeysResult() throws IOException {
    String resource = getResourceAsString(METADATA_KEYS_RESULT_JSON);
    MetadataResult<MetadataKeysContainer> metadataResult = keysResultSerializer.deserialize(resource);

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
    MetadataResult<ComponentMetadataDescriptor> metadataResult =
        metadataDescriptorSerializer.deserialize(resource);

    assertThat(metadataResult.isSuccess(), is(true));
    assertThat(metadataResult.get().getName(), is(operationMetadataDescriptor.getName()));
    assertThat(metadataResult.get().getInputMetadata().getAllParameters().values(), hasSize(4));
    assertOutputMetadata(metadataResult);
    assertContentMetadata(metadataResult);
  }

  @Test
  public void deserializeFailureKeysResult() throws IOException {
    String resource = getResourceAsString(METADATA_KEYS_RESULT_FAILURE_JSON);
    MetadataResult<MetadataKeysContainer> metadataResult = keysResultSerializer.deserialize(resource);
    assertThat(metadataResult.isSuccess(), is(false));
    assertThat(metadataResult.getFailures().isEmpty(), is(false));

    MetadataFailure metadataFailure = metadataResult.getFailures().get(0);
    assertThat(metadataFailure.getReason(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailure.getMessage(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailure.getFailureCode().getName(), is(NOT_AUTHORIZED.getName()));
  }

  @Test
  public void deserializeNoDynamicMetadataAvailable() throws IOException {
    String resource = getResourceAsString(METADATA_WITHOUT_INPUT_FAILURE_JSON);
    MetadataResult<ComponentMetadataDescriptor> metadataResult = metadataDescriptorSerializer.deserialize(resource);
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
    MetadataResult<ComponentMetadataDescriptor> metadataResult = metadataDescriptorSerializer.deserialize(resource);

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

  private ComponentMetadataDescriptor buildTestOperationMetadataDescriptor() {
    final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
    final MetadataType stringType = typeLoader.load(String.class);
    final MetadataType intType = typeLoader.load(Integer.class);

    Map<String, ParameterMetadataDescriptor> parameters = new HashMap<>();
    parameters.put("stringParam1", new ImmutableParameterMetadataDescriptor("stringParam1", stringType, false));
    parameters.put("stringParam2", new ImmutableParameterMetadataDescriptor("stringParam2", stringType, false));
    parameters.put("intParam1", new ImmutableParameterMetadataDescriptor("intParam1", intType, false));
    parameters.put("content", new ImmutableParameterMetadataDescriptor("content", typeLoader.load(Object.class), true));

    OutputMetadataDescriptor output = new ImmutableOutputMetadataDescriptor(new ImmutableTypeMetadataDescriptor(stringType, true),
                                                                            new ImmutableTypeMetadataDescriptor(stringType,
                                                                                                                false));

    InputMetadataDescriptor input = new ImmutableInputMetadataDescriptor(parameters);

    return new ImmutableComponentMetadataDescriptor("testOperationMetadataDescriptor", input, output);
  }

  private TypeMetadataDescriptor buildTestTypeMetadataDescriptor() {
    final ClassTypeLoader javaTypeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
    return new ImmutableTypeMetadataDescriptor(javaTypeLoader.load(String.class), false);
  }

  private void assertOutputMetadata(MetadataResult<ComponentMetadataDescriptor> metadataResult) {
    OutputMetadataDescriptor outputMetadata = metadataResult.get().getOutputMetadata();
    OutputMetadataDescriptor expectedOutputMetadata = operationMetadataDescriptor.getOutputMetadata();
    assertThat(outputMetadata.getAttributesMetadata().getType(),
               is(expectedOutputMetadata.getAttributesMetadata().getType()));
    assertThat(outputMetadata.getPayloadMetadata().getType(),
               is(expectedOutputMetadata.getPayloadMetadata().getType()));
  }

  private void assertContentMetadata(MetadataResult<ComponentMetadataDescriptor> metadataResult) {
    InputMetadataDescriptor contentMetadata = metadataResult.get().getInputMetadata();
    ParameterMetadataDescriptor expectedContentMetadata =
        operationMetadataDescriptor.getInputMetadata().getParameterMetadata("content");
    assertThat(contentMetadata.getParameterMetadata("content").getType(), is(expectedContentMetadata.getType()));
  }
}
