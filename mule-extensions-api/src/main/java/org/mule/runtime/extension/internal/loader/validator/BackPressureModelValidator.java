/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.extension.api.ExtensionConstants.BACK_PRESSURE_STRATEGY_PARAMETER_NAME;
import org.mule.metadata.api.annotation.EnumAnnotation;
import org.mule.metadata.api.model.StringType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.runtime.source.BackPressureMode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Validates that the backPressureStrategy parameter is correctly formed on the sources that apply
 *
 * @since 1.1
 */
public class BackPressureModelValidator implements ExtensionModelValidator {

  private static final String ERROR_PREFIX = "backPressureStrategy parameter ";

  @Override
  public void validate(ExtensionModel model, ProblemsReporter problemsReporter) {
    new IdempotentExtensionWalker() {

      @Override
      protected void onSource(SourceModel model) {
        model.getAllParameterModels().stream().filter(p -> BACK_PRESSURE_STRATEGY_PARAMETER_NAME.equals(p.getName()))
            .findAny()
            .ifPresent(p -> validateBackPressureStrategyParameter(model, p, problemsReporter));
      }
    }.walk(model);
  }

  private void validateBackPressureStrategyParameter(SourceModel source, ParameterModel parameter,
                                                     ProblemsReporter problemsReporter) {
    if (!(parameter.getType() instanceof StringType)) {
      reportInvalidEnum(source, problemsReporter);
      return;
    }

    String defaultValue = parameter.getDefaultValue() != null ? parameter.getDefaultValue().toString() : null;
    if (defaultValue == null) {
      problemsReporter.addError(new Problem(source, ERROR_PREFIX + "does not specify a default"));
    }

    extractEnumAnnotation(source, parameter, problemsReporter).ifPresent(enumAnnotation -> {
      if (defaultValue == null) {
        return;
      }

      List<String> values = Stream.of(enumAnnotation.getValues()).map(Object::toString).collect(toList());
      if (!values.contains(defaultValue)) {
        problemsReporter.addError(new Problem(source,
                                              ERROR_PREFIX + "has a default value which is not listed as an available option"));
      }
    });
  }

  private Optional<EnumAnnotation> extractEnumAnnotation(SourceModel source, ParameterModel parameter,
                                                         ProblemsReporter problemsReporter) {
    if (!(parameter.getType() instanceof StringType)) {
      problemsReporter.addError(new Problem(source, ERROR_PREFIX + "is not a String"));
      return empty();
    }

    StringType type = (StringType) parameter.getType();

    EnumAnnotation enumAnnotation = type.getAnnotation(EnumAnnotation.class).orElse(null);
    if (enumAnnotation == null) {
      reportInvalidEnum(source, problemsReporter);
      return empty();
    } else {
      for (Object value : enumAnnotation.getValues()) {
        if (value == null) {
          reportInvalidEnum(source, problemsReporter);
          return of(enumAnnotation);
        }

        try {
          BackPressureMode.valueOf(value.toString());
        } catch (IllegalArgumentException e) {
          reportInvalidEnum(source, problemsReporter);
          return of(enumAnnotation);
        }
      }

      return of(enumAnnotation);
    }
  }

  private void reportInvalidEnum(SourceModel source, ProblemsReporter problemsReporter) {
    problemsReporter.addError(new Problem(source, ERROR_PREFIX + "is not an enum of valid modes"));
  }
}
