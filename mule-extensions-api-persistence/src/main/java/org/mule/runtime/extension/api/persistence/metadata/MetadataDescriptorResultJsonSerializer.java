/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.descriptor.*;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;

/**
 * Serializer that can convert a {@link MetadataResult} of a {@link ComponentMetadataDescriptor} type into a readable and
 * processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
public class MetadataDescriptorResultJsonSerializer extends AbstractMetadataResultJsonSerializer {

  private final static String CONTENT = "CONTENT";
  private final static String COMPONENT = "COMPONENT";
  private final static String OUTPUT_PAYLOAD = "OUTPUT_PAYLOAD";
  private final static String OUTPUT_ATTRIBUTES = "OUTPUT_ATTRIBUTES";
  private final static String OUTPUT = "OUTPUT";

  public MetadataDescriptorResultJsonSerializer() {
    super(false);
  }

  public MetadataDescriptorResultJsonSerializer(boolean prettyPrint) {
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
    ComponentMetadataResult result = gson.fromJson(metadataResult, new TypeToken<ComponentMetadataResult>() {}.getType());
    return result.toComponentMetadataResult();
  }

  /**
   * Serializer that can convert a {@link MetadataResult} of a {@link ComponentMetadataDescriptor} type into a readable and
   * processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
   *
   * @since 1.0
   */
  private class ComponentMetadataResult implements Descriptable<ComponentMetadataDescriptor> {


    private final String componentName;
    private final List<ParameterMetadata> parameters;
    private final OutputMetadata output;
    private final List<Failure> failures;

    ComponentMetadataResult(MetadataResult<ComponentMetadataDescriptor> result) {
      this.componentName = result.get() != null ? result.get().getName() : "";
      this.output = result.get() != null ? new OutputMetadata(result.get()) : null;
      this.parameters = result.get() != null ? getParametersMetadata(result.get()) : emptyList();
      this.failures = collectFailures(result);
    }

    private List<ParameterMetadata> getParametersMetadata(ComponentMetadataDescriptor componentResult) {
      ImmutableList.Builder<ParameterMetadata> parameterList = ImmutableList.builder();

      parameterList.addAll(componentResult.getParametersMetadata()
          .stream()
          .map(p -> new ParameterMetadata(p.get().getName(), p.get().getType(), false))
          .collect(toList()));

      Optional<MetadataResult<ParameterMetadataDescriptor>> contentMetadata = componentResult.getContentMetadata();
      if (contentMetadata.isPresent()) {
        ParameterMetadataDescriptor descriptor = contentMetadata.get().get();
        parameterList.add(new ParameterMetadata(descriptor.getName(), descriptor.getType(), true));
      }

      return parameterList.build();
    }

    public MetadataResult<ComponentMetadataDescriptor> toComponentMetadataResult() {
      return toDescriptorResult(failures);
    }

    private List<Failure> collectFailures(MetadataResult<ComponentMetadataDescriptor> result) {
      ImmutableMap.Builder<String, MetadataFailure> failures = ImmutableMap.builder();
      ComponentMetadataDescriptor descriptor = result.get();
      if (result.isSuccess()) {
        return emptyList();
      }

      failures.put(COMPONENT, result.getFailure().get());

      if (descriptor != null) {
        Optional<MetadataResult<ParameterMetadataDescriptor>> contentMetadata = descriptor.getContentMetadata();
        if (contentMetadata.isPresent() && !contentMetadata.get().isSuccess()) {
          failures.put(CONTENT, contentMetadata.get().getFailure().get());
        }

        MetadataResult<OutputMetadataDescriptor> outputMetadata = descriptor.getOutputMetadata();
        if (!outputMetadata.isSuccess()) {
          MetadataResult<TypeMetadataDescriptor> payloadMetadata = outputMetadata.get().getPayloadMetadata();
          if (!payloadMetadata.isSuccess()) {
            failures.put(OUTPUT_PAYLOAD, payloadMetadata.getFailure().get());
          }

          MetadataResult<TypeMetadataDescriptor> attributesMetadata = outputMetadata.get().getAttributesMetadata();
          if (!attributesMetadata.isSuccess()) {
            failures.put(OUTPUT_ATTRIBUTES, attributesMetadata.getFailure().get());
          }

          failures.put(OUTPUT, payloadMetadata.getFailure().get());
        }
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
      Optional<ParameterMetadata> content = parameters.stream().filter(ParameterMetadata::isDynamic).findFirst();

      List<MetadataResult<ParameterMetadataDescriptor>> parameterDescriptors = parameters.stream()
          .filter(p -> !p.isDynamic())
          .map(p -> p.toDescriptorResult(failures))
          .collect(toList());


      MetadataResult<ParameterMetadataDescriptor> contentResult =
          content.isPresent() ? content.get().toDescriptorResult(failures) : null;

      ImmutableComponentMetadataDescriptor descriptor = new ImmutableComponentMetadataDescriptor(componentName,
                                                                                                 parameterDescriptors,
                                                                                                 output
                                                                                                     .toDescriptorResult(failures),
                                                                                                 contentResult);
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
  private class ParameterMetadata implements Descriptable<ParameterMetadataDescriptor> {

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
      Optional<Failure> metadataFailure = getComponentFailure(failures, isDynamic ? CONTENT : name);
      ImmutableParameterMetadataDescriptor descriptor = new ImmutableParameterMetadataDescriptor(name, type);
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
  private class OutputMetadata implements Descriptable<OutputMetadataDescriptor> {

    private final TypeMetadata content;
    private final TypeMetadata attributes;

    OutputMetadata(ComponentMetadataDescriptor result) {
      OutputMetadataDescriptor outputDescriptor = result.getOutputMetadata().get();
      this.content = new TypeMetadata(outputDescriptor.getPayloadMetadata().get().getType(), true);
      this.attributes = new TypeMetadata(outputDescriptor.getAttributesMetadata().get().getType(), true);
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
      List<Failure> attributesFailure =
          getComponentFailure(failures, OUTPUT_ATTRIBUTES).map(Collections::singletonList).orElse(emptyList());
      List<Failure> payloadFailure =
          getComponentFailure(failures, OUTPUT_PAYLOAD).map(Collections::singletonList).orElse(emptyList());

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
}
