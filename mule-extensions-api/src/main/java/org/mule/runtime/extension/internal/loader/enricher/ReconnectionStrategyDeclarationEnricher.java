/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addReconnectionStrategyParameter;

import org.mule.runtime.api.meta.model.declaration.fluent.ExecutableComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.internal.property.NoReconnectionStrategyModelProperty;

import java.util.Optional;

/**
 * A {@link DeclarationEnricher} which adds the following to all {@link SourceDeclaration}:
 *
 * <ul>
 * <li>A reconnection strategy parameter</li>
 * </ul>
 *
 * @since 1.0
 */
public final class ReconnectionStrategyDeclarationEnricher implements WalkingDeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalker(ExtensionLoadingContext extensionLoadingContext) {
    final ExtensionDeclaration declaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();

    if (!declaration.getModelProperty(NoReconnectionStrategyModelProperty.class).isPresent()) {
      return of(new IdempotentDeclarationEnricherWalkDelegate() {

        @Override
        protected void onSource(SourceDeclaration declaration) {
          addReconnectionIfRequired(declaration);
        }

        @Override
        protected void onOperation(OperationDeclaration declaration) {
          addReconnectionIfRequired(declaration);
        }

        private void addReconnectionIfRequired(ExecutableComponentDeclaration declaration) {
          if (declaration.isRequiresConnection()) {
            addReconnectionStrategyParameter(declaration);
          }
        }
      });
    }

    return empty();
  }
}
