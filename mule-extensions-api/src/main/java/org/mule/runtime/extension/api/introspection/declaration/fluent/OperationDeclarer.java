/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.runtime.operation.OperationExecutor;
import org.mule.runtime.extension.api.runtime.operation.OperationExecutorFactory;

/**
 * Allows configuring a {@link OperationDeclaration} through a fluent API
 *
 * @since 1.0
 */
public class OperationDeclarer extends ComponentDeclarer<OperationDeclarer, OperationDeclaration> {

  /**
   * Creates a new instance
   *
   * @param declaration the {@link OperationDeclaration} which will be configured
   */
  OperationDeclarer(OperationDeclaration declaration) {
    super(declaration);
  }

  /**
   * Specifies the {@link OperationExecutorFactory} to be used
   * to create {@link OperationExecutor} instances.
   *
   * @param executorFactory a {@link OperationExecutorFactory}
   * @return {@code this} declarer
   */
  public OperationDeclarer executorsCreatedBy(OperationExecutorFactory executorFactory) {
    declaration.setExecutorFactory(executorFactory);
    return this;
  }
}
