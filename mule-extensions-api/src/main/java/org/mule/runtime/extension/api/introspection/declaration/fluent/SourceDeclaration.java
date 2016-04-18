/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricherFactory;
import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.api.introspection.metadata.MetadataResolverFactory;
import org.mule.runtime.extension.api.runtime.source.SourceFactory;
import org.mule.metadata.api.model.MetadataType;

import java.util.Optional;

/**
 * A declaration object for a {@link SourceModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link SourceModel}
 *
 * @since 1.0
 */
public class SourceDeclaration extends ParameterizedInterceptableDeclaration<SourceDeclaration>
{

    private MetadataType returnType;
    private MetadataType attributesType;
    private SourceFactory sourceFactory;
    private Optional<ExceptionEnricherFactory> exceptionEnricherFactory;
    private MetadataResolverFactory metadataResolverFactory;


    /**
     * {@inheritDoc}
     */
    SourceDeclaration(String name)
    {
        super(name);
    }

    public MetadataType getReturnType()
    {
        return returnType;
    }

    public void setReturnType(MetadataType returnType)
    {
        this.returnType = returnType;
    }

    public SourceFactory getSourceFactory()
    {
        return sourceFactory;
    }

    public void setSourceFactory(SourceFactory sourceFactory)
    {
        this.sourceFactory = sourceFactory;
    }

    public MetadataType getAttributesType()
    {
        return attributesType;
    }

    public void setAttributesType(MetadataType attributesType)
    {
        this.attributesType = attributesType;
    }

    public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory()
    {
        return exceptionEnricherFactory;
    }

    public void setExceptionEnricherFactory(Optional<ExceptionEnricherFactory> exceptionEnricherFactory)
    {
        this.exceptionEnricherFactory = exceptionEnricherFactory;
    }

    public MetadataResolverFactory getMetadataResolverFactory()
    {
        return metadataResolverFactory;
    }

    public void setMetadataResolverFactory(MetadataResolverFactory metadataResolverFactory)
    {
        this.metadataResolverFactory = metadataResolverFactory;
    }
}
