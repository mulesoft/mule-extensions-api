/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

/**
 * Validates rules specific to {@link FunctionModel}.
 *
 * @since 1.0
 */
public final class FunctionModelValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
    new IdempotentExtensionWalker() {

      @Override
      protected void onConfiguration(ConfigurationModel model) {
        if (!model.getFunctionModels().isEmpty()) {
          problemsReporter.addError(
                                    new Problem(model,
                                                format("Configuration [%s] declares scoped functions, but only extension-level functions are allowed",
                                                       model.getName())));
        }
      }

      @Override
      protected void onFunction(FunctionModel model) {
        model.getAllParameterModels().forEach(p -> validateParameter(model, p, problemsReporter));
      }
    }.walk(extensionModel);
  }

  private void validateParameter(FunctionModel model, ParameterModel parameter, ProblemsReporter problemsReporter) {

    if (parameter.isOverrideFromConfig()) {
      problemsReporter
          .addError(new Problem(model,
                                format("Parameter [%s] is declared as config override, but functions cannot be bound to a config.",
                                       parameter.getName())));
    }

    if (!parameter.getRole().equals(ParameterRole.BEHAVIOUR)) {
      problemsReporter
          .addError(new Problem(model, format("Parameter [%s] is declared as Content, but that is not allowed for functions.",
                                              parameter.getName())));
    }
  }
}
