/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.VALIDATOR;
import static org.mule.runtime.extension.internal.util.ExtensionErrorUtils.getValidationError;

import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.annotation.param.stereotype.Validator;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

/**
 * Makes sure that operations with the {@code VALIDATOR} stereotype comply with its restrictions, as defined in {@link Validator}
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

        if (!getValidationError(operation.getErrorModels()).isPresent()) {
          problemsReporter.addError(new Problem(operation,
                                                format("Operation '%s' is a validator but it doesn't declare a validation error. "
                                                    + "Upon failure, all validators must throw a Mule validation error "
                                                    + "or a child of one", operation.getName())));
        }
      }
    }.walk(model);
  }
}
