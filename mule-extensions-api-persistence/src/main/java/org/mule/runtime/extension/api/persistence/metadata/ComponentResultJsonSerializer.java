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
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Serializer that can convert a {@link MetadataResult} of a {@link ComponentMetadataDescriptor} type into a readable and
 * processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
public class ComponentResultJsonSerializer extends AbstractMetadataResultJsonSerializer {

  private final static String COMPONENT = "COMPONENT";
  private final static String OUTPUT_PAYLOAD = "OUTPUT_PAYLOAD";
  private final static String OUTPUT_ATTRIBUTES = "OUTPUT_ATTRIBUTES";
  private final static String OUTPUT = "OUTPUT";
  private final static String INPUT = "INPUT";

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
    return result.toComponentMetadataResult();
  }

  /**
   * Serializer that can convert a {@link MetadataResult} of a {@link ComponentMetadataDescriptor} type into a readable and
   * processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
   *
   * @since 1.0
   */
  private static class ComponentMetadataResult implements Descriptable<ComponentMetadataDescriptor> {

    private final String componentName;
    private final List<ParameterMetadata> input;
    private final OutputMetadata output;
    private final List<Failure> failures;

    ComponentMetadataResult(MetadataResult<ComponentMetadataDescriptor> result) {
      this.componentName = result.get() != null ? result.get().getName() : "";
      this.output = result.get() != null ? new OutputMetadata(result.get()) : null;
      this.input = result.get() != null ? new InputMetadata(result.get()).getParameters() : null;
      this.failures = collectFailures(result);
    }


    public MetadataResult<ComponentMetadataDescriptor> toComponentMetadataResult() {
      return toDescriptorResult(failures);
    }

    private List<Failure> collectFailures(MetadataResult<ComponentMetadataDescriptor> result) {
      if (!result.getFailure().isPresent()) {
        return emptyList();
      }

      ImmutableMap.Builder<String, MetadataFailure> failures = ImmutableMap.builder();
      ComponentMetadataDescriptor descriptor = result.get();
      failures.put(COMPONENT, result.getFailure().get());

      if (descriptor != null) {
        MetadataResult<InputMetadataDescriptor> inputMetadata = descriptor.getInputMetadata();
        inputMetadata.getFailure().ifPresent(failure -> failures.put(INPUT, failure));

        MetadataResult<OutputMetadataDescriptor> outputMetadata = descriptor.getOutputMetadata();
        outputMetadata.getFailure()
            .ifPresent(outputFailure -> {
              outputMetadata.get().getPayloadMetadata().getFailure()
                  .ifPresent(failure -> failures.put(OUTPUT_PAYLOAD, failure));

              outputMetadata.get().getAttributesMetadata().getFailure()
                  .ifPresent(failure -> failures.put(OUTPUT_ATTRIBUTES, failure));

              failures.put(OUTPUT, outputFailure);
            });
      }

      return failures.build()
          .entrySet()
          .stream()
          .map(e -> new Failure(e.getValue(), e.getKey()))
          .collect(toList());
    }

    @Override
    public MetadataResult<ComponentMetadataDescriptor> toDescriptorResult(List<Failure> failures) {
      Optional<Failure> metadataFailure = getComponentFailure(failures, COMPONENT);

      ImmutableComponentMetadataDescriptor descriptor =
          new ImmutableComponentMetadataDescriptor(componentName,
                                                   new InputMetadata(input).toDescriptorResult(failures),
                                                   output.toDescriptorResult(failures));
      if (metadataFailure.isPresent()) {
        return failure(descriptor,
                       metadataFailure.get().getMessage(),
                       metadataFailure.get().getFailureCode(),
                       metadataFailure.get().getReason());
      }

      return success(descriptor);
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
    public MetadataResult<ParameterMetadataDescriptor> toDescriptorResult(List<Failure> failures) {
      Optional<Failure> metadataFailure = getComponentFailure(failures, name);
      ImmutableParameterMetadataDescriptor descriptor = new ImmutableParameterMetadataDescriptor(name, type, isDynamic);
      if (metadataFailure.isPresent()) {
        return failure(descriptor,
                       metadataFailure.get().getMessage(),
                       metadataFailure.get().getFailureCode(),
                       metadataFailure.get().getReason());
      }
      return success(descriptor);
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

    OutputMetadata(ComponentMetadataDescriptor result) {
      OutputMetadataDescriptor outputDescriptor = result.getOutputMetadata().get();
      this.content = new TypeMetadata(outputDescriptor.getPayloadMetadata().get().getType(),
                                      outputDescriptor.getPayloadMetadata().get().isDynamic());
      this.attributes = new TypeMetadata(outputDescriptor.getAttributesMetadata().get().getType(), false);
    }

    TypeMetadata getContent() {
      return content;
    }

    TypeMetadata getAttributes() {
      return attributes;
    }

    @Override
    public MetadataResult<OutputMetadataDescriptor> toDescriptorResult(List<Failure> failures) {
      Optional<Failure> metadataFailure = getComponentFailure(failures, OUTPUT);
      Optional<Failure> attributesFailure = getComponentFailure(failures, OUTPUT_ATTRIBUTES);
      Optional<Failure> payloadFailure = getComponentFailure(failures, OUTPUT_PAYLOAD);

      ImmutableOutputMetadataDescriptor descriptor =
          new ImmutableOutputMetadataDescriptor(content.toDescriptorResult(payloadFailure),
                                                attributes.toDescriptorResult(attributesFailure));
      if (metadataFailure.isPresent()) {
        return failure(descriptor,
                       metadataFailure.get().getMessage(),
                       metadataFailure.get().getFailureCode(),
                       metadataFailure.get().getReason());
      }
      return success(descriptor);
    }
  }


  /**
   * DTO that represents an {@link InputMetadataDescriptor} into a serializable format.
   *
   * @since 1.0
   */
  private static class InputMetadata implements Descriptable<InputMetadataDescriptor> {

    private final List<ParameterMetadata> parameters;

    InputMetadata(ComponentMetadataDescriptor result) {
      InputMetadataDescriptor intputDescriptor = result.getInputMetadata().get();

      if (intputDescriptor == null) {
        parameters = ImmutableList.of();
      } else {
        parameters = copyOf(intputDescriptor.getAllParameters().values().stream()
            .filter(p -> p.get() != null)
            .map(p -> new ParameterMetadata(p.get().getName(), p.get().getType(), p.get().isDynamic()))
            .collect(toList()));
      }
    }

    public InputMetadata(List<ParameterMetadata> input) {
      parameters = input;
    }

    @Override
    public MetadataResult<InputMetadataDescriptor> toDescriptorResult(List<Failure> failures) {
      Optional<Failure> metadataFailure = getComponentFailure(failures, INPUT);

      //TODO MULE-10707: update failure handling
      Map<String, MetadataResult<ParameterMetadataDescriptor>> input =
          parameters.stream().collect(toMap(ParameterMetadata::getName,
                                            p -> success(new ImmutableParameterMetadataDescriptor(p.getName(), p.getType(),
                                                                                                  p.isDynamic()))));

      ImmutableInputMetadataDescriptor descriptor = new ImmutableInputMetadataDescriptor(input);
      if (metadataFailure.isPresent()) {
        return failure(descriptor,
                       metadataFailure.get().getMessage(),
                       metadataFailure.get().getFailureCode(),
                       metadataFailure.get().getReason());
      }
      return success(descriptor);
    }

    public List<ParameterMetadata> getParameters() {
      return parameters;
    }
  }
}
