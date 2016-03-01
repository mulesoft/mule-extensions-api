/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.extension.api.runtime.source.Source;
import org.mule.extension.api.runtime.source.SourceFactory;
import org.mule.metadata.api.model.MetadataType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Immutable implementation of {@link SourceModel}
 *
 * @since 1.0
 */
public final class ImmutableSourceModel extends AbstractInterceptableModel implements SourceModel
{

    private final List<ParameterModel> parameterModels;
    private final MetadataType returnType;
    private final MetadataType attributesType;
    private final SourceFactory sourceFactory;
    private final Optional<ExceptionEnricherFactory> exceptionEnricherFactory;

    /**
     * Creates a new instance
     *
     * @param name                     the source name. Cannot be blank
     * @param description              the source description
     * @param parameterModels          a {@link List} with the source's {@link ParameterModel parameterModels}
     * @param returnType               a {@link MetadataType} which represents the payload of generated messages
     * @param attributesType           a {@link MetadataType} which represents the attributes on the generated messages
     * @param sourceFactory            a {@link SourceFactory} used to create instances of {@link Source} which are consistent with this model
     * @param modelProperties          A {@link Map} of custom properties which extend this model
     * @param interceptorFactories     A {@link List} with the {@link InterceptorFactory} instances that should be applied to instances built from this model
     * @param exceptionEnricherFactory an Optional @{@link ExceptionEnricherFactory} that creates a concrete {@link ExceptionEnricher} instance
     */
    public ImmutableSourceModel(String name,
                                String description,
                                List<ParameterModel> parameterModels,
                                MetadataType returnType,
                                MetadataType attributesType,
                                SourceFactory sourceFactory,
                                Map<String, Object> modelProperties,
                                List<InterceptorFactory> interceptorFactories,
                                Optional<ExceptionEnricherFactory> exceptionEnricherFactory)
    {
        super(name, description, modelProperties, interceptorFactories);
        this.parameterModels = Collections.unmodifiableList(parameterModels);
        this.returnType = returnType;
        this.sourceFactory = sourceFactory;
        this.attributesType = attributesType;
        this.exceptionEnricherFactory = exceptionEnricherFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceFactory getSourceFactory()
    {
        return sourceFactory;
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
    public List<ParameterModel> getParameterModels()
    {
        return parameterModels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataType getAttributesType()
    {
        return attributesType;
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
        return new StringBuilder().append("ImmutableSourceModel{")
                .append(super.toString())
                .append(", parameterModels=").append(parameterModels)
                .append(", returnType=").append(returnType)
                .append(", attributesType=").append(attributesType)
                .append(", sourceFactory=").append(sourceFactory)
                .append('}').toString();
    }
}
