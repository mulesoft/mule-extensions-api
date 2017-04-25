/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

/**
 * Validates {@link ParameterModel parameters} with rules specific to {@link OperationModel}.
 *
 * @since 1.0
 */
public final class OperationParametersModelValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
    new IdempotentExtensionWalker() {

      @Override
      protected void onOperation(OperationModel model) {
        if (model.getOutput().getType() == null) {
          problemsReporter.addError(new Problem(model, format("Operation '%s' does not define an output type", model.getName())));
        }

        if (model.getOutputAttributes().getType() == null) {
          problemsReporter
              .addError(new Problem(model, format("Operation '%s' does not define an attributes output type", model.getName())));
        }
      }
    }.walk(extensionModel);
  }
}
