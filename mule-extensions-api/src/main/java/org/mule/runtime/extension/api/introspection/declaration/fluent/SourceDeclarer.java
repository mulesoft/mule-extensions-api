/*
/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceFactory;

/**
 * Allows configuring a {@link SourceDeclaration} through a fluent API
 *
 * @since 1.0
 */
public class SourceDeclarer extends ComponentDeclarer<SourceDeclarer, SourceDeclaration> {

  /**
   * Creates a new instance
   *
   * @param declaration the {@link SourceDeclaration} to be configured
   */
  SourceDeclarer(SourceDeclaration declaration) {
    super(declaration);
  }

  /**
   * Specifies the {@link SourceFactory} to be used
   * to create {@link Source} instances.
   *
   * @param sourceFactory a {@link SourceDeclarer}
   * @return {@code this} descriptor
   */
  public SourceDeclarer sourceCreatedBy(SourceFactory sourceFactory) {
    declaration.setSourceFactory(sourceFactory);
    return this;
  }

}
