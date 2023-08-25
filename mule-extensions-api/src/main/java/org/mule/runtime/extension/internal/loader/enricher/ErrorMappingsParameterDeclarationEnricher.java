/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.Optional.of;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addErrorMappings;

import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.extension.api.ExtensionConstants;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.internal.property.NoErrorMappingModelProperty;

import java.util.Optional;

/**
 * A {@link DeclarationEnricher} which adds a {@link ExtensionConstants#ERROR_MAPPINGS_PARAMETER_NAME} parameter to all operations
 *
 * @since 1.4
 */
public final class ErrorMappingsParameterDeclarationEnricher implements WalkingDeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalkDelegate(ExtensionLoadingContext extensionLoadingContext) {
    return of(new IdempotentDeclarationEnricherWalkDelegate() {

      @Override
      protected void onOperation(OperationDeclaration declaration) {
        if (!declaration.getModelProperty(NoErrorMappingModelProperty.class).isPresent()) {
          addErrorMappings(declaration);
        }
      }
    });
  }
}
