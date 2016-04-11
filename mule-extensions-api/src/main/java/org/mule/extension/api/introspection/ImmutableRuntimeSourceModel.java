/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.api.metadata.resolving.MetadataContentResolver;
import org.mule.api.metadata.resolving.MetadataKeysResolver;
import org.mule.api.metadata.resolving.MetadataOutputResolver;
import org.mule.extension.api.introspection.metadata.MetadataResolverFactory;
import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.extension.api.runtime.source.Source;
import org.mule.extension.api.runtime.source.SourceFactory;
import org.mule.metadata.api.model.MetadataType;

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
    private final MetadataResolverFactory metadataResolverFactory;
    private transient final List<InterceptorFactory> interceptorFactories;

    /**
     * Creates a new instance
     *
     * @param name                     the source name. Cannot be blank
     * @param description              the source description
     * @param parameterModels          a {@link List} with the source's {@link ParameterModel parameterModels}
     * @param returnType               a {@link MetadataType} which represents the payload of generated messages
     * @param attributesType           a {@link MetadataType} which represents the attributes on the generated messages
     * @param sourceFactory            a {@link SourceFactory} used to create instances of {@link Source} which are consistent with this model
     * @param modelProperties          A {@link Set} of custom properties which extend this model
     * @param interceptorFactories     A {@link List} with the {@link InterceptorFactory} instances that should be applied to instances built from this model
     * @param exceptionEnricherFactory an Optional @{@link ExceptionEnricherFactory} that creates a concrete {@link ExceptionEnricher} instance
     * @param metadataResolverFactory  a {@link MetadataResolverFactory} to create the associated {@link MetadataKeysResolver},
     *                                 {@link MetadataContentResolver} and {@link MetadataOutputResolver}
     */
    public ImmutableRuntimeSourceModel(String name,
                                       String description,
                                       List<ParameterModel> parameterModels,
                                       MetadataType returnType,
                                       MetadataType attributesType,
                                       SourceFactory sourceFactory,
                                       Set<ModelProperty> modelProperties,
                                       List<InterceptorFactory> interceptorFactories,
                                       Optional<ExceptionEnricherFactory> exceptionEnricherFactory,
                                       MetadataResolverFactory metadataResolverFactory)
    {
        super(name, description, parameterModels, returnType, attributesType, modelProperties);
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
