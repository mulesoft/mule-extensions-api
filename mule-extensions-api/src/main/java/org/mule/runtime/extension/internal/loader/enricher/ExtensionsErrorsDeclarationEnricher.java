/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.POST_STRUCTURE;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_PREFIX;

import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithOperationsDeclaration;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.error.MuleErrors;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

import java.util.Optional;
import java.util.Set;

/**
 * {@link DeclarationEnricher} implementation which enriches {@link OperationModel operationModels} adding connectivity related
 * {@link MuleErrors} if the operations are considered as a connected ones.
 *
 * @since 1.5.0
 */
public class ExtensionsErrorsDeclarationEnricher implements DeclarationEnricher {

  private static final String CORE_NAMESPACE_NAME = CORE_PREFIX.toUpperCase();
  private static final String CONNECTIVITY_ERROR_TYPE = "CONNECTIVITY";
  private static final String RETRY_EXHAUSTED_ERROR_TYPE = "RETRY_EXHAUSTED";
  private static String ERROR_MASK = "Trying to add the '%s' Error to the Component '%s' but the Extension doesn't declare it";

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return POST_STRUCTURE;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new EnricherDelegate().enrich(extensionLoadingContext);
  }

  private class EnricherDelegate implements DeclarationEnricher {

    @Override
    public void enrich(ExtensionLoadingContext extensionLoadingContext) {
      ExtensionDeclaration extensionDeclaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
      Set<ErrorModel> errorModels = extensionDeclaration.getErrorModels();
      Optional<ErrorModel> connectivityError = getErrorModel(CONNECTIVITY_ERROR_TYPE, errorModels);
      Optional<ErrorModel> retryErrorModel = getErrorModel(RETRY_EXHAUSTED_ERROR_TYPE, errorModels);

      new IdempotentDeclarationWalker() {

        @Override
        public void onOperation(WithOperationsDeclaration owner, OperationDeclaration operationDeclaration) {
          errorModels.addAll(operationDeclaration.getErrorModels());
          if (operationDeclaration.isRequiresConnection()) {
            addErrorModel(operationDeclaration, connectivityError, CONNECTIVITY_ERROR_TYPE);
            addErrorModel(operationDeclaration, retryErrorModel, RETRY_EXHAUSTED_ERROR_TYPE);
          }
        }
      }.walk(extensionDeclaration);
    }
  }

  private void addErrorModel(OperationDeclaration declaration, Optional<ErrorModel> errorModel, String type) {
    if (errorModel.isPresent()) {
      declaration.getErrorModels().add(errorModel.get());
    } else {
      throw new IllegalModelDefinitionException(format(ERROR_MASK, type, declaration.getName()));
    }
  }

  private Optional<ErrorModel> getErrorModel(String type, Set<ErrorModel> errors) {
    return errors
        .stream()
        .filter(e -> !e.getNamespace().equals(CORE_NAMESPACE_NAME) && e.getType().equals(type))
        .findFirst();
  }
}
