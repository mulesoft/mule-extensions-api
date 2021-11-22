/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
import static org.mule.runtime.api.meta.model.error.ErrorModelBuilder.newError;
import static org.mule.runtime.extension.api.error.MuleErrors.ANY;
import static org.mule.runtime.extension.api.error.MuleErrors.VALIDATION;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.POST_STRUCTURE;
import static org.mule.runtime.extension.internal.util.ExtensionErrorUtils.getValidationError;
import static org.mule.runtime.extension.internal.util.ExtensionNamespaceUtils.getExtensionsNamespace;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_PREFIX;
import static org.mule.sdk.api.stereotype.MuleStereotypes.VALIDATOR;

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
    new EnricherDelegate(extensionLoadingContext).enrich();
  }

  private class EnricherDelegate {

    private final ExtensionDeclaration extensionDeclaration;
    private final Set<ErrorModel> errorModels;
    private final ErrorModel connectivityError;
    private final ErrorModel retryErrorModel;
    private ErrorModel validationErrorModel;

    public EnricherDelegate(ExtensionLoadingContext extensionLoadingContext) {
      extensionDeclaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
      errorModels = extensionDeclaration.getErrorModels();
      connectivityError = getErrorModel(CONNECTIVITY_ERROR_TYPE, errorModels);
      retryErrorModel = getErrorModel(RETRY_EXHAUSTED_ERROR_TYPE, errorModels);
    }

    public void enrich() {

      new IdempotentDeclarationWalker() {

        @Override
        public void onOperation(WithOperationsDeclaration owner, OperationDeclaration operationDeclaration) {
          errorModels.addAll(operationDeclaration.getErrorModels());
          if (operationDeclaration.isRequiresConnection()) {
            addErrorModel(operationDeclaration, connectivityError, CONNECTIVITY_ERROR_TYPE);
            addErrorModel(operationDeclaration, retryErrorModel, RETRY_EXHAUSTED_ERROR_TYPE);
          }

          assureValidationError(extensionDeclaration, operationDeclaration);
        }
      }.walk(extensionDeclaration);
    }

    private void assureValidationError(ExtensionDeclaration extensionDeclaration, OperationDeclaration operation) {
      if (operation.getStereotype() != null && operation.getStereotype().isAssignableTo(VALIDATOR)) {
        if (!getValidationError(operation.getErrorModels()).isPresent()) {
          operation.addErrorModel(getOrAddValidationError(extensionDeclaration));
        }
      }
    }

    private ErrorModel getOrAddValidationError(ExtensionDeclaration extensionDeclaration) {
      if (validationErrorModel == null) {
        validationErrorModel = getValidationError(extensionDeclaration.getErrorModels()).orElse(null);
        if (validationErrorModel == null) {
          ErrorModel parent = newError(VALIDATION.getType(), "MULE")
              .withParent(newError(ANY.getType(), "MULE").build())
              .build();
          validationErrorModel = newError(VALIDATION.getType(), getExtensionsNamespace(extensionDeclaration))
              .withParent(parent)
              .build();
        }

        extensionDeclaration.addErrorModel(validationErrorModel);
      }

      return validationErrorModel;
    }

  }

  private void addErrorModel(OperationDeclaration declaration, ErrorModel errorModel, String type) {
    if (errorModel != null) {
      declaration.getErrorModels().add(errorModel);
    } else {
      throw new IllegalModelDefinitionException(format(ERROR_MASK, type, declaration.getName()));
    }
  }

  private ErrorModel getErrorModel(String type, Set<ErrorModel> errors) {
    return errors
        .stream()
        .filter(e -> !e.getNamespace().equals(CORE_NAMESPACE_NAME) && e.getType().equals(type))
        .findFirst()
        .orElse(null);
  }
}
