/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.util.stream.Collectors.toList;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_ACTION_PARAMETER_NAME;
import static org.mule.runtime.extension.api.util.NameUtils.getComponentModelTypeName;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.internal.property.TransactionalActionModelProperty;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * {@link ExtensionModelValidator} implementation in charge of validate the set of "Transactional Parameters" for
 * {@link OperationModel} and {@link SourceModel}
 *
 * @since 4.0
 */
public final class TransactionalParametersValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
    new IdempotentExtensionWalker() {

      @Override
      protected void onSource(SourceModel sourceModel) {
        if (sourceModel.isTransactional()) {
          validateTransactionalParameters(sourceModel, problemsReporter, extensionModel);
        }
      }

      @Override
      protected void onOperation(OperationModel operationModel) {
        if (operationModel.isTransactional()) {
          validateTransactionalParameters(operationModel, problemsReporter, extensionModel);
        }
      }
    }.walk(extensionModel);
  }

  private void validateTransactionalParameters(ComponentModel componentModel, ProblemsReporter problemsReporter,
                                               ExtensionModel extensionModel) {
    List<Pair<ParameterModel, ParameterGroupModel>> parameters = componentModel.getParameterGroupModels()
        .stream()
        .map(group -> group.getParameterModels().stream()
            .map(param -> Pair.of(param, group)))
        .flatMap(stream -> stream).collect(toList());


    parameters.stream().filter(pair -> pair.getKey().getModelProperty(TransactionalActionModelProperty.class).isPresent())
        .forEach(pair -> {
          if (pair.getValue().isShowInDsl()) {
            problemsReporter.addError(new Problem(componentModel,
                                                  "The " + getComponentModelTypeName(componentModel) + " ["
                                                      + componentModel.getName() + "] defines the parameter ["
                                                      + pair.getKey().getName() + "] which is a TransactionalAction. " +
                                                      "Transactional parameters can't be placed inside of Parameter Groups with 'showInDsl' option."));
          }
        });

    parameters.stream().filter(pair -> pair.getKey().getName().equals(TRANSACTIONAL_ACTION_PARAMETER_NAME))
        .findFirst()
        .ifPresent(pair -> {
          if (!pair.getKey().getModelProperty(TransactionalActionModelProperty.class).isPresent()) {
            problemsReporter.addError(new Problem(componentModel,
                                                  "The " + getComponentModelTypeName(componentModel) + " ["
                                                      + componentModel.getName() + "] defines a parameter named: '"
                                                      + TRANSACTIONAL_ACTION_PARAMETER_NAME + "', which is a reserved word"));
          }
        });
  }
}
