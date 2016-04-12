/*
/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ExceptionEnricherFactory;
import org.mule.extension.api.introspection.ModelProperty;
import org.mule.extension.api.introspection.metadata.MetadataResolverFactory;
import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.extension.api.runtime.source.Source;
import org.mule.extension.api.runtime.source.SourceFactory;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;

import java.util.Optional;

/**
 * Allows configuring a {@link SourceDeclaration} through a fluent API
 *
 * @since 1.0
 */
public class SourceDeclarer extends ParameterizedDeclarer implements HasModelProperties<SourceDeclarer>, HasInterceptors<SourceDeclarer>,
        HasExceptionEnricher<SourceDeclarer>, HasMetadataResolver<SourceDeclarer>
{

    private final SourceDeclaration source;

    /**
     * Creates a new instance
     *
     * @param source     the {@link SourceDeclaration} to be configured
     * @param typeLoader a {@link ClassTypeLoader} used to create the {@link MetadataType types}
     */
    SourceDeclarer(SourceDeclaration source, ClassTypeLoader typeLoader)
    {
        super(source, typeLoader);
        this.source = source;
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return {@code this} descriptor
     */
    public SourceDeclarer describedAs(String description)
    {
        source.setDescription(description);
        return this;
    }

    /**
     * Specifies the source's return type
     *
     * @param returnType the returned type {@link Class}
     * @return {@code this} descriptor
     */
    public SourceDeclarer whichReturns(Class<?> returnType)
    {
        return whichReturns(typeLoader.load(returnType));
    }

    /**
     * Specifies the source's return type
     *
     * @param returnType a {@link MetadataType}
     * @return {@code this} descriptor
     */
    public SourceDeclarer whichReturns(MetadataType returnType)
    {
        source.setReturnType(returnType);
        return this;
    }

    /**
     * Specifies the type of the attributes that the generated
     * messages will have
     *
     * @param attributesType the attribute's {@link Class} type
     * @return {@code this} descriptor
     */
    public SourceDeclarer withAttributesOfType(Class<?> attributesType)
    {
        return withAttributesOfType(typeLoader.load(attributesType));
    }

    /**
     * Specifies the type of the attributes that the generated
     * messages will have
     *
     * @param attributesType a {@link MetadataType}
     * @return {@code this} descriptor
     */
    public SourceDeclarer withAttributesOfType(MetadataType attributesType)
    {
        source.setAttributesType(attributesType);
        return this;
    }

    /**
     * Specifies the {@link SourceFactory} to be used
     * to create {@link Source} instances.
     *
     * @param sourceFactory a {@link SourceDeclarer}
     * @return {@code this} descriptor
     */
    public SourceDeclarer sourceCreatedBy(SourceFactory sourceFactory)
    {
        source.setSourceFactory(sourceFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDeclarer withInterceptorFrom(InterceptorFactory interceptorFactory)
    {
        source.addInterceptorFactory(interceptorFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDeclarer withModelProperty(ModelProperty modelProperty)
    {
        source.addModelProperty(modelProperty);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDeclarer withExceptionEnricherFactory(Optional<ExceptionEnricherFactory> enricherFactory)
    {
        source.setExceptionEnricherFactory(enricherFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public SourceDeclarer withMetadataResolverFactory(MetadataResolverFactory metadataResolverFactory)
    {
        source.setMetadataResolverFactory(metadataResolverFactory);
        return this;
    }
}
