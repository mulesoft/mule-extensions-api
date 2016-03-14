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
import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.extension.api.runtime.source.Source;
import org.mule.extension.api.runtime.source.SourceFactory;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;

import java.util.Optional;

/**
 * A {@link Descriptor} which allows configuring a {@link SourceDeclaration}
 * through a fluent API
 *
 * @since 1.0
 */
public class SourceDescriptor extends HasParameters implements HasModelProperties<SourceDescriptor>, HasInterceptors<SourceDescriptor>, HasExceptionEnricher<SourceDescriptor>
{

    private final SourceDeclaration source;

    /**
     * Creates a new instance
     *
     * @param source      the {@link SourceDeclaration} which will be defined through {@code this} {@link Descriptor}
     * @param declaration the {@link DeclarationDescriptor} which owns {@code this} one
     */
    SourceDescriptor(SourceDeclaration source, DeclarationDescriptor declaration, ClassTypeLoader typeLoader)
    {
        super(declaration, typeLoader);
        this.source = source;
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return {@code this} descriptor
     */
    public SourceDescriptor describedAs(String description)
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
    public SourceDescriptor whichReturns(Class<?> returnType)
    {
        return whichReturns(typeLoader.load(returnType));
    }

    /**
     * Specifies the source's return type
     *
     * @param returnType a {@link MetadataType}
     * @return {@code this} descriptor
     */
    public SourceDescriptor whichReturns(MetadataType returnType)
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
    public SourceDescriptor withAttributesOfType(Class<?> attributesType)
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
    public SourceDescriptor withAttributesOfType(MetadataType attributesType)
    {
        source.setAttributesType(attributesType);
        return this;
    }

    /**
     * Specifies the {@link SourceFactory} to be used
     * to create {@link Source} instances.
     *
     * @param sourceFactory a {@link SourceDescriptor}
     * @return {@code this} descriptor
     */
    public SourceDescriptor sourceCreatedBy(SourceFactory sourceFactory)
    {
        source.setSourceFactory(sourceFactory);
        return this;
    }

    /**
     * Adds a {@link ParameterDeclaration}
     *
     * @param parameter a {@link ParameterDeclaration}
     */
    @Override
    protected void addParameter(ParameterDeclaration parameter)
    {
        source.addParameter(parameter);
    }

    /**
     * @return a {@link WithParameters} which allows adding {@link ParameterDeclaration}
     * to this descriptor
     */
    public WithParameters with()
    {
        return new WithParameters(this, getRootDeclaration(), typeLoader);
    }

    /**
     * @return the root {@link DeclarationDescriptor}
     */
    @Override
    public DeclarationDescriptor getRootDeclaration()
    {
        return declaration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDescriptor withInterceptorFrom(InterceptorFactory interceptorFactory)
    {
        source.addInterceptorFactory(interceptorFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDescriptor withModelProperty(ModelProperty modelProperty)
    {
        source.addModelProperty(modelProperty);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDescriptor withExceptionEnricherFactory(Optional<ExceptionEnricherFactory> enricherFactory)
    {
        source.setExceptionEnricherFactory(enricherFactory);
        return this;
    }

    /**
     * @return the {@link SourceDescriptor} which is being defined by {@code this} {@link Descriptor}
     */
    public SourceDeclaration getDeclaration()
    {
        return source;
    }
}
