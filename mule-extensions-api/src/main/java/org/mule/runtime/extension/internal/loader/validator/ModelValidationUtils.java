/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.internal.dsl.DslConstants.NAME_ATTRIBUTE_NAME;

import java.util.List;

import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.property.SyntheticModelModelProperty;

/**
 * Utility class for {@link ExtensionModelValidator}s
 *
 * @since 1.5
 */
public final class ModelValidationUtils {

  private ModelValidationUtils() {}

  public static void validateConfigOverrideParametersNotAllowed(ParameterizedModel model, ProblemsReporter reporter,
                                                                String kind) {

    List<String> configOverrideParameters = model.getAllParameterModels().stream()
        .filter(ParameterModel::isOverrideFromConfig)
        .map(ParameterModel::getName)
        .collect(toList());

    if (!configOverrideParameters.isEmpty()) {
      reporter.addError(new Problem(model, format(
                                                  "%s '%s' declares the parameters %s as '%s', which is not allowed for this component parameters",
                                                  kind,
                                                  model.getName(),
                                                  configOverrideParameters.toString(),
                                                  ConfigOverride.class.getSimpleName())));
    }
  }

  public static void validateConfigParametersNamesNotAllowed(ParameterizedModel model, ProblemsReporter reporter, String kind) {

    model.getAllParameterModels().stream()
        .filter(parameterModel -> parameterModel.getName().equals(NAME_ATTRIBUTE_NAME)
            && !parameterModel.getModelProperty(SyntheticModelModelProperty.class).isPresent())
        .findAny()
        .ifPresent((parameterModel) -> reporter
            .addError(new Problem(model, format("%s '%s' declares a parameter whose name is '%s', which is not allowed.",
                                                kind,
                                                model.getName(),
                                                NAME_ATTRIBUTE_NAME))));
  }

}
