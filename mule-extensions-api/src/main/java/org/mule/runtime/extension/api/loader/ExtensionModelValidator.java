/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;

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
   * @deprecated since 1.9 use {@link #validate(ExtensionModel, DslSyntaxResolver, ProblemsReporter)} instead.
   */
  @Deprecated
  void validate(ExtensionModel model, ProblemsReporter problemsReporter);

  /**
   * Validates the given {@code model}
   *
   * @param model          a {@link ExtensionModel}
   * @param syntaxResolver the dsl syntax for the {@code extensionModel}
   * @throws IllegalModelDefinitionException if the model is illegal
   */
  default void validate(ExtensionModel model, DslSyntaxResolver syntaxResolver, ProblemsReporter problemsReporter) {
    validate(model, problemsReporter);
  }
}
