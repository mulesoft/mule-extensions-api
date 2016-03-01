/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.extension.api.runtime.OperationExecutorFactory;
import org.mule.metadata.api.model.MetadataType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Immutable concrete implementation of {@link OperationModel}
 *
 * @since 1.0
 */
public final class ImmutableOperationModel extends AbstractInterceptableModel implements OperationModel
{

    private final List<ParameterModel> parameterModels;
    private final OperationExecutorFactory executorFactory;
    private final MetadataType returnType;
    private final Optional<ExceptionEnricherFactory> exceptionEnricherFactory;

    /**
     * Creates a new instance with the given state
     *
     * @param name                     the operation's name. Cannot be blank
     * @param description              the operation's descriptor
     * @param executorFactory          a {@link OperationExecutorFactory}. Cannot be {@code null}
     * @param parameterModels          a {@link List} with the operation's {@link ParameterModel parameterModels}
     * @param returnType               a {@link MetadataType} which represents the operation's output
     * @param modelProperties          A {@link Map} of custom properties which extend this model
     * @param interceptorFactories     A {@link List} with the {@link InterceptorFactory} instances that should be applied to instances built from this model
     * @param exceptionEnricherFactory an Optional {@link ExceptionEnricherFactory} to create an {@link ExceptionEnricher} instance
     * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
     */
    public ImmutableOperationModel(String name,
                                   String description,
                                   OperationExecutorFactory executorFactory,
                                   List<ParameterModel> parameterModels,
                                   MetadataType returnType,
                                   Map<String, Object> modelProperties,
                                   List<InterceptorFactory> interceptorFactories,
                                   Optional<ExceptionEnricherFactory> exceptionEnricherFactory)
    {
        super(name, description, modelProperties, interceptorFactories);
        if (executorFactory == null)
        {
            throw new IllegalArgumentException(String.format("Operation '%s' cannot have a null executor factory", name));
        }

        this.returnType = returnType;
        this.executorFactory = executorFactory;
        this.parameterModels = Collections.unmodifiableList(parameterModels);
        this.exceptionEnricherFactory = exceptionEnricherFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParameterModel> getParameterModels()
    {
        return parameterModels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationExecutorFactory getExecutor()
    {
        return executorFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataType getReturnType()
    {
        return returnType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory()
    {
        return exceptionEnricherFactory;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append("ImmutableOperationModel{")
                .append(super.toString())
                .append(", parameterModels=").append(parameterModels)
                .append(", executorFactory=").append(executorFactory)
                .append(", returnType=").append(returnType)
                .append(", exceptionEnricherFactory=").append(exceptionEnricherFactory)
                .append('}').toString();
    }
}
