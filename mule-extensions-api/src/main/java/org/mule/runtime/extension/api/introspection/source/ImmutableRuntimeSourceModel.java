/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.source;

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
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceFactory;

import java.beans.Transient;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Runtime Immutable implementation of {@link SourceModel}
 *
 * @since 1.0
 */
public final class ImmutableRuntimeSourceModel extends ImmutableSourceModel implements RuntimeSourceModel
{

    private transient final SourceFactory sourceFactory;
    private transient final Optional<ExceptionEnricherFactory> exceptionEnricherFactory;
    private transient final MetadataResolverFactory metadataResolverFactory;
    private transient final List<InterceptorFactory> interceptorFactories;

    /**
     * Creates a new instance
     *
     * @param name                     the source name. Cannot be blank
     * @param description              the source description
     * @param parameterModels          a {@link List} with the source's {@link ParameterModel parameterModels}
     * @param outputPayload            an {@link OutputModel} which represents the operation's output payload
     * @param outputAttributes         an {@link OutputModel} which represents the attributes on the output me
     * @param sourceFactory            a {@link SourceFactory} used to create instances of {@link Source} which are consistent with this model
     * @param modelProperties          A {@link Set} of custom properties which extend this model
     * @param interceptorFactories     A {@link List} with the {@link InterceptorFactory} instances that should be applied to instances built from this model
     * @param exceptionEnricherFactory an Optional @{@link ExceptionEnricherFactory} that creates a concrete {@link ExceptionEnricher} instance
     * @param metadataResolverFactory  a {@link MetadataResolverFactory} to create the associated {@link MetadataKeysResolver}, {@link MetadataContentResolver} and {@link MetadataOutputResolver}
     * @throws IllegalArgumentException if {@code name} is blank or {@code sourceFactory} is {@code null}
     */
    public ImmutableRuntimeSourceModel(String name,
                                       String description,
                                       List<ParameterModel> parameterModels,
                                       OutputModel outputPayload,
                                       OutputModel outputAttributes,
                                       SourceFactory sourceFactory,
                                       Set<ModelProperty> modelProperties,
                                       List<InterceptorFactory> interceptorFactories,
                                       Optional<ExceptionEnricherFactory> exceptionEnricherFactory,
                                       MetadataResolverFactory metadataResolverFactory)
    {
        super(name, description, parameterModels, outputPayload, outputAttributes, modelProperties);
        if (sourceFactory == null)
        {
            throw new IllegalArgumentException(String.format("Source '%s' cannot have a null source factory", name));
        }
        this.sourceFactory = sourceFactory;
        this.exceptionEnricherFactory = exceptionEnricherFactory;
        this.metadataResolverFactory = metadataResolverFactory;
        this.interceptorFactories = interceptorFactories != null ? Collections.unmodifiableList(interceptorFactories) : Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    @Override
    public SourceFactory getSourceFactory()
    {
        return sourceFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    @Override
    public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory()
    {
        return exceptionEnricherFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    @Override
    public List<InterceptorFactory> getInterceptorFactories()
    {
        return interceptorFactories;
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    @Override
    public MetadataResolverFactory getMetadataResolverFactory()
    {
        return metadataResolverFactory;
    }
}
