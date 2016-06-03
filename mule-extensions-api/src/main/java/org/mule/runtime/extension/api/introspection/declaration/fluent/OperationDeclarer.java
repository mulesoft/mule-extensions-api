/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricherFactory;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.metadata.MetadataResolverFactory;
import org.mule.runtime.extension.api.runtime.InterceptorFactory;
import org.mule.runtime.extension.api.runtime.OperationExecutor;
import org.mule.runtime.extension.api.runtime.OperationExecutorFactory;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;

import java.util.Optional;

/**
 * Allows configuring a {@link OperationDeclaration} through a fluent API
 *
 * @since 1.0
 */
public class OperationDeclarer extends ParameterizedDeclarer<OperationDeclaration> implements HasModelProperties<OperationDeclarer>,
        HasInterceptors<OperationDeclarer>, HasExceptionEnricher<OperationDeclarer>, HasMetadataResolver<OperationDeclarer>
{

    /**
     * Creates a new instance
     *
     * @param declaration the {@link OperationDeclaration} which will be configured
     * @param typeLoader  a {@link ClassTypeLoader} used to create the {@link MetadataType types}
     */
    OperationDeclarer(OperationDeclaration declaration, ClassTypeLoader typeLoader)
    {
        super(declaration, typeLoader);
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return {@code this} declarer
     */
    public OperationDeclarer describedAs(String description)
    {
        declaration.setDescription(description);
        return this;
    }

    /**
     * Specifies the operation's return type
     *
     * @param returnType the returned type {@link Class}
     * @return {@code this} declarer
     */
    public OperationDeclarer whichReturns(Class<?> returnType)
    {
        return whichReturns(typeLoader.load(returnType));
    }

    /**
     * Specifies the operation's return type
     *
     * @param returnType a {@link MetadataType}
     * @return {@code this} declarer
     */
    public OperationDeclarer whichReturns(MetadataType returnType)
    {
        declaration.setReturnType(returnType);
        return this;
    }

    /**
     * Specifies the type of the attributes that the output
     * messages will have
     *
     * @param attributesType the attribute's {@link Class} type
     * @return {@code this} declarer
     */
    public OperationDeclarer withAttributesOfType(Class<?> attributesType)
    {
        return withAttributesOfType(typeLoader.load(attributesType));
    }

    /**
     * Specifies the type of the attributes that the output
     * messages will have
     *
     * @param attributesType a {@link MetadataType}
     * @return {@code this} declarer
     */
    public OperationDeclarer withAttributesOfType(MetadataType attributesType)
    {
        declaration.setAttributesType(attributesType);
        return this;
    }

    /**
     * Specifies the {@link OperationExecutorFactory} to be used
     * to create {@link OperationExecutor} instances.
     *
     * @param executorFactory a {@link OperationExecutorFactory}
     * @return {@code this} declarer
     */
    public OperationDeclarer executorsCreatedBy(OperationExecutorFactory executorFactory)
    {
        declaration.setExecutorFactory(executorFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationDeclarer withInterceptorFrom(InterceptorFactory interceptorFactory)
    {
        declaration.addInterceptorFactory(interceptorFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationDeclarer withModelProperty(ModelProperty modelProperty)
    {
        declaration.addModelProperty(modelProperty);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationDeclarer withExceptionEnricherFactory(Optional<ExceptionEnricherFactory> enricherFactory)
    {
        declaration.setExceptionEnricherFactory(enricherFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationDeclarer withMetadataResolverFactory(MetadataResolverFactory metadataResolver)
    {
        declaration.setMetadataResolverFactory(metadataResolver);
        return this;
    }
}
