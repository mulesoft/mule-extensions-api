/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
