/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.api.meta.model.ExtensionModel;

/**
 * Validates that a constructed model is legal, meaning it's consistent and doesn't violate any restriction.
 *
 * @since 1.0
 */
public interface ExtensionModelValidator {

  /**
   * Validates the given {@code model}
   *
   * @param model a {@link ExtensionModel}
   * @throws IllegalModelDefinitionException if the model is illegal
   */
  void validate(ExtensionModel model, ProblemsReporter problemsReporter);
}
