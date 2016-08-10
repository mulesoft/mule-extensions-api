/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.ComponentModel;
import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricherFactory;
import org.mule.runtime.extension.api.introspection.metadata.MetadataResolverFactory;

import java.util.Optional;

/**
 * A declaration object for a {@link ComponentModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ComponentModel}
 *
 * @since 1.0
 */
abstract class ComponentDeclaration<T extends ComponentDeclaration> extends ParameterizedInterceptableDeclaration<T> {

  private OutputDeclaration outputContent;
  private OutputDeclaration outputAttributes;
  private Optional<ExceptionEnricherFactory> exceptionEnricherFactory;
  private MetadataResolverFactory metadataResolverFactory;

  /**
   * {@inheritDoc}
   */
  ComponentDeclaration(String name) {
    super(name);
  }

  public OutputDeclaration getOutput() {
    return outputContent;
  }

  public void setOutput(OutputDeclaration content) {
    this.outputContent = content;
  }

  public OutputDeclaration getOutputAttributes() {
    return outputAttributes;
  }

  public void setOutputAttributes(OutputDeclaration attributes) {
    this.outputAttributes = attributes;
  }

  public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory() {
    return exceptionEnricherFactory;
  }

  public void setExceptionEnricherFactory(Optional<ExceptionEnricherFactory> exceptionEnricherFactory) {
    this.exceptionEnricherFactory = exceptionEnricherFactory;
  }

  public MetadataResolverFactory getMetadataResolverFactory() {
    return metadataResolverFactory;
  }

  public void setMetadataResolverFactory(MetadataResolverFactory metadataResolverFactory) {
    this.metadataResolverFactory = metadataResolverFactory;
  }
}
