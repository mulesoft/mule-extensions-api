/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.util;

import static java.lang.String.format;

import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.property.InfrastructureParameterModelProperty;

public final class ExtensionValidationUtils {

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
