/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.operation;

import org.mule.runtime.api.message.MuleMessage;
import org.mule.runtime.api.metadata.resolving.MetadataContentResolver;
import org.mule.runtime.api.metadata.resolving.MetadataKeysResolver;
import org.mule.runtime.api.metadata.resolving.MetadataOutputResolver;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.OutputModel;
import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricher;
import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricherFactory;
import org.mule.runtime.extension.api.introspection.metadata.MetadataResolverFactory;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.runtime.InterceptorFactory;
import org.mule.runtime.extension.api.runtime.operation.OperationExecutorFactory;

import java.beans.Transient;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Runtime Immutable implementation of {@link OperationModel}
 *
 * @since 1.0
 */
public final class ImmutableRuntimeOperationModel extends ImmutableOperationModel implements RuntimeOperationModel {

  private final transient OperationExecutorFactory executorFactory;
  private final transient Optional<ExceptionEnricherFactory> exceptionEnricherFactory;
  private final transient List<InterceptorFactory> interceptorFactories;
  private final MetadataResolverFactory metadataResolverFactory;

  /**
   * Creates a new instance with the given state
   *
   * @param name                     the operation's name. Cannot be blank
   * @param description              the operation's descriptor
   * @param executorFactory          a {@link OperationExecutorFactory}. Cannot be {@code null}
   * @param parameterModels          a {@link List} with the operation's {@link ParameterModel parameterModels}
   * @param output                   an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes         an {@link OutputModel} which represents the attributes on the output {@link MuleMessage}
   * @param modelProperties          a {@link Set} of custom properties which extend this model
   * @param interceptorFactories     a {@link List} with the {@link InterceptorFactory} instances that should be applied to instances built from this model
   * @param exceptionEnricherFactory an Optional {@link ExceptionEnricherFactory} to create an {@link ExceptionEnricher} instance
   * @param metadataResolverFactory  a {@link MetadataResolverFactory} to create the associated {@link MetadataKeysResolver},
   *                                 {@link MetadataContentResolver} and {@link MetadataOutputResolver}
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   */

  public ImmutableRuntimeOperationModel(String name,
                                        String description,
                                        OperationExecutorFactory executorFactory,
                                        List<ParameterModel> parameterModels,
                                        OutputModel output,
                                        OutputModel outputAttributes,
                                        Set<ModelProperty> modelProperties,
                                        List<InterceptorFactory> interceptorFactories,
                                        Optional<ExceptionEnricherFactory> exceptionEnricherFactory,
                                        MetadataResolverFactory metadataResolverFactory) {
    super(name, description, parameterModels, output, outputAttributes, modelProperties);
    if (executorFactory == null) {
      throw new IllegalArgumentException(String.format("Operation '%s' cannot have a null executor factory", name));
    }
    this.executorFactory = executorFactory;
    this.exceptionEnricherFactory = exceptionEnricherFactory;
    this.interceptorFactories =
        interceptorFactories != null ? Collections.unmodifiableList(interceptorFactories) : Collections.emptyList();

    this.metadataResolverFactory = metadataResolverFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Transient
  @Override
  public OperationExecutorFactory getExecutor() {
    return executorFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Transient
  @Override
  public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory() {
    return exceptionEnricherFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Transient
  @Override
  public List<InterceptorFactory> getInterceptorFactories() {
    return interceptorFactories;
  }

  /**
   * {@inheritDoc}
   */
  @Transient
  @Override
  public MetadataResolverFactory getMetadataResolverFactory() {
    return metadataResolverFactory;
  }

}
