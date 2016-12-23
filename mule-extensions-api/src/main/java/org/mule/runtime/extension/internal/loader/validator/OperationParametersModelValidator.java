/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_ATTRIBUTE;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.List;

/**
 * Validates that no {@link ParameterModel parameters} named {@code target}, since that word is reserved.
 *
 * @since 1.0
 */
public final class OperationParametersModelValidator implements ExtensionModelValidator {

  private final List<String> reservedWords = ImmutableList.of(TARGET_ATTRIBUTE);

  @Override
  public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
    Multimap<String, String> offenses = LinkedHashMultimap.create();
    new IdempotentExtensionWalker() {

      @Override
      protected void onOperation(OperationModel model) {
        collectOffenses(offenses, model);
      }
    }.walk(extensionModel);


    if (!offenses.isEmpty()) {
      StringBuilder message =
          new StringBuilder("The following operations have parameters named after reserved words. Offending operations are:\n");
      offenses.asMap().forEach((key, values) -> message.append(format("%s: [%s]", key, Joiner.on(", ").join(values))));
      problemsReporter.addError(new Problem(extensionModel, message.toString()));
    }
  }

  private void collectOffenses(Multimap<String, String> offenses, OperationModel operationModel) {
    operationModel.getAllParameterModels().stream().filter(parameter -> reservedWords.contains(parameter.getName()))
        .forEach(parameter -> offenses.put(parameter.getName(), operationModel.getName()));
  }
}
