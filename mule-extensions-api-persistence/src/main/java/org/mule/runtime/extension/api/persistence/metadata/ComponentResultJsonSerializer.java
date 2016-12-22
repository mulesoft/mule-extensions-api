/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableInputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableOutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ImmutableParameterMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.InputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.ParameterMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;

/**
 * Serializer that can convert a {@link MetadataResult} of a {@link ComponentMetadataDescriptor} type into a readable and
 * processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
public class ComponentResultJsonSerializer extends AbstractMetadataResultJsonSerializer {

  public ComponentResultJsonSerializer() {
    super(false);
  }

  public ComponentResultJsonSerializer(boolean prettyPrint) {
    super(prettyPrint);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String serialize(MetadataResult metadataResult) {
    return gson.toJson(new ComponentMetadataResult(metadataResult));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MetadataResult<ComponentMetadataDescriptor> deserialize(String metadataResult) {
    ComponentMetadataResult result = gson.fromJson(metadataResult, new TypeToken<ComponentMetadataResult>() {

    }.getType());
    return result.toDescriptor();
  }

  /**
   * Serializer that can convert a {@link MetadataResult} of a {@link ComponentMetadataDescriptor} type into a readable and
   * processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
   *
   * @since 1.0
   */
  private static class ComponentMetadataResult implements Descriptable<MetadataResult<ComponentMetadataDescriptor>> {

    private final String componentName;
    private final List<ParameterMetadata> input;
    private final OutputMetadata output;
    private final List<MetadataFailure> failures;

    ComponentMetadataResult(MetadataResult<ComponentMetadataDescriptor> result) {
      ComponentMetadataDescriptor descriptor = result.get();
      this.componentName = descriptor != null ? descriptor.getName() : "";
      this.output = descriptor != null ? new OutputMetadata(descriptor.getOutputMetadata()) : null;
      this.input = descriptor != null ? new InputMetadata(descriptor.getInputMetadata()).getParameters() : null;
      this.failures = result.getFailures();
    }

    @Override
    public MetadataResult<ComponentMetadataDescriptor> toDescriptor() {
      InputMetadataDescriptor inputDesc = input != null ? new InputMetadata(input).toDescriptor() : null;
      OutputMetadataDescriptor outputDesc = output != null ? output.toDescriptor() : null;
      ComponentMetadataDescriptor descriptor = new ImmutableComponentMetadataDescriptor(componentName, inputDesc, outputDesc);
      return failures.isEmpty() ? success(descriptor) : failure(descriptor, failures);
    }
  }


  /**
   * DTO that represents a {@link ParameterMetadataDescriptor} into a serializable format.
   *
   * @since 1.0
   */
  private static class ParameterMetadata implements Descriptable<ParameterMetadataDescriptor> {

    private final String name;
    private final MetadataType type;
    private final boolean isDynamic;

    ParameterMetadata(String name, MetadataType type, boolean isDynamic) {
      this.isDynamic = isDynamic;
      this.type = type;
      this.name = name;
    }

    String getName() {
      return name;
    }

    MetadataType getType() {
      return type;
    }

    boolean isDynamic() {
      return isDynamic;
    }

    @Override
    public ParameterMetadataDescriptor toDescriptor() {
      return new ImmutableParameterMetadataDescriptor(name, type, isDynamic);
    }
  }


  /**
   * DTO that represents a {@link OutputMetadataDescriptor} into a serializable format.
   *
   * @since 1.0
   */
  private static class OutputMetadata implements Descriptable<OutputMetadataDescriptor> {

    private final TypeMetadata content;
    private final TypeMetadata attributes;

    OutputMetadata(OutputMetadataDescriptor output) {
      TypeMetadataDescriptor payload = output.getPayloadMetadata();
      TypeMetadataDescriptor attributes = output.getAttributesMetadata();
      this.content = new TypeMetadata(payload.getType(), payload.isDynamic());
      this.attributes = new TypeMetadata(attributes.getType(), attributes.isDynamic());
    }

    TypeMetadata getContent() {
      return content;
    }

    TypeMetadata getAttributes() {
      return attributes;
    }

    public OutputMetadataDescriptor toDescriptor() {
      return new ImmutableOutputMetadataDescriptor(content.toDescriptor(), attributes.toDescriptor());
    }
  }


  /**
   * DTO that represents an {@link InputMetadataDescriptor} into a serializable format.
   *
   * @since 1.0
   */
  private static class InputMetadata implements Descriptable<InputMetadataDescriptor> {

    private final List<ParameterMetadata> parameters;

    InputMetadata(InputMetadataDescriptor input) {
      if (input == null) {
        parameters = emptyList();
      } else {
        parameters = copyOf(input.getAllParameters().values().stream()
            .filter(Objects::nonNull)
            .map(p -> new ParameterMetadata(p.getName(), p.getType(), p.isDynamic()))
            .collect(toList()));
      }
    }

    InputMetadata(List<ParameterMetadata> input) {
      parameters = input != null ? input : emptyList();
    }

    public List<ParameterMetadata> getParameters() {
      return parameters;
    }

    @Override
    public InputMetadataDescriptor toDescriptor() {
      return new ImmutableInputMetadataDescriptor(parameters.stream().collect(toMap(ParameterMetadata::getName,
                                                                                    ParameterMetadata::toDescriptor)));
    }
  }
}
