/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricherFactory;
import org.mule.runtime.extension.api.introspection.metadata.MetadataResolverFactory;
import org.mule.runtime.extension.api.runtime.InterceptorFactory;

import java.util.Optional;

/**
 * Allows configuring a {@link ComponentDeclaration} through a fluent API
 *
 * @since 1.0
 */
public abstract class ComponentDeclarer<T extends ComponentDeclarer, D extends ComponentDeclaration>
    extends ConfigurableOutputDeclarer<D>
    implements HasModelProperties<ComponentDeclarer>, HasInterceptors<ComponentDeclarer>,
    HasExceptionEnricher<ComponentDeclarer>, HasMetadataResolver<ComponentDeclarer> {

  /**
   * Creates a new instance
   *
   * @param declaration the {@link ComponentDeclaration} which will be configured
   */
  ComponentDeclarer(D declaration) {
    super(declaration);
  }

  /**
   * Adds a description
   *
   * @param description a description
   * @return {@code this} declarer
   */
  public T describedAs(String description) {
    declaration.setDescription(description);
    return (T) this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T withInterceptorFrom(InterceptorFactory interceptorFactory) {
    declaration.addInterceptorFactory(interceptorFactory);
    return (T) this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T withModelProperty(ModelProperty modelProperty) {
    declaration.addModelProperty(modelProperty);
    return (T) this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T withExceptionEnricherFactory(Optional<ExceptionEnricherFactory> enricherFactory) {
    declaration.setExceptionEnricherFactory(enricherFactory);
    return (T) this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T withMetadataResolverFactory(MetadataResolverFactory metadataResolver) {
    declaration.setMetadataResolverFactory(metadataResolver);
    return (T) this;
  }
}
