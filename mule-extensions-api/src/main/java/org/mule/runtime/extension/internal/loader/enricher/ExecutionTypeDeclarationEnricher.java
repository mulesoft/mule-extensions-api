/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.api.meta.model.ExecutionType.BLOCKING;
import static org.mule.runtime.api.meta.model.ExecutionType.CPU_LITE;
import org.mule.runtime.api.meta.model.ExecutionType;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

/**
 * Sets the {@link ExecutionType} on all operations which didn't explicitly
 * set one. This is done by doing a best guess with the following rules:
 *
 * <ul>
 *   <li>Operation requires connection and is blocking: {@link ExecutionType#BLOCKING}</li>
 *   <li>Operation requires connection and is non blocking: {@link ExecutionType#CPU_LITE}</li>
 *   <li>None of the above: {@link ExecutionType#CPU_LITE}</li>
 * </ul>
 *
 * Notice that under no circumstance the runtime will guess the operation is {@link ExecutionType#CPU_INTENSIVE},
 * the user needs to hint that manually.
 *
 * @since 1.0
 */
public final class ExecutionTypeDeclarationEnricher implements DeclarationEnricher {

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new IdempotentDeclarationWalker() {

      @Override
      protected void onOperation(OperationDeclaration declaration) {
        declaration.setExecutionType(resolve(declaration));
      }
    }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
  }

  private ExecutionType resolve(OperationDeclaration declaration) {
    ExecutionType executionType = declaration.getExecutionType();
    if (executionType != null) {
      return executionType;
    }

    if (declaration.isRequiresConnection()) {
      return declaration.isBlocking() ? BLOCKING : CPU_LITE;
    }

    return CPU_LITE;
  }
}
