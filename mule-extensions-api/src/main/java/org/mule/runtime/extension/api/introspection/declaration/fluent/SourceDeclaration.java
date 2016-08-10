/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.api.runtime.source.SourceFactory;

/**
 * A declaration object for a {@link SourceModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link SourceModel}
 *
 * @since 1.0
 */
public class SourceDeclaration extends ComponentDeclaration<SourceDeclaration> {

  private SourceFactory sourceFactory;

  /**
   * {@inheritDoc}
   */
  SourceDeclaration(String name) {
    super(name);
  }

  public SourceFactory getSourceFactory() {
    return sourceFactory;
  }

  public void setSourceFactory(SourceFactory sourceFactory) {
    this.sourceFactory = sourceFactory;
  }

}
