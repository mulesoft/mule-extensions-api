/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.util;

import static java.lang.String.format;

import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.property.InfrastructureParameterModelProperty;

/**
 * Utilities for validating extension models.
 * <p>
 * This class <b>IS NOT</b> part of the API. To be used by the Mule Runtime only
 *
 * @since 1.5.0
 */
public final class ExtensionValidationUtils {

  /**
   * Validates that the given {@code model} doesn't have parameters defined inline. If found, said parameters will be reported
   * through the {@code problemsReporter}
   *
   * @param model            the model being validated
   * @param kind             the kind of component being validated (Operation, Source, etc)
   * @param problemsReporter a {@link ProblemsReporter}
   * @param dsl              a {@link DslSyntaxResolver}
   */
  public static void validateNoInlineParameters(ParameterizedModel model,
                                                String kind,
                                                ProblemsReporter problemsReporter,
                                                DslSyntaxResolver dsl) {
    model.getParameterGroupModels().stream()
        .forEach(group -> {
          if (group.isShowInDsl()) {
            problemsReporter.addError(new Problem(model,
                                                  format("Invalid parameter group [%s] found in operation [%s], inline groups are not allowed in %s",
                                                         group.getName(), model.getName(), kind)));
          }
          group.getParameterModels().stream()
              // an example of this is error-mappings that are allowed in a scope
              .filter(parameter -> !parameter.getModelProperty(InfrastructureParameterModelProperty.class).isPresent())
              .forEach(parameter -> {
                if (dsl.resolve(parameter).supportsChildDeclaration()) {
                  problemsReporter.addError(new Problem(model,
                                                        format("Invalid parameter [%s] found in group [%s] of operation [%s], "
                                                            + "parameters that allow inline declaration are not allowed in %s. "
                                                            + "Use attribute declaration only for all the parameters.",
                                                               parameter.getName(), group.getName(), model.getName(), kind)));
                }
              });
        });
  }

  private ExtensionValidationUtils() {}
}
