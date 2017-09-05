/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static org.mule.runtime.extension.api.error.MuleErrors.VALIDATION;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.VALIDATOR;
import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.annotation.param.stereotype.Validator;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

/**
 * Makes sure that operations with the {@code VALIDATOR} stereotype comply with its restrictions, as defined in
 * {@link Validator}
 *
 * @since 1.0
 */
public class ValidatorModelValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel model, ProblemsReporter problemsReporter) {

    new IdempotentExtensionWalker() {

      @Override
      protected void onOperation(OperationModel operation) {
        if (!operation.getStereotype().isAssignableTo(VALIDATOR)) {
          return;
        }

        if (!(operation.getOutput().getType() instanceof VoidType)) {
          problemsReporter.addError(new Problem(operation, format("Operation '%s' is marked as a validator but it's not void.",
                                                                  operation.getName())));
        }

        boolean hasValidationError = operation.getErrorModels().stream()
            .filter(e -> isValidation(e))
            .findAny()
            .isPresent();

        if (!hasValidationError) {
          problemsReporter.addError(new Problem(operation,
                                                format("Operation '%s' is a validator but it doesn't declare a validation error. "
                                                    + "Upon failure, all validators must throw a Mule validation error "
                                                    + "or a child of one", operation.getName())));
        }
      }
    }.walk(model);
  }

  private boolean isValidation(ErrorModel errorModel) {
    if (VALIDATION.getType().equals(errorModel.getType()) &&
        "MULE".equals(errorModel.getNamespace())) {
      return true;
    }

    return errorModel.getParent().map(this::isValidation).orElse(false);
  }
}
