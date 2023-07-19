/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.persistence.test;

import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.metadata.descriptor.ParameterMetadataDescriptor.builder;
import static org.mule.runtime.api.metadata.resolving.FailureCode.UNKNOWN;
import static org.mule.runtime.api.metadata.resolving.MetadataFailure.Builder.newFailure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualCompressingWhiteSpace.equalToCompressingWhiteSpace;
import static org.junit.Assert.assertThat;

import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.impl.DefaultStringType;
import org.mule.metadata.internal.utils.MetadataTypeWriter;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataTypesDescriptor;
import org.mule.runtime.api.metadata.descriptor.InputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.api.persistence.metadata.ComponentMetadataTypesDescriptorResultJsonSerializer;

import org.junit.Test;

import org.hamcrest.collection.IsCollectionWithSize;

public class ComponentMetadataTypesDescriptorResultJsonSerializerTestCase {

  private final ComponentMetadataTypesDescriptorResultJsonSerializer serializer =
      new ComponentMetadataTypesDescriptorResultJsonSerializer(false, true);

  @Test
  public void failureResult() {
    String expectedError = "Expected error message";
    MetadataResult<ComponentMetadataTypesDescriptor> error =
        failure(newFailure(new IllegalStateException(expectedError)).onComponent());

    MetadataResult<ComponentMetadataTypesDescriptor> deserialize = serializer.deserialize(serializer.serialize(error));

    assertThat(deserialize.isSuccess(), is(false));
    assertThat(deserialize.getFailures(), IsCollectionWithSize.hasSize(1));
    MetadataFailure failure = deserialize.getFailures().get(0);
    assertThat(failure.getMessage(), is(expectedError));
    assertThat(failure.getFailureCode(), is(UNKNOWN));
  }

  @Test
  public void successInputMetadata() {
    DefaultStringType paramAType = create(JAVA)
        .stringType()
        .defaultValue("defaultValue")
        .length(1)
        .build();

    ArrayType paramBType = create(JAVA)
        .arrayType()
        .of(create(JAVA).stringType())
        .build();

    InputMetadataDescriptor inputMetadataDescriptor = InputMetadataDescriptor.builder()
        .withParameter("paramA", builder("paramA").withType(paramAType).dynamic(true).build())
        .withParameter("paramB", builder("paramB").withType(paramBType).dynamic(true).build())
        .build();
    MetadataResult<ComponentMetadataTypesDescriptor> success =
        success(ComponentMetadataTypesDescriptor.builder().withInputMetadataDescriptor(inputMetadataDescriptor).build());

    MetadataResult<ComponentMetadataTypesDescriptor> deserialize = serializer.deserialize(serializer.serialize(success));

    assertThat(deserialize.isSuccess(), is(true));
    assertThat(deserialize.getFailures(), IsCollectionWithSize.hasSize(0));
    ComponentMetadataTypesDescriptor deserializedComponentMetadataTypes = deserialize.get();
    assertMetadataTypeEquals(deserializedComponentMetadataTypes.getInputMetadata().get("paramA"), paramAType);
    assertMetadataTypeEquals(deserializedComponentMetadataTypes.getInputMetadata().get("paramB"), paramBType);
  }

  @Test
  public void successOutput() {
    DefaultStringType outputAttributesType = create(JAVA)
        .stringType()
        .defaultValue("defaultValue")
        .length(1)
        .build();

    ArrayType outputType = create(JAVA)
        .arrayType()
        .of(create(JAVA).stringType())
        .build();

    OutputMetadataDescriptor outputMetadataResult = OutputMetadataDescriptor.builder()
        .withReturnType(TypeMetadataDescriptor.builder().withType(outputType).dynamic(true).build())
        .withAttributesType(TypeMetadataDescriptor.builder().withType(outputAttributesType).dynamic(true).build())
        .build();
    MetadataResult<ComponentMetadataTypesDescriptor> success =
        success(ComponentMetadataTypesDescriptor.builder().withOutputMetadataDescriptor(outputMetadataResult).build());

    MetadataResult<ComponentMetadataTypesDescriptor> deserialize = serializer.deserialize(serializer.serialize(success));

    assertThat(deserialize.isSuccess(), is(true));
    assertThat(deserialize.getFailures(), IsCollectionWithSize.hasSize(0));
    ComponentMetadataTypesDescriptor deserializedComponentMetadataTypes = deserialize.get();
    assertThat(deserializedComponentMetadataTypes.getInputMetadata().isEmpty(), is(true));

    assertThat(deserializedComponentMetadataTypes.getOutputAttributesMetadata().isPresent(), is(true));
    assertMetadataTypeEquals(deserializedComponentMetadataTypes.getOutputAttributesMetadata().get(), outputAttributesType);

    assertThat(deserializedComponentMetadataTypes.getOutputMetadata().isPresent(), is(true));
    assertMetadataTypeEquals(deserializedComponentMetadataTypes.getOutputAttributesMetadata().get(), outputAttributesType);
  }

  static void assertMetadataTypeEquals(MetadataType expected, MetadataType actual) {
    assertMetadataTypeEquals(toText(expected), actual);
  }

  static private void assertMetadataTypeEquals(String expected, MetadataType actual) {
    assertThat(expected, equalToCompressingWhiteSpace(toText(actual)));
  }

  static String toText(MetadataType metadataType) {
    return new MetadataTypeWriter().toString(metadataType);
  }

}
